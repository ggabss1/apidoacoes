package com.doacoes.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doacoes.api.models.Help;
import com.doacoes.api.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
    Optional<Help> findById(Long id);
    List<Help> findByIsActive(Boolean isActive);
    List<Help> findByUser(User user);
    List<Help> findByUserAndIsActive(User user, Boolean isActive);
    List<Help> findByUserAndIsActiveOrderByCreatedAtDesc(User user, Boolean isActive);
}