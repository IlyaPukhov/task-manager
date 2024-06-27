package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.model.dto.UserReadDto;
import com.ilyap.taskmanager.service.TaskService;
import com.ilyap.taskmanager.service.UserService;
import com.ilyap.taskmanager.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class UserTaskController {

    private final UserService userService;
    private final UserTaskService userTaskService;
    private final TaskService taskService;

    @GetMapping("/users/{username:\\w+}")
    public List<TaskReadDto> getByUsername(@PathVariable String username) {
        return taskService.getAllByUsername(username);
    }

    @DeleteMapping("/users/{username:\\w+}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        taskService.deleteAllByUsername(username);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{taskId:\\d+}/users")
    public List<UserReadDto> getByTaskId(@PathVariable Long taskId) {
        return userService.getAllByTaskId(taskId);
    }

    @PostMapping("/{taskId:\\d+}/users")
    public ResponseEntity<Void> addTaskUser(@PathVariable Long taskId, @RequestBody String username) {
        userTaskService.addTaskUser(taskId, username);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{taskId:\\d+}/users")
    public ResponseEntity<Void> deleteTaskUser(@PathVariable Long taskId, @RequestBody String username) {
        userTaskService.deleteTaskUser(taskId, username);
        return ResponseEntity.noContent()
                .build();
    }
}
