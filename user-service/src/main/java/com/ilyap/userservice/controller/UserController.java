package com.ilyap.userservice.controller;

import com.ilyap.userservice.controller.api.UserControllerApi;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.service.UserService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{username:\\w+}")
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;

    @GetMapping
    public UserReadDto findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PreAuthorize("#userCreateUpdateDto.username == principal.username")
    @PutMapping
    public ResponseEntity<?> update(@Validated @RequestBody UserCreateUpdateDto userCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto updatedUser = userService.update(userCreateUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("#username == principal.username")
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent()
                .build();
    }
}
