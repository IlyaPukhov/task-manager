package com.ilyap.taskservice.security;

import com.ilyap.taskservice.exception.TaskNotFoundException;
import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskPermissionHandler {

    private final TaskRepository taskRepository;

    public boolean isTaskOwner(Long taskId, String username) {
        String taskOwnerUsername = taskRepository.findById(taskId)
                .map(Task::getOwnerUsername)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        return username.equals(taskOwnerUsername);
    }
}
