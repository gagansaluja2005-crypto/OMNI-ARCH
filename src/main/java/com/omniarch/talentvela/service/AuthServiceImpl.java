package com.omniarch.talentvela.service;

import com.omniarch.talentvela.entity.AccountStatus;
import com.omniarch.talentvela.entity.PasswordResetToken;
import com.omniarch.talentvela.entity.Role;
import com.omniarch.talentvela.entity.User;
import com.omniarch.talentvela.dto.request.ForgotPasswordRequest;
import com.omniarch.talentvela.dto.request.LoginRequest;
import com.omniarch.talentvela.dto.request.RegisterRequest;
import com.omniarch.talentvela.dto.request.ResetPasswordRequest;
import com.omniarch.talentvela.dto.response.AuthResponse;
import com.omniarch.talentvela.dto.response.MessageResponse;
import com.omniarch.talentvela.exception.BadRequestException;
import com.omniarch.talentvela.exception.ResourceNotFoundException;
import com.omniarch.talentvela.exception.UnauthorizedException;
import com.omniarch.talentvela.repository.PasswordResetTokenRepository;
import com.omniarch.talentvela.repository.UserRepository;
import com.omniarch.talentvela.security.TokenProvider;
import com.omniarch.talentvela.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Implementation of AuthService business logic.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordResetTokenRepository tokenRepository,
                           PasswordEncoder passwordEncoder,
                           TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Enforce rule: Public registration permits ONLY JOB_SEEKER and EMPLOYER
        if (request.getRole() == null || !request.getRole().isPublicRegistrationAllowed()) {
            throw new BadRequestException("Public registration is only allowed for JOB_SEEKER and EMPLOYER roles. ADMIN accounts cannot be registered publicly.");
        }

        // Validate uniqueness of email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address is already registered: " + request.getEmail());
        }

        // Validate uniqueness of phone
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number is already registered: " + request.getPhone());
        }

        // Hash raw password
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase().trim())
                .phone(request.getPhone().trim())
                .passwordHash(passwordHash)
                .role(request.getRole())
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        User savedUser = userRepository.save(user);

        // Generate JWT token for immediate session activation
        String token = tokenProvider.generateToken(savedUser);

        return AuthResponse.of(token, tokenProvider.getExpirationSeconds(), savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        if (!user.getAccountStatus().canAuthenticate()) {
            throw new UnauthorizedException("Account is currently " + user.getAccountStatus() + ". Access denied.");
        }

        String token = tokenProvider.generateToken(user);
        return AuthResponse.of(token, tokenProvider.getExpirationSeconds(), user);
    }

    @Override
    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Invalidate prior pending tokens
        tokenRepository.deleteByUserEmail(user.getEmail());

        // Create new single-use token valid for 15 minutes
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(15, ChronoUnit.MINUTES);
        PasswordResetToken resetToken = new PasswordResetToken(token, user.getEmail(), expiryDate);
        tokenRepository.save(resetToken);

        // Dispatched via notification service in production
        return MessageResponse.success("Password reset instructions dispatched to registered email: " + user.getEmail());
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        if (!request.doPasswordsMatch()) {
            throw new BadRequestException("New password and confirmation password do not match.");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired password reset token."));

        if (!resetToken.isValid()) {
            throw new BadRequestException("Password reset token is invalid, already used, or expired.");
        }

        User user = userRepository.findByEmail(resetToken.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", resetToken.getUserEmail()));

        // Update password and timestamp
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        // Mark token used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return MessageResponse.success("Password has been successfully reset. You may now log in with your new password.");
    }

    @Override
    public User provisionAdmin(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email address is already in use: " + email);
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        User adminUser = User.builder()
                .fullName(request.getFullName())
                .email(email)
                .phone(request.getPhone().trim())
                .passwordHash(passwordHash)
                .role(Role.ADMIN)
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return userRepository.save(adminUser);
    }
}
