package com.ilyap.taskservice.controller;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#taskId, principal.username)")
    @GetMapping
    public TaskReadDto findById(@PathVariable Long taskId) {
        return taskService.findTaskById(taskId);
    }

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#taskId, principal.username)")
    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long taskId,
                                    @Validated TaskCreateUpdateDto taskCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        TaskReadDto updatedTask = taskService.update(taskId, taskCreateUpdateDto);
        return ResponseEntity.ok(updatedTask);
    }

    @PreAuthorize("@taskPermissionHandler.isTaskOwner(#taskId, principal.username)")
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent()
                .build();
    }
}
