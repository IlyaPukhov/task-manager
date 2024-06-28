package com.ilyap.taskmanager.security;

import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.repository.TaskRepository;
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

    public boolean isTaskUser(Long taskId, String username) {
        return isTaskOwner(taskId, username)
                || taskRepository.findById(taskId)
                .map(Task::getUserTasks)
                .stream().flatMap(List::stream)
                .map(ut -> ut.getUser().getUsername())
                .anyMatch(username::equals);
    }
}
