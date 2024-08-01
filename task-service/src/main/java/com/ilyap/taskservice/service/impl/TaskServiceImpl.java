package com.ilyap.taskservice.service.impl;

import com.ilyap.logging.annotation.Logged;
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

    @Override
    public TaskReadDto findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::toDto)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Page<TaskReadDto> findAllByUsername(String username, Pageable pageable) {
        return taskRepository.findAllByOwnerUsername(username, pageable)
                .map(taskReadMapper::toDto);
    }

    @Transactional
    @Override
    public TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto) {
        return Optional.of(taskCreateUpdateDto)
                .map(taskCreateUpdateMapper::toEntity)
                .map(taskRepository::save)
                .map(taskReadMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Task could not be created"));
    }

    @Transactional
    @Override
    public TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto) {
        return taskRepository.findById(id)
                .map(task -> taskCreateUpdateMapper.toEntity(taskCreateUpdateDto, task))
                .map(taskRepository::save)
                .map(taskReadMapper::toDto)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAllByUsername(String username) {
        taskRepository.deleteAllByOwnerUsername(username);
    }
}
