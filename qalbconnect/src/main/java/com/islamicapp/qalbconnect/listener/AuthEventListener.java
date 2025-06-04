package com.islamicapp.qalbconnect.listener;

import com.islamicapp.qalbconnect.command.impl.LogFailedLoginCommand;
import com.islamicapp.qalbconnect.command.impl.LogRegistrationCommand;
import com.islamicapp.qalbconnect.command.impl.LogSuccessfulLoginCommand;
import com.islamicapp.qalbconnect.event.UserLoggedInEvent;
import com.islamicapp.qalbconnect.event.UserLoginFailedEvent;
import com.islamicapp.qalbconnect.event.UserRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuthEventListener {

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        // Execute a command when a user registers
        new LogRegistrationCommand(event.getUsername()).execute();
        // Could also trigger email sending, etc.
    }

    @EventListener
    public void handleUserLoggedInEvent(UserLoggedInEvent event) {
        // Execute a command when a user logs in successfully
        new LogSuccessfulLoginCommand(event.getUsername()).execute();
        // Could also update user last login time
    }

    @EventListener
    public void handleUserLoginFailedEvent(UserLoginFailedEvent event) {
        // Execute a command when a user login fails
        new LogFailedLoginCommand(event.getUsername()).execute();
        // Could also trigger security alerts
    }
}