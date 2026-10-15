package com.omniarch.talentvela.service;

import com.omniarch.talentvela.entity.User;
import com.omniarch.talentvela.dto.request.ForgotPasswordRequest;
import com.omniarch.talentvela.dto.request.LoginRequest;
import com.omniarch.talentvela.dto.request.RegisterRequest;
import com.omniarch.talentvela.dto.request.ResetPasswordRequest;
import com.omniarch.talentvela.dto.response.AuthResponse;
import com.omniarch.talentvela.dto.response.MessageResponse;

/**
 * Service contract for identity management, registration, credential validation,
 * token issuing, and password recovery.
 */
public interface AuthService {

    /**
     * Handles public self-registration.
     * Allowed roles are restricted to JOB_SEEKER and EMPLOYER.
     * Attempting to register as ADMIN via public registration will throw BadRequestException.
     *
     * @param request the registration details
     * @return AuthResponse containing access token and session metadata
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates user credentials and generates a session JWT token.
     * Checks whether the account status allows authentication.
     *
     * @param request login credentials
     * @return AuthResponse containing token and user profile summary
     */
    AuthResponse login(LoginRequest request);

    /**
     * Initiates password recovery flow and generates a single-use token.
     *
     * @param request email of the account to recover
     * @return MessageResponse confirming the request has been dispatched
     */
    MessageResponse forgotPassword(ForgotPasswordRequest request);

    /**
     * Finalizes password reset with a valid token and matching new passwords.
     *
     * @param request token and new password
     * @return MessageResponse indicating success
     */
    MessageResponse resetPassword(ResetPasswordRequest request);

    /**
     * Administrative provisioning endpoint for ADMIN accounts.
     * Bypasses the public role restriction.
     *
     * @param request user creation payload
     * @return created User entity
     */
    User provisionAdmin(RegisterRequest request);
}
