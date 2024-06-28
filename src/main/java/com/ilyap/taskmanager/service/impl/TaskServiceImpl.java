package com.ilyap.taskmanager.service.impl;

import com.ilyap.taskmanager.exception.TaskNotFoundException;
import com.ilyap.taskmanager.mapper.TaskCreateUpdateMapper;
import com.ilyap.taskmanager.mapper.TaskReadMapper;
import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.repository.TaskRepository;
import com.ilyap.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskReadMapper taskReadMapper;
    private final TaskCreateUpdateMapper taskCreateUpdateMapper;
    private final TaskRepository taskRepository;

    @PostAuthorize("@taskPermissionHandler.isTaskUser(#id, principal.username)")
    @Override
    public TaskReadDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public List<TaskReadDto> getAll() {
        return taskRepository.findAll().stream()
                .map(taskReadMapper::map)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public List<TaskReadDto> getAllByUsername(String username) {
        return taskRepository.getAllByUsername(username).stream()
                .map(taskReadMapper::map)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    @Override
    public TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto) {
        return Optional.of(taskCreateUpdateDto)
                .map(taskCreateUpdateMapper::map)
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new RuntimeException("Task could not be created"));
    }

    @PreAuthorize("@taskPermissionHandler.isTaskUser(#id, principal.username)")
    @Transactional
    @Override
    public TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto) {
        return taskRepository.findById(id)
                .map(task -> taskCreateUpdateMapper.map(taskCreateUpdateDto, task))
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#id, principal.username)")
    @Transactional
    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @PreAuthorize("#username == principal.username")
    @Transactional
    @Override
    public void deleteAllByUsername(String username) {
        taskRepository.deleteAllByUsername(username);
    }
}
