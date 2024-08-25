package com.ilyap.taskservice.controller;

import com.ilyap.taskservice.model.dto.PageResponse;
import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Validated @RequestBody TaskCreateUpdateDto taskCreateUpdateDto,
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

    @GetMapping("/user/{ownerUsername}")
    public PageResponse<TaskReadDto> findAllByUser(Pageable pageable,
                                                   @PathVariable String ownerUsername) {
        Page<TaskReadDto> page = taskService.findAllByUsername(ownerUsername, pageable);
        return PageResponse.of(page);
    }

    @PreAuthorize("#ownerUsername == principal.username")
    @DeleteMapping("/user/{ownerUsername}")
    public ResponseEntity<Void> deleteAllByUser(@PathVariable String ownerUsername) {
        taskService.deleteAllByUsername(ownerUsername);
        return ResponseEntity.noContent()
                .build();
    }
}
