package com.ilyap.taskservice.security;

import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskPermissionHandler {

    private final TaskRepository taskRepository;

    public boolean isTaskOwner(Long taskId, String username) {
        return taskRepository.findById(taskId)
                .map(task -> task.getOwner().getUsername().equals(username))
                .orElse(false);
    }
}
