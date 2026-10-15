package com.omniarch.talentvela.security;

import com.omniarch.talentvela.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Component responsible for issuing, parsing, and validating JSON Web Tokens (JWT).
 */
@Component
public class JwtTokenProvider implements TokenProvider {

    // Default 256-bit secret key for HMAC-SHA256 (overridden via application.yml)
    @Value("${app.jwt.secret:JobPortalVerySecretKeyThatMustBeAtLeast256BitsLongForHmacSha256SecurityRequirement123456}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-seconds:86400}")
    private long jwtExpirationInSeconds;

    public JwtTokenProvider() {
    }

    public JwtTokenProvider(String jwtSecret, long jwtExpirationInSeconds) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInSeconds = jwtExpirationInSeconds;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a signed JWT token containing user identity and authority claims.
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        claims.put("role", user.getRole().name());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (jwtExpirationInSeconds * 1000));

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts email (subject) from token.
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject() != null ? claims.getSubject() : claims.get("email", String.class);
    }

    /**
     * Extracts role name from token claims.
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * Validates signature and expiration of token.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationSeconds() {
        return jwtExpirationInSeconds;
    }
}
