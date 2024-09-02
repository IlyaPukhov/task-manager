package com.ilyap.userservice.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User %s not found".formatted(username));
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
