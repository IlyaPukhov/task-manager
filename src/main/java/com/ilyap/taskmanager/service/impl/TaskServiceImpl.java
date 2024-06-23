package com.ilyap.taskmanager.service.impl;

import com.ilyap.taskmanager.mapper.TaskCreateUpdateMapper;
import com.ilyap.taskmanager.mapper.TaskReadMapper;
import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.repository.TaskRepository;
import com.ilyap.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskReadMapper taskReadMapper;
    private final TaskCreateUpdateMapper taskCreateUpdateMapper;
    private final TaskRepository taskRepository;

    @Override
    public TaskReadDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::map)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<TaskReadDto> getAll() {
        return taskRepository.findAll().stream()
                .map(taskReadMapper::map)
                .toList();
    }

    @Override
    public List<TaskReadDto> getAllByUserId(Long userId) {
        return taskRepository.getAllByUserId(userId).stream()
                .map(taskReadMapper::map)
                .toList();
    }

    @Transactional
    @Override
    public TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto) {
        return Optional.of(taskCreateUpdateDto)
                .map(taskCreateUpdateMapper::map)
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new RuntimeException("Task could not be created"));
    }

    @Transactional
    @Override
    public TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto) {
        return taskRepository.findById(id)
                .map(task -> taskCreateUpdateMapper.map(taskCreateUpdateDto, task))
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        taskRepository.findById(id)
                .ifPresentOrElse(taskRepository::delete, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Transactional
    @Override
    public void deleteAllByUserId(Long userId) {
        if (taskRepository.getAllByUserId(userId).isEmpty()) {
            throw new NoSuchElementException();
        }
        taskRepository.deleteAllByUserId(userId);
    }
}
