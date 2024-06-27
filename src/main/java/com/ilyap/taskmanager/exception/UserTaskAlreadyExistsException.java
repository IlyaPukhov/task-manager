package com.ilyap.taskmanager.exception;

import com.ilyap.taskmanager.model.entity.UserTask;

public class UserTaskAlreadyExistsException extends RuntimeException {

    public UserTaskAlreadyExistsException(UserTask userTask) {
        super("User %s has already been added to the task \"%s\"".formatted(userTask.getUser().getUsername(), userTask.getTask().getTitle()));
    }
}
