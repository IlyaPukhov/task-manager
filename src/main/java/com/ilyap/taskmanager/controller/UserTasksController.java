package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.UserReadDto;
import com.ilyap.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId:\\d+}/users")
@RequiredArgsConstructor
public class UserTasksController {

    private final UserService userService;
//    private final UserTaskService userTaskService;

    @GetMapping
    public List<UserReadDto> getByTaskId(@PathVariable Long taskId) {
        return userService.getAllByTaskId(taskId);
    }

    //todo добавить к таске и открепить от таски
}
