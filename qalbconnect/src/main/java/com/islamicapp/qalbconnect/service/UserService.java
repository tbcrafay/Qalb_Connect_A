package com.islamicapp.qalbconnect.service;

import com.islamicapp.qalbconnect.Entity.User;
import com.islamicapp.qalbconnect.event.UserRegisteredEvent;
import com.islamicapp.qalbconnect.factory.UserFactory;
import com.islamicapp.qalbconnect.repository.UserRepository;
import com.islamicapp.qalbconnect.validation.ValidationHandler;
import com.islamicapp.qalbconnect.validation.impl.PasswordLengthValidator;
import com.islamicapp.qalbconnect.validation.impl.UsernameUniquenessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher; // For Observer pattern
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserFactory userFactory; // Inject our UserFactory

    @Autowired
    private UsernameUniquenessValidator usernameUniquenessValidator; // Inject validators
    @Autowired
    private PasswordLengthValidator passwordLengthValidator;

    @Autowired
    private ApplicationEventPublisher eventPublisher; // Inject event publisher for Observer pattern

    /**
     * Registers a new user after validating username uniqueness and password strength.
     *
     * @param rawUser The User object containing username and raw password.
     * @return The registered User object.
     * @throws RuntimeException if validation fails (UsernameAlreadyExistsException or IllegalArgumentException).
     */
    public User registerNewUser(User rawUser) {
        // 1. Create the user object using the Factory Method
        User userToRegister = userFactory.createUser(rawUser.getUsername(), rawUser.getPassword());

        // 2. Apply validation using Chain of Responsibility
        // Build the chain: UsernameUniquenessValidator -> PasswordLengthValidator
        usernameUniquenessValidator.setNext(passwordLengthValidator);
        usernameUniquenessValidator.validate(userToRegister); // Start the chain

        // 3. Encode password
        userToRegister.setPassword(passwordEncoder.encode(rawUser.getPassword()));

        // 4. Save the new user document to MongoDB
        User registeredUser = userRepository.save(userToRegister);

        // 5. Publish UserRegisteredEvent (Observer Pattern)
        eventPublisher.publishEvent(new UserRegisteredEvent(this, registeredUser.getUsername()));

        return registeredUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
