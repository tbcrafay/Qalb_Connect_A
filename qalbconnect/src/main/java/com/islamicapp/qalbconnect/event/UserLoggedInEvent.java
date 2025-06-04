package com.islamicapp.qalbconnect.event;

import org.springframework.context.ApplicationEvent;

public class UserLoggedInEvent extends ApplicationEvent {
    private final String username;

    public UserLoggedInEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
