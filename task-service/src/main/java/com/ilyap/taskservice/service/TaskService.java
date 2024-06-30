package com.ilyap.taskservice.service;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface TaskService {

    TaskReadDto findTaskById(Long id);

    Page<TaskReadDto> findAll(UserDetails userDetails, Pageable pageable);

    Page<TaskReadDto> findAllByUserId(Long userId, Pageable pageable);

    TaskReadDto create(TaskCreateUpdateDto taskCreateUpdateDto);

    TaskReadDto update(Long id, TaskCreateUpdateDto taskCreateUpdateDto);

    void delete(Long id);

    void deleteAllByUserId(Long userId);
}
