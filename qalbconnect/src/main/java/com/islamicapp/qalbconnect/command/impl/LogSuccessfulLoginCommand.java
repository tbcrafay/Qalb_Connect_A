package com.islamicapp.qalbconnect.command.impl;

import com.islamicapp.qalbconnect.command.AuthCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSuccessfulLoginCommand implements AuthCommand {
    private static final Logger logger = LoggerFactory.getLogger(LogSuccessfulLoginCommand.class);
    private final String username;

    public LogSuccessfulLoginCommand(String username) {
        this.username = username;
    }

    @Override
    public void execute() {
        logger.info("AUDIT: User '{}' successfully logged in.", username);
        // In a real application, this would save to an audit log database or file
    }
}
