package com.omniarch.talentvela.security;

import com.omniarch.talentvela.entity.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utility helper class providing convenient static access to the currently authenticated user.
 * 
 * Provides to Kushagra (JobSeekerController) and Ashutosh (EmployerController):
 * Easy extraction of current user ID, email, role, and authentication status.
 */
public final class SecurityUtil {

    private SecurityUtil() {
        // Prevent instantiation
    }

    /**
     * Retrieves the currently authenticated UserPrincipal from Spring SecurityContext.
     *
     * @return Optional containing UserPrincipal if authenticated, empty otherwise.
     */
    public static Optional<UserPrincipal> getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal) {
            return Optional.of((UserPrincipal) principal);
        }

        return Optional.empty();
    }

    /**
     * Gets the ID of the currently logged-in user.
     *
     * @return user ID as Long, or null if not authenticated.
     */
    public static Long getCurrentUserId() {
        return getCurrentUserPrincipal().map(UserPrincipal::getId).orElse(null);
    }

    /**
     * Gets the ID of the currently logged-in user with a fallback value (useful for dev/test stubs).
     *
     * @param fallback default ID if unauthenticated
     * @return user ID or fallback
     */
    public static Integer getCurrentUserIdOrFallback(Integer fallback) {
        Long id = getCurrentUserId();
        return id != null ? id.intValue() : fallback;
    }

    /**
     * Gets the email address of the currently logged-in user.
     *
     * @return email String, or null if not authenticated.
     */
    public static String getCurrentUserEmail() {
        return getCurrentUserPrincipal().map(UserPrincipal::getEmail).orElse(null);
    }

    /**
     * Gets the role of the currently logged-in user.
     *
     * @return Role enum, or null if not authenticated.
     */
    public static Role getCurrentUserRole() {
        return getCurrentUserPrincipal().map(UserPrincipal::getRole).orElse(null);
    }

    /**
     * Checks if the current visitor is authenticated.
     *
     * @return true if logged in, false if anonymous.
     */
    public static boolean isAuthenticated() {
        return getCurrentUserPrincipal().isPresent();
    }

    /**
     * Checks if the currently authenticated user has the specified role.
     *
     * @param role required role
     * @return true if user has the role, false otherwise.
     */
    public static boolean hasRole(Role role) {
        if (role == null) return false;
        return getCurrentUserRole() == role;
    }

    /**
     * Checks if the currently authenticated user is a Job Seeker.
     */
    public static boolean isJobSeeker() {
        return hasRole(Role.JOB_SEEKER);
    }

    /**
     * Checks if the currently authenticated user is an Employer.
     */
    public static boolean isEmployer() {
        return hasRole(Role.EMPLOYER);
    }

    /**
     * Checks if the currently authenticated user is an Admin.
     */
    public static boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }
}
