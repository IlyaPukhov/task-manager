package com.ilyap.taskservice.service;

import com.ilyap.taskservice.model.dto.UserCreateUpdateDto;
import com.ilyap.taskservice.model.dto.UserReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserReadDto> findAllByTaskId(Long taskId, Pageable pageable);

    UserReadDto findUserByUsername(String username);

    UserReadDto registerUser(UserCreateUpdateDto userCreateUpdateDto);

    UserReadDto update(UserCreateUpdateDto taskCreateUpdateDto);

    void delete(String username);
}
