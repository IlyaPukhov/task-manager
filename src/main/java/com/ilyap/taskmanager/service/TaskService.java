package com.ilyap.taskmanager.service;

import com.ilyap.taskmanager.dto.TaskCreateDto;
import com.ilyap.taskmanager.exception.TaskNotFoundException;
import com.ilyap.taskmanager.model.Task;
import com.ilyap.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAllTasks() {
        return this.taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public boolean delete(Long id) {
        return taskRepository.findById(id)
                .map(entity -> {
                    taskRepository.delete(entity);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Task save(TaskCreateDto taskCreateDto) {
        // TODO: 19.06.2024
        return new Task();
    }
}
