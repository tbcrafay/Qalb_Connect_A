package com.islamicapp.qalbconnect.validation.impl;

import com.islamicapp.qalbconnect.Entity.User;
import com.islamicapp.qalbconnect.validation.ValidationHandler;
import org.springframework.stereotype.Component;

@Component
public class PasswordLengthValidator extends ValidationHandler {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 8;

    @Override
    public void validate(User user) {
        String password = user.getPassword();
        if (password == null || password.trim().isEmpty() || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Password must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters long.");
        }
        checkNext(user); // Pass to the next handler in the chain
    }
}
