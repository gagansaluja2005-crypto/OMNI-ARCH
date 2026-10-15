package com.omniarch.talentvela.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Domain entity representing a password reset token with expiration and usage status.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @Column(name = "token", nullable = false, unique = true, length = 100)
    private String token;

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "used", nullable = false)
    @lombok.Builder.Default
    private boolean used = false;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, String userEmail, Instant expiryDate) {
        this.token = token;
        this.userEmail = userEmail;
        this.expiryDate = expiryDate;
        this.used = false;
    }

    public PasswordResetToken(String token, String userEmail, Instant expiryDate, boolean used) {
        this.token = token;
        this.userEmail = userEmail;
        this.expiryDate = expiryDate;
        this.used = used;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public boolean isValid() {
        return !used && !isExpired();
    }

    public static class Builder {
        private String token;
        private String userEmail;
        private Instant expiryDate;
        private boolean used = false;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder expiryDate(Instant expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder used(boolean used) {
            this.used = used;
            return this;
        }

        public PasswordResetToken build() {
            return new PasswordResetToken(token, userEmail, expiryDate, used);
        }
    }

    public boolean getUsed() { return used; }
}
