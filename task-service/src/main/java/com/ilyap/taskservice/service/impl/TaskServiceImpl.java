package com.ilyap.taskservice.service.impl;

import com.ilyap.loggingstarter.annotation.Logged;
import com.ilyap.taskservice.exception.TaskNotFoundException;
import com.ilyap.taskservice.mapper.TaskCreateUpdateMapper;
import com.ilyap.taskservice.mapper.TaskReadMapper;
import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.repository.TaskRepository;
import com.ilyap.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Logged
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskReadMapper taskReadMapper;
    private final TaskCreateUpdateMapper taskCreateUpdateMapper;
    private final TaskRepository taskRepository;

    @PostAuthorize("@taskPermissionHandler.isTaskUser(#id, principal.username)")
    @Override
    public TaskReadDto findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public Page<TaskReadDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskReadMapper::map);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public Page<TaskReadDto> findAllByUsername(String username, Pageable pageable) {
        return taskRepository.findAllByUsername(username, pageable)
                .map(taskReadMapper::map);
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
