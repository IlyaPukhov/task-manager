package com.ilyap.taskmanager.service.impl;

import com.ilyap.taskmanager.model.entity.User;
import com.ilyap.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(Long userId) {
        return null;
    }
}
