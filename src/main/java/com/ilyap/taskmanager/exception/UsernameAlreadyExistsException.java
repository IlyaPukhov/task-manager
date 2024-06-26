package com.ilyap.taskmanager.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("User %s already exists".formatted(username));
    }
}
