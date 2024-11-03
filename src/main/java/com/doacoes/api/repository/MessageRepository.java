package com.doacoes.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doacoes.api.models.Message;
import com.doacoes.api.models.Chat;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findByChat(Chat chat, Sort sort);
  Long countByChatIdAndIsRead(Long chatId, boolean isRead);
}
