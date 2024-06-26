package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/users/{username:\\w+}")
@RequiredArgsConstructor
public class TasksUserController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskReadDto> getByUsername(@PathVariable String username) {
        return taskService.getAllByUsername(username);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        taskService.deleteAllByUsername(username);
        return ResponseEntity.noContent()
                .build();
    }
}
