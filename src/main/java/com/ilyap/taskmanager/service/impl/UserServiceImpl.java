package com.ilyap.taskmanager.service.impl;

import com.ilyap.taskmanager.exception.UserAlreadyExistsException;
import com.ilyap.taskmanager.mapper.UserCreateUpdateMapper;
import com.ilyap.taskmanager.mapper.UserReadMapper;
import com.ilyap.taskmanager.model.dto.UserCreateUpdateDto;
import com.ilyap.taskmanager.model.dto.UserReadDto;
import com.ilyap.taskmanager.repository.UserRepository;
import com.ilyap.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserReadMapper userReadMapper;
    private final UserCreateUpdateMapper userCreateUpdateMapper;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @Override
    public List<UserReadDto> getAllByTaskId(Long taskId) {
        return userRepository.getAllByTaskId(taskId).stream()
                .map(userReadMapper::map)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public UserReadDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userReadMapper::map)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    @Transactional
    @Override
    public UserReadDto registerUser(UserCreateUpdateDto userCreateUpdateDto) {
        String username = userCreateUpdateDto.getUsername();
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException(user.getUsername());
                });

        return Optional.of(userCreateUpdateDto)
                .map(userCreateUpdateMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow(() -> new RuntimeException("User could not be registered"));
    }

    @PreAuthorize("#userCreateUpdateDto.username == principal.username")
    @Transactional
    @Override
    public UserReadDto update(UserCreateUpdateDto userCreateUpdateDto) {
        String username = userCreateUpdateDto.getUsername();
        return userRepository.findByUsername(username)
                .map(user -> userCreateUpdateMapper.map(userCreateUpdateDto, user))
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    @PreAuthorize("#username == principal.username")
    @Transactional
    @Override
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
