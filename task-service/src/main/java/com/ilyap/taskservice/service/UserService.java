package com.ilyap.taskservice.service;

import com.ilyap.taskservice.model.dto.UserCreateUpdateDto;
import com.ilyap.taskservice.model.dto.UserReadDto;

import java.util.List;

public interface UserService {

    List<UserReadDto> getAllByTaskId(Long taskId);

    UserReadDto getUserByUsername(String username);

    UserReadDto registerUser(UserCreateUpdateDto userCreateUpdateDto);

    UserReadDto update(UserCreateUpdateDto taskCreateUpdateDto);

    void delete(String username);
}
