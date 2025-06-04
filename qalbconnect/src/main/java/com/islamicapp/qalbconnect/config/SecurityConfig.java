package com.islamicapp.qalbconnect.config;

import com.islamicapp.qalbconnect.event.UserLoggedInEvent;
import com.islamicapp.qalbconnect.event.UserLoginFailedEvent;
import com.islamicapp.qalbconnect.service.UserDetailsServiceImpl; // Corrected import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // Import AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Import for AuthenticationManager bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Ensure this import path is correct

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Expose AuthenticationManager as a bean for programmatic authentication in MainController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Custom success handler to publish login success event and redirect to homepage
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            eventPublisher.publishEvent(new UserLoggedInEvent(this, authentication.getName()));
            // Redirect to the main homepage URL after successful login
            response.sendRedirect("/homepage");
        };
    }

    // Custom failure handler to publish login failure event and redirect to login page with error
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String username = request.getParameter("username");
            eventPublisher.publishEvent(new UserLoginFailedEvent(this, username != null ? username : "unknown"));
            // Redirect to the HTML login page with an error parameter
            response.sendRedirect("/login?error");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simpler testing. Re-enable for production.
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests((requests) -> requests
                // Permit access to static resources (CSS, JS, Images)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/qalbconnectlogo.png", "/featuresimage.jpg").permitAll()
                // Permit access to the initial landing page, registration, and login HTML pages
                .requestMatchers("/", "/register", "/login").permitAll()
                // Permit access to the API registration endpoint (POST)
                .requestMatchers("/api/register").permitAll()
                // Permit access to the API login endpoint (POST) - Spring Security will handle this
                .requestMatchers("/api/login").permitAll() // This is for the POST request to /api/login
                // Require authentication for the homepage HTML view
                .requestMatchers("/homepage").authenticated()
                // Require authentication for all other API endpoints (e.g., /api/user-info)
                .requestMatchers("/api/**").authenticated()
                // Deny any other requests not explicitly permitted or authenticated
                .anyRequest().denyAll()
            )
            .formLogin((form) -> form
                .loginPage("/login") // The HTML login page URL (GET request)
                .loginProcessingUrl("/api/login") // The URL to which the login form POSTs data (API endpoint)
                .successHandler(customAuthenticationSuccessHandler()) // Use custom success handler
                .failureHandler(customAuthenticationFailureHandler()) // Use custom failure handler
                .permitAll() // Allow all to access the login form and processing
            )
            .logout((logout) -> logout
                .logoutUrl("/logout") // The URL to trigger logout from the HTML page (POST request)
                .logoutSuccessUrl("/login?logout") // Redirect to the HTML login page after successful logout
                .permitAll() // Allow all to access the logout functionality
            );

        return http.build();
    }
}