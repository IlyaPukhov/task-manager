package com.ilyap.userservice.service;

import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserReadDto> findAll(Pageable pageable);

    UserReadDto findOwnerByTaskId(Long taskId);

    UserReadDto findByUsername(String username);

    UserReadDto createUser(UserCreateUpdateDto userCreateUpdateDto);

    UserReadDto update(UserCreateUpdateDto taskCreateUpdateDto);

    void delete(String username);
}
