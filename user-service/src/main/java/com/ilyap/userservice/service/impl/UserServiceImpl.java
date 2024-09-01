package com.ilyap.userservice.service.impl;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.userservice.client.TaskServiceClient;
import com.ilyap.userservice.exception.UserAlreadyExistsException;
import com.ilyap.userservice.exception.UserNotFoundException;
import com.ilyap.userservice.mapper.UserCreateUpdateMapper;
import com.ilyap.userservice.mapper.UserReadMapper;
import com.ilyap.userservice.model.dto.TaskResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.repository.UserRepository;
import com.ilyap.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * Provides methods for managing users.
 */
@Service
@Logged
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user-cache")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserReadMapper userReadMapper;
    private final UserCreateUpdateMapper userCreateUpdateMapper;
    private final UserRepository userRepository;
    private final TaskServiceClient taskServiceClient;

    /**
     * Finds all users, paginated by the provided pageable object.
     *
     * @param pageable the {@link Pageable} object to paginate the results
     * @return {@link Page} of {@link UserReadDto UserReadDtos}
     */
    @Override
    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userReadMapper::toDto);
    }

    /**
     * Finds the owner of a task by task ID.
     *
     * @param taskId the ID of the task to find the owner of
     * @return {@link UserReadDto} of the task owner
     */
    @Cacheable(key = "#taskId")
    @Override
    public UserReadDto findOwnerByTaskId(Long taskId) {
        TaskResponse task = taskServiceClient.findTaskByTaskId(taskId);
        return userRepository.findByUsername(task.ownerUsername())
                .map(userReadMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Finds a user by username.
     *
     * @param username the username to find the user by
     * @return {@link UserReadDto} of the found user
     */
    @Cacheable(key = "#username")
    @Override
    public UserReadDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Creates a new user.
     *
     * @param userCreateUpdateDto the {@link UserCreateUpdateDto} to create the user from
     * @return {@link UserReadDto} of the created user
     */
    @CachePut(key = "#userCreateUpdateDto.username")
    @Transactional
    @Override
    public UserReadDto create(UserCreateUpdateDto userCreateUpdateDto) {
        String username = userCreateUpdateDto.username();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(user.getUsername());
                });

        return Optional.of(userCreateUpdateDto)
                .map(userCreateUpdateMapper::toEntity)
                .map(userRepository::save)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User could not be created"));
    }

    /**
     * Updates an exising user.
     *
     * @param userCreateUpdateDto the {@link UserCreateUpdateDto} to update the user from
     * @return {@link UserReadDto} of the updated user
     */
    @CachePut(key = "#userCreateUpdateDto.username")
    @Transactional
    @Override
    public UserReadDto update(UserCreateUpdateDto userCreateUpdateDto) {
        String username = userCreateUpdateDto.username();
        return userRepository.findByUsername(username)
                .map(user -> userCreateUpdateMapper.toEntity(userCreateUpdateDto, user))
                .map(userRepository::save)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username to delete the user by
     */
    @CacheEvict(key = "#username")
    @Transactional
    @Override
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }
}
