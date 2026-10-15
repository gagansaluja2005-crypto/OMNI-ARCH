package com.omniarch.talentvela.controller;

import com.omniarch.talentvela.dto.request.RegisterRequest;
import com.omniarch.talentvela.entity.*;
import com.omniarch.talentvela.repository.EmployerProfileRepository;
import com.omniarch.talentvela.repository.JobSeekerProfileRepository;
import com.omniarch.talentvela.repository.UserRepository;
import com.omniarch.talentvela.security.CustomAuthenticationSuccessHandler;
import com.omniarch.talentvela.security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller managing browser-based authentication flows:
 * - Login & Registration views + form processing
 * - Role-based post-login / post-registration redirects (Task 6)
 * - Session logout handling (Task 7)
 * - Access-denied route handling (Task 7)
 */
@Controller
public class WebAuthController {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository seekerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public WebAuthController(UserRepository userRepository,
                             JobSeekerProfileRepository seekerProfileRepository,
                             EmployerProfileRepository employerProfileRepository,
                             PasswordEncoder passwordEncoder,
                             UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.seekerProfileRepository = seekerProfileRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Root URL router.
     */
    @GetMapping("/")
    public String root() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (SecurityUtil.isAuthenticated()) {
            return "redirect:" + CustomAuthenticationSuccessHandler.determineTargetUrl(auth);
        }
        return "redirect:/seeker/jobs";
    }

    /**
     * Renders Login page.
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (SecurityUtil.isAuthenticated()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return "redirect:" + CustomAuthenticationSuccessHandler.determineTargetUrl(auth);
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please verify your credentials.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been successfully logged out.");
        }
        model.addAttribute("title", "Sign In - TalentVela");
        return "auth/login";
    }

    /**
     * Renders Register page.
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        if (SecurityUtil.isAuthenticated()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return "redirect:" + CustomAuthenticationSuccessHandler.determineTargetUrl(auth);
        }
        model.addAttribute("title", "Create Account - TalentVela");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    /**
     * Handles browser-based registration form submission.
     */
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute RegisterRequest request,
                                      HttpServletRequest httpRequest,
                                      RedirectAttributes redirectAttributes) {
        // Enforce RBAC registration rule: public cannot register ADMIN
        if (request.getRole() == null || !request.getRole().isPublicRegistrationAllowed()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Public registration is only permitted for JOB_SEEKER and EMPLOYER roles.");
            return "redirect:/register";
        }

        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        if (email.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email address is required.");
            return "redirect:/register";
        }

        if (userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email '" + email + "' is already registered. Please log in.");
            return "redirect:/register";
        }

        if (request.getPhone() != null && !request.getPhone().isBlank() && userRepository.existsByPhone(request.getPhone().trim())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phone number is already associated with an account.");
            return "redirect:/register";
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 8 characters long.");
            return "redirect:/register";
        }

        // Save new user with BCrypt password hash
        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setAccountStatus(AccountStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        // Pre-initialize corresponding profile entity
        if (savedUser.getRole() == Role.JOB_SEEKER) {
            JobSeekerProfile profile = new JobSeekerProfile();
            profile.setUserId(savedUser.getId());
            seekerProfileRepository.save(profile);
        } else if (savedUser.getRole() == Role.EMPLOYER) {
            EmployerProfile profile = new EmployerProfile();
            profile.setUserId(savedUser.getId());
            profile.setCompanyName(savedUser.getFullName() + " Company");
            employerProfileRepository.save(profile);
        }

        // Establish authenticated session immediately
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Welcome to TalentVela.");

        // Role-based post-registration redirect
        if (savedUser.getRole() == Role.JOB_SEEKER) {
            return "redirect:/seeker/dashboard";
        } else if (savedUser.getRole() == Role.EMPLOYER) {
            return "redirect:/employer/dashboard";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    /**
     * Explicit web logout route (Task 7).
     */
    @GetMapping({"/logout", "/auth/logout"})
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    /**
     * Access Denied view (Task 7).
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("title", "Access Denied - TalentVela");
        model.addAttribute("currentUser", SecurityUtil.getCurrentUserPrincipal().orElse(null));
        return "auth/access-denied";
    }
}
