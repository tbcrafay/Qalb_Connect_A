package com.islamicapp.qalbconnect.event;

import org.springframework.context.ApplicationEvent;

public class UserLoginFailedEvent extends ApplicationEvent {
    private final String username; // The username that failed to log in

    public UserLoginFailedEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}