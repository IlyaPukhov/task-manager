package com.ilyap.taskmanager.controller;

import com.ilyap.taskmanager.model.dto.UserCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.UserReadDto;
import com.ilyap.taskmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@Valid UserCreateUpdateDto userCreateUpdateDto,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto userReadDto = userService.registerUser(userCreateUpdateDto);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{username}")
                                .build(userReadDto.username())
                )
                .body(userReadDto);
    }
}
