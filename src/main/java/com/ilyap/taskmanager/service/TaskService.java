package com.ilyap.taskmanager.service;

import com.ilyap.taskmanager.mapper.TaskMapper;
import com.ilyap.taskmanager.model.dto.TaskDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.getAllByUserId(userId);
    }

    @Transactional
    public TaskDto create(Task task) {
        Task saved = taskRepository.save(task);
        return taskMapper.fromEntityToDto(saved);
    }

    @Transactional
    public Optional<TaskDto> update(Long id, Task task) {
        return taskRepository.findById(id)
                .map(t -> taskRepository.save(task))
                .map(taskMapper::fromEntityToDto);
    }

    @Transactional
    public boolean delete(Long id) {
        return taskRepository.findById(id)
                .map(entity -> {
                    taskRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean deleteAllByUserId(Long userId) {
        taskRepository.deleteAllByUserId(userId);
        return getAllByUserId(userId).isEmpty();
    }
}
