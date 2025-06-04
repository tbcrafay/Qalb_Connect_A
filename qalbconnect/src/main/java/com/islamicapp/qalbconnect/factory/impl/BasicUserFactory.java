package com.islamicapp.qalbconnect.factory.impl;



import com.islamicapp.qalbconnect.Entity.User;
import com.islamicapp.qalbconnect.factory.UserFactory;
import org.springframework.stereotype.Component;

@Component // Make it a Spring bean so it can be @Autowired
public class BasicUserFactory implements UserFactory {
    @Override
    public User createUser(String username, String password) {
        // In a more complex scenario, this could involve more intricate object initialization
        return new User(username, password);
    }
}