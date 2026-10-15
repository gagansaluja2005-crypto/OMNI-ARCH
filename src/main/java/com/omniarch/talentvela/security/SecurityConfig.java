package com.omniarch.talentvela.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security Configuration implementing:
 * 1. BCrypt password hashing (cost factor 12)
 * 2. Role-Based Access Control (RBAC) across Web and API routes
 * 3. Form login with role-based post-login redirects
 * 4. Stateless JWT support for REST API endpoints
 * 5. Session-based authentication for Web UI pages
 * 6. Session logout flow and custom access denied handling
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                          AccessDeniedHandlerImpl accessDeniedHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    /**
     * BCrypt Password Encoder bean for cryptographic one-way hashing of passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for seamless form + REST operation
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Exception handling (401 unauthorized & 403 forbidden)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // Support sessions for Thymeleaf browser navigation while JWT works for REST
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Define endpoint authorization rules according to the Access Matrix
                .authorizeHttpRequests(authorize -> authorize
                        // --- 1. PUBLIC ROUTES ---
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/access-denied",
                                "/auth/**",
                                "/api/auth/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/error",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/health"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/jobs/**", "/api/jobs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/companies/**").permitAll()

                        // --- 2. SEEKER-ONLY ROUTES (Task 7 Enforced) ---
                        .requestMatchers("/seeker/**", "/jobseeker/**", "/api/seeker/**").hasRole("JOB_SEEKER")

                        // --- 3. EMPLOYER-ONLY ROUTES (Task 7 Enforced) ---
                        .requestMatchers("/employer/**", "/api/employer/**").hasRole("EMPLOYER")

                        // --- 4. ADMIN-ONLY ROUTES (Task 7 Enforced) ---
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                        // --- 5. AUTHENTICATED FALLBACK ---
                        .anyRequest().authenticated()
                )

                // Form Login configuration with role-based redirect
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // Session Logout configuration (Task 7 Enforced)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        // Register custom JWT authentication filter before standard username/password filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
