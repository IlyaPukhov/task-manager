package com.ilyap.taskmanager.service.impl;

import com.ilyap.taskmanager.exception.TaskNotFoundException;
import com.ilyap.taskmanager.exception.UserTaskAlreadyExistsException;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.model.entity.TaskManagerUser;
import com.ilyap.taskmanager.model.entity.UserTask;
import com.ilyap.taskmanager.repository.TaskRepository;
import com.ilyap.taskmanager.repository.UserRepository;
import com.ilyap.taskmanager.repository.UserTaskRepository;
import com.ilyap.taskmanager.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

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

    @Override
    public void deleteTaskUser(Long taskId, String username) {
        userTaskRepository.deleteByTaskIdAndUserUsername(taskId, username);
    }
}
