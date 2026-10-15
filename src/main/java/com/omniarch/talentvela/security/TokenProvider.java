package com.omniarch.talentvela.security;

import com.omniarch.talentvela.entity.User;

/**
 * Token provider contract for issuing and validating authentication tokens.
 */
public interface TokenProvider {

    String generateToken(User user);

    String getEmailFromToken(String token);

    String getRoleFromToken(String token);

    boolean validateToken(String token);

    long getExpirationSeconds();
}
