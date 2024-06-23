package com.ilyap.taskmanager.service;

import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.TaskReadDto;

import java.util.List;

public interface TaskService {

    TaskReadDto getTaskById(Long id);

    List<TaskReadDto> getAll();

    List<TaskReadDto> getAllByUserId(Long userId);

    TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto);

    TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto);

    void delete(Long id);

    void deleteAllByUserId(Long userId);
}
