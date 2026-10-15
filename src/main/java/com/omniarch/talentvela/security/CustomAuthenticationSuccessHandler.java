package com.omniarch.talentvela.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Handles role-based redirection immediately upon successful authentication:
 * - ROLE_JOB_SEEKER -> /seeker/dashboard
 * - ROLE_EMPLOYER   -> /employer/dashboard
 * - ROLE_ADMIN      -> /admin/dashboard
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);
        response.sendRedirect(request.getContextPath() + targetUrl);
    }

    public static String determineTargetUrl(Authentication authentication) {
        if (authentication == null) {
            return "/login";
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_JOB_SEEKER".equalsIgnoreCase(role)) {
                return "/seeker/dashboard";
            } else if ("ROLE_EMPLOYER".equalsIgnoreCase(role)) {
                return "/employer/dashboard";
            } else if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
                return "/admin/dashboard";
            }
        }
        return "/seeker/dashboard";
    }
}
