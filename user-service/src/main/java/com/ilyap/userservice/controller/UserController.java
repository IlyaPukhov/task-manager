package com.ilyap.userservice.controller;

import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public PageResponse<UserReadDto> findAll(Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(pageable);
        return PageResponse.of(page);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid UserCreateUpdateDto userCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto userReadDto = userService.registerUser(userCreateUpdateDto);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{username}")
                                .build(userReadDto.getUsername())
                )
                .body(userReadDto);
    }

    @GetMapping("/{username:\\w+}")
    public UserReadDto findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("tasks/{taskId:\\d+}")
    public UserReadDto findByTaskId(@PathVariable Long taskId) {
        return userService.findOwnerByTaskId(taskId);
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
