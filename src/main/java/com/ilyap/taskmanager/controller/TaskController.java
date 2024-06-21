package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.mapper.TaskMapper;
import com.ilyap.taskmanager.model.dto.TaskDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<Task> getByUserId(@PathVariable Long userId) {
        return taskService.getAllByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.of(taskService.getTaskById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(TaskDto taskDto) {
        return taskService.create(taskMapper.fromDtoToEntity(taskDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    TaskDto taskDto) {
        Optional<TaskDto> updatedTask = taskService.update(id, taskMapper.fromDtoToEntity((taskDto)));
        return ResponseEntity.of(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return taskService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteByUserId(@PathVariable Long userId) {
        return taskService.deleteAllByUserId(userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
