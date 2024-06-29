package com.ilyap.taskservice.service.impl;

import com.ilyap.loggingstarter.annotation.Logged;
import com.ilyap.taskservice.exception.TaskNotFoundException;
import com.ilyap.taskservice.exception.UserTaskAlreadyExistsException;
import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.model.entity.TaskManagerUser;
import com.ilyap.taskservice.model.entity.UserTask;
import com.ilyap.taskservice.repository.TaskRepository;
import com.ilyap.taskservice.repository.UserRepository;
import com.ilyap.taskservice.repository.UserTaskRepository;
import com.ilyap.taskservice.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Logged
@RequiredArgsConstructor
@Transactional
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#taskId, principal.username)")
    @Override
    public void addTaskUser(Long taskId, String username) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        TaskManagerUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User %s not found".formatted(username))
        );

        userTaskRepository.findByTaskIdAndUserUsername(taskId, username)
                .ifPresent(ut -> {
                    throw new UserTaskAlreadyExistsException(ut);
                });

        userTaskRepository.save(new UserTask()
                .setTask(task)
                .setUser(user)
        );
    }

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#taskId, principal.username)")
    @Override
    public void deleteTaskUser(Long taskId, String username) {
        userTaskRepository.deleteByTaskIdAndUserUsername(taskId, username);
    }
}
