package com.omniarch.talentvela.dto.response;

import com.omniarch.talentvela.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Authentication response containing the JWT bearer token, expiration, and session identity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;

    @lombok.Builder.Default
    private String tokenType = "Bearer";

    private long expiresIn;
    private SessionUser user;

    public AuthResponse() {
        this.tokenType = "Bearer";
    }

    public AuthResponse(String accessToken, String tokenType, long expiresIn, SessionUser user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static AuthResponse of(String accessToken, long expiresIn, User user) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .user(SessionUser.fromUser(user))
                .build();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public SessionUser getUser() {
        return user;
    }

    public void setUser(SessionUser user) {
        this.user = user;
    }

    public static class Builder {
        private String accessToken;
        private String tokenType = "Bearer";
        private long expiresIn;
        private SessionUser user;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public Builder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder user(SessionUser user) {
            this.user = user;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(accessToken, tokenType, expiresIn, user);
        }
    }
}
