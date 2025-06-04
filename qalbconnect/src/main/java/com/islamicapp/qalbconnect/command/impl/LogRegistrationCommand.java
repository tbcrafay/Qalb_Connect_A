package com.islamicapp.qalbconnect.command.impl;

import com.islamicapp.qalbconnect.command.AuthCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRegistrationCommand implements AuthCommand {
    private static final Logger logger = LoggerFactory.getLogger(LogRegistrationCommand.class);
    private final String username;

    public LogRegistrationCommand(String username) {
        this.username = username;
    }

    @Override
    public void execute() {
        logger.info("AUDIT: User '{}' successfully registered.", username);
        // In a real application, this would save to an audit log database or file
    }
}
