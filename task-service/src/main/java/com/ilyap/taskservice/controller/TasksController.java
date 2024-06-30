package com.ilyap.taskservice.controller;

import com.ilyap.taskservice.model.dto.PageResponse;
import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskService taskService;

    @GetMapping
    public PageResponse<TaskReadDto> findAll(Pageable pageable,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Page<TaskReadDto> page = taskService.findAll(userDetails, pageable);
        return PageResponse.of(page);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid TaskCreateUpdateDto taskCreateUpdateDto,
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

    @GetMapping("/user/{ownerId:\\d+}")
    public PageResponse<TaskReadDto> findAllByUser(Pageable pageable,
                                                   @PathVariable Long ownerId) {
        Page<TaskReadDto> page = taskService.findAllByUserId(ownerId, pageable);
        return PageResponse.of(page);
    }

    @DeleteMapping("/user/{ownerId:\\d+}")
    public ResponseEntity<Void> deleteAllByUser(@PathVariable Long ownerId) {
        taskService.deleteAllByUserId(ownerId);
        return ResponseEntity.noContent()
                .build();
    }
}
