package com.ilyap.taskservice.controller;

import com.ilyap.taskservice.model.dto.UserCreateUpdateDto;
import com.ilyap.taskservice.model.dto.UserReadDto;
import com.ilyap.taskservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

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

    @GetMapping("/{username:\\w+}")
    public UserReadDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("/{username:\\w+}")
    public ResponseEntity<?> update(@Valid UserCreateUpdateDto userCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto updatedUser = userService.update(userCreateUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username:\\w+}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent()
                .build();
    }
}
