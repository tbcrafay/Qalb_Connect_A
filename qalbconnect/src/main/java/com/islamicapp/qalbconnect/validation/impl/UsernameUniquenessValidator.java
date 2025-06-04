package com.islamicapp.qalbconnect.validation.impl;

import com.islamicapp.qalbconnect.Entity.User;
import com.islamicapp.qalbconnect.exception.UsernameAlreadyExistsException;
import com.islamicapp.qalbconnect.repository.UserRepository;
import com.islamicapp.qalbconnect.validation.ValidationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernameUniquenessValidator extends ValidationHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' is already taken.");
        }
        checkNext(user); // Pass to the next handler in the chain
    }
}
