package com.ilyap.taskservice.service;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskReadDto findTaskById(Long id);

    Page<TaskReadDto> findAll(Pageable pageable);

    Page<TaskReadDto> findAllByUsername(String username, Pageable pageable);

    TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto);

    TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto);

    void delete(Long id);

    void deleteAllByUsername(String username);
}
