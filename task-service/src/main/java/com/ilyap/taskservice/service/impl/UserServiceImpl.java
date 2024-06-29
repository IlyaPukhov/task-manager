package com.ilyap.taskservice.service.impl;

import com.ilyap.commonloggingstarter.annotation.Logged;
import com.ilyap.taskservice.exception.UserAlreadyExistsException;
import com.ilyap.taskservice.mapper.UserCreateUpdateMapper;
import com.ilyap.taskservice.mapper.UserReadMapper;
import com.ilyap.taskservice.model.dto.UserCreateUpdateDto;
import com.ilyap.taskservice.model.dto.UserReadDto;
import com.ilyap.taskservice.repository.UserRepository;
import com.ilyap.taskservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Logged
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserReadMapper userReadMapper;
    private final UserCreateUpdateMapper userCreateUpdateMapper;
    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @Override
    public Page<UserReadDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userReadMapper::map);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public Page<UserReadDto> findAllByTaskId(Long taskId, Pageable pageable) {
        return userRepository.findAllByTaskId(taskId, pageable)
                .map(userReadMapper::map);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public UserReadDto findUserByUsername(String username) {
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
