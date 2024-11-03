package com.doacoes.api.controllers;


import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.payload.response.UserResponse;
import com.doacoes.api.repository.UserRepository;
import com.doacoes.api.security.services.UserDetailsImpl;
import com.doacoes.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        UserResponse user = new UserResponse(userId, userRepository.findById(userId).get().getFirst_name(), userRepository.findById(userId).get().getLast_name(), userRepository.findById(userId).get().getBirth_date(), userRepository.findById(userId).get().getEmail());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserResponse userResponse) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        if (authUserId != userId) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only update yourself!"));
        }

        Optional<User> user = userRepository.findById(userId);
        user.get().setFirst_name(userResponse.getFirst_name());
        user.get().setLast_name(userResponse.getLast_name());
        user.get().setBirth_date(userResponse.getBirth_date());

        userRepository.save(user.get());
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

}