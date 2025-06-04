package com.islamicapp.qalbconnect.factory;

// src/main/java/com/qalbconnect/islamiapp/factory/UserFactory.java


import com.islamicapp.qalbconnect.Entity.User;

public interface UserFactory {
    User createUser(String username, String password);
}


