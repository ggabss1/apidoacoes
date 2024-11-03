package com.doacoes.api.controllers;

import com.doacoes.api.models.Chat;
import com.doacoes.api.models.Message;
import com.doacoes.api.models.User;
import com.doacoes.api.payload.request.ChatRequest;
import com.doacoes.api.payload.request.MessageRequest;
import com.doacoes.api.payload.response.ChatResponse;
import com.doacoes.api.payload.response.ChatMessageResponse;
import com.doacoes.api.repository.ChatRepository;
import com.doacoes.api.repository.MessageRepository;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.UserRepository;
import com.doacoes.api.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getChats(@RequestHeader Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (authUserId != userId) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only get chats for yourself!"));
        }

        Optional<User> user = userRepository.findById(userId);

        List<Chat> chats = chatRepository.findAll();
        List<ChatResponse> userChats = new ArrayList<>();
        for (Chat chat : chats) {
            if (chat.getUser1().getId() == user.get().getId() || chat.getUser2().getId() == user.get().getId()) {
                Long chatId = chat.getId();
                Long otherUser = chat.getUser1().getId() == user.get().getId() ? chat.getUser2().getId() : chat.getUser1().getId();
                String userName = chat.getUser1().getId() == user.get().getId() ? chat.getUser2().get_full_name() : chat.getUser1().get_full_name();
                Long unreadCount = messageRepository.countByChatIdAndIsRead(chatId, false);
                boolean hasUnreadMessages = unreadCount > 0;
                userChats.add(new ChatResponse(chatId, otherUser, userName, unreadCount, hasUnreadMessages));
            }
        }
        return ResponseEntity.ok(userChats);
    }


    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody ChatRequest chatRequest) {
        Optional<User> user1 = userRepository.findById(chatRequest.getUser1());
        Optional<User> user2 = userRepository.findById(chatRequest.getUser2());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (authUserId != chatRequest.getUser1() || authUserId != chatRequest.getUser2()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only create a chat for yourself!"));
        }

        if (chatRepository.existsByUser1AndUser2(user1.get(), user2.get())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Chat already exists!"));
        }

        if ((user1.isEmpty() || user2.isEmpty()) && (user1.get() != user2.get())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        Chat chat = new Chat(user1.get(), user2.get());
        chatRepository.save(chat);

        return ResponseEntity.ok(new MessageResponse("Chat created successfully!"));
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable Long chatId, @RequestHeader Long senderId, @RequestBody MessageRequest messageRequest) {
        if (!chatRepository.existsById(chatId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Chat not found!"));
        }
        if (messageRequest.getText() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Message cannot be null!"));
        }
        if (messageRequest.getText().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Message cannot be empty!"));
        }

        if (!userRepository.existsById(senderId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Sender not found!"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (authUserId != senderId) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only send messages as yourself!"));
        }

        Optional<Chat> chat = chatRepository.findById(chatId);
        Optional<User> sender = userRepository.findById(senderId);


        Message message = new Message(chat.get(), sender.get(), messageRequest.getText());
        messageRepository.save(message);

        return ResponseEntity.ok(new MessageResponse("Message sent successfully!"));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Chat not found!"));
        }

        Optional<Chat> chat_optional = chatRepository.findById(chatId);
        Chat chat = chat_optional.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        if (authUserId != chat.getUser1().getId() && authUserId != chat.getUser2().getId()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only get messages for yourself!"));
        }

        if (messageRepository.findByChat(chat, Sort.by("sentAt")).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No messages found!"));
        }

        List<Message> messages = messageRepository.findByChat(chat, Sort.by("sentAt"));

        if (messages.isEmpty()) {
            return ResponseEntity.ok("[]");
        }

        List<ChatMessageResponse> messageList = new ArrayList<>();
        for (Message message : messages) {
            messageList.add(new ChatMessageResponse(message.getId(), message.getSender().getId(), message.getText(), message.getSentAt().toString(), message.isRead()));
        }

        return ResponseEntity.ok(messageList);    
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        if (!messageRepository.existsById(messageId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Message not found!"));
        }
        if (messageRepository.findById(messageId).get().isRead()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Message already marked as read!"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        Long senderId = messageRepository.findById(messageId).get().getSender().getId();
        if (authUserId == senderId) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only mark messages as read for yourself!"));
        }

        if (authUserId != messageRepository.findById(messageId).get().getChat().getUser1().getId() && authUserId != messageRepository.findById(messageId).get().getChat().getUser2().getId()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only mark messages as read for yourself!"));
        }

        Optional<Message> message_optional = messageRepository.findById(messageId);
        Message message = message_optional.get();
        message.setRead(true);
        messageRepository.save(message);
        return ResponseEntity.ok(new MessageResponse("Message marked as read!"));
    }
}