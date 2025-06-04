package com.islamicapp.qalbconnect.controller;



import com.islamicapp.qalbconnect.Entity.User;
import com.islamicapp.qalbconnect.exception.UsernameAlreadyExistsException;
import com.islamicapp.qalbconnect.service.UserService;
import com.islamicapp.qalbconnect.dto.RegisterRequest; // Import the DTO
import com.islamicapp.qalbconnect.dto.AuthenticationRequest; // Import the DTO

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller; // Use @Controller
import org.springframework.web.bind.annotation.*; // Import all annotations

import java.util.HashMap;
import java.util.Map;

@Controller // This controller will serve both HTML views and API responses
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // --- HTML View Endpoints ---
    @GetMapping("/")
    public String index() {
        return "index"; // Resolves to src/main/resources/templates/index.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Resolves to src/main/resources/templates/register.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Resolves to src/main/resources/templates/login.html
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "homepage"; // Resolves to src/main/resources/templates/homepage.html
    }

    // --- REST API Endpoints (All need @ResponseBody) ---

    @PostMapping("/api/register")
    @ResponseBody // Indicates this method returns data directly, not a view
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = new User(request.getUsername(), request.getPassword());
            User registeredUser = userService.registerNewUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful!");
            response.put("userId", registeredUser.getId());
            response.put("username", registeredUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UsernameAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/api/login")
    @ResponseBody // Indicates this method returns data directly, not a view
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("username", authentication.getName());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/api/user-info")
    @ResponseBody // Indicates this method returns data directly, not a view
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("username", authentication.getName());
            userInfo.put("message", "You are logged in.");
            return ResponseEntity.ok(userInfo);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No user authenticated.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/api/login")
    @ResponseBody // Indicates this method returns data directly, not a view
    public ResponseEntity<Map<String, String>> apiLoginPage(@RequestParam(value = "error", required = false) String error,
                                                            @RequestParam(value = "logout", required = false) String logout) {
        Map<String, String> response = new HashMap<>();
        if (error != null) {
            response.put("message", "Authentication Failed: Invalid username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        if (logout != null) {
            response.put("message", "You have been logged out successfully.");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Please authenticate.");
        return ResponseEntity.ok(response);
    }
}