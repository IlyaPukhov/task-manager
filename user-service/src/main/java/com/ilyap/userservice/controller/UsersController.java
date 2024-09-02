package com.ilyap.userservice.controller;

import com.ilyap.userservice.controller.api.UsersControllerApi;
import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController implements UsersControllerApi {

    private final UserService userService;

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageable pagination information
     * @return a {@link PageResponse} of {@link UserReadDto UserReadDtos}
     */
    @GetMapping
    public PageResponse<UserReadDto> findAll(Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(pageable);
        return PageResponse.of(page);
    }

    /**
     * Registers a new user.
     *
     * @param userCreateUpdateDto user data to register
     * @param bindingResult validation result
     * @return {@link ResponseEntity} of registered {@link UserReadDto}
     * @throws BindException if validation fails
     */
    @PostMapping("/registration")
    public ResponseEntity<?> register(@Validated @RequestBody UserCreateUpdateDto userCreateUpdateDto,
                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        UserReadDto userReadDto = userService.create(userCreateUpdateDto);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .replacePath("/api/v1/users")
                                .path("/{username}")
                                .build(userReadDto.getUsername())
                )
                .body(userReadDto);
    }

    /**
     * Retrieves the user who owns a task with the given ID.
     *
     * @param taskId task ID
     * @return the user who owns the task
     */
    @GetMapping("/tasks/{taskId:\\d+}")
    public UserReadDto findByTaskId(@PathVariable Long taskId) {
        return userService.findOwnerByTaskId(taskId);
    }
}
