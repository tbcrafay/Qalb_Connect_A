package com.islamicapp.qalbconnect.validation;

import com.islamicapp.qalbconnect.Entity.User;

public abstract class ValidationHandler {
    private ValidationHandler next;

    public void setNext(ValidationHandler next) {
        this.next = next;
    }

    // Abstract method to be implemented by concrete validators
    public abstract void validate(User user);

    // Helper method to pass the validation to the next handler in the chain
    protected void checkNext(User user) {
        if (next != null) {
            next.validate(user);
        }
    }
}
