package com.islamicapp.qalbconnect.command.impl;

import com.islamicapp.qalbconnect.command.AuthCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFailedLoginCommand implements AuthCommand {
    private static final Logger logger = LoggerFactory.getLogger(LogFailedLoginCommand.class);
    private final String username; // The username that attempted to log in

    public LogFailedLoginCommand(String username) {
        this.username = username;
    }

    @Override
    public void execute() {
        logger.warn("AUDIT: Failed login attempt for user '{}'.", username);
        // In a real application, this would save to an audit log database or file
    }
}
