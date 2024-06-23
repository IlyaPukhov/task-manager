package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskReadDto> getAllTasks() {
        return taskService.getAll();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@Valid TaskCreateUpdateDto taskCreateUpdateDto,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        TaskReadDto taskReadDto = taskService.create(taskCreateUpdateDto);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{taskId}")
                                .build(taskReadDto.id())
                )
                .body(taskReadDto);
    }
}
