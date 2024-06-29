package com.ilyap.taskservice.controller;

import com.ilyap.taskservice.model.dto.PageResponse;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.model.dto.UserReadDto;
import com.ilyap.taskservice.service.TaskService;
import com.ilyap.taskservice.service.UserService;
import com.ilyap.taskservice.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class UserTaskController {

    private final UserService userService;
    private final UserTaskService userTaskService;
    private final TaskService taskService;

    @GetMapping("/users/{username:\\w+}")
    public PageResponse<TaskReadDto> findByUsername(@PathVariable String username,
                                                    Pageable pageable) {
        Page<TaskReadDto> page = taskService.findAllByUsername(username, pageable);
        return PageResponse.of(page);
    }

    @DeleteMapping("/users/{username:\\w+}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        taskService.deleteAllByUsername(username);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{taskId:\\d+}/users")
    public PageResponse<UserReadDto> findByTaskId(@PathVariable Long taskId,
                                                  Pageable pageable) {
        Page<UserReadDto> page = userService.findAllByTaskId(taskId, pageable);
        return PageResponse.of(page);
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
