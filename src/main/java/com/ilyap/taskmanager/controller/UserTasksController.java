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
@RequestMapping("/api/v1/tasks/user/{userId:\\d+}")
@RequiredArgsConstructor
public class UserTasksController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskReadDto> getByUserId(@PathVariable Long userId) {
        return taskService.getAllByUserId(userId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
        taskService.deleteAllByUserId(userId);
        return ResponseEntity.noContent()
                .build();
    }
}
