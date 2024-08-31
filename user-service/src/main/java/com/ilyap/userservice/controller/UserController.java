package com.ilyap.userservice.controller;

import com.ilyap.userservice.controller.api.UserControllerApi;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

/**
 * Controller for managing user data.
 * <p>
 * This controller provides endpoints for retrieving, updating, and deleting user data.
 */
@RestController
@RequestMapping("/api/v1/users/{username:\\w+}")
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return found {@link UserReadDto}
     */
    @GetMapping
    public UserReadDto findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    /**
     * Updates a user's data.
     * <p>
     * Only the user themselves can update their own data.
     *
     * @param userCreateUpdateDto the updated user data
     * @param bindingResult the binding result
     * @return {@link ResponseEntity} of updated {@link UserReadDto}
     * @throws BindException if the binding result has errors
     */
    @PreAuthorize("#userCreateUpdateDto.username == authentication.name")
    @PutMapping
    public ResponseEntity<?> update(@Validated @RequestBody UserCreateUpdateDto userCreateUpdateDto,
                                    BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto updatedUser = userService.update(userCreateUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user's data.
     * <p>
     * Only the user themselves can delete their own data.
     *
     * @param username the username of the user to delete
     * @return {@link ResponseEntity} indicating that the user was deleted ({@link HttpStatus#NO_CONTENT NO_CONTENT})
     */
    @PreAuthorize("#username == authentication.name")
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent()
                .build();
    }
}
