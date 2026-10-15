package com.omniarch.talentvela.repository;

import com.omniarch.talentvela.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository abstraction for PasswordResetToken lifecycle and verification.
 * Extends Spring Data JPA repository for database-backed persistence (MySQL / H2).
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUserEmail(String email);
}
