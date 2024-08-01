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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Logged
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserReadMapper userReadMapper;
    private final UserCreateUpdateMapper userCreateUpdateMapper;
    private final UserRepository userRepository;
    private final TaskServiceClient taskServiceClient;

    @Override
    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userReadMapper::toDto);
    }

    @Override
    public UserReadDto findOwnerByTaskId(Long taskId) {
        TaskResponse task = taskServiceClient.findTaskByTaskId(taskId);
        return userRepository.findByUsername(task.ownerUsername())
                .map(userReadMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserReadDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(username)));
    }

    @Transactional
    @Override
    public UserReadDto createUser(UserCreateUpdateDto userCreateUpdateDto) {
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

    @Transactional
    @Override
    public UserReadDto update(UserCreateUpdateDto userCreateUpdateDto) {
        String username = userCreateUpdateDto.username();
        return userRepository.findByUsername(username)
                .map(user -> userCreateUpdateMapper.toEntity(userCreateUpdateDto, user))
                .map(userRepository::save)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User %s not found".formatted(username)));
    }

    @Transactional
    @Override
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }
}
