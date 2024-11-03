package com.doacoes.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doacoes.api.models.Chat;
import com.doacoes.api.models.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Boolean existsByUser1AndUser2(User user1, User user2);
}
