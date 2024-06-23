package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks/{taskId:\\d+}")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public TaskReadDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long taskId,
                                    @Valid TaskCreateUpdateDto taskCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        TaskReadDto updatedTask = taskService.update(taskId, taskCreateUpdateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent()
                .build();
    }
}
