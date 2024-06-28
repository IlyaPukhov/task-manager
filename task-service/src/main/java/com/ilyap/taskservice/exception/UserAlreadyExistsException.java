package com.ilyap.taskservice.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("User %s already exists".formatted(username));
    }
}
