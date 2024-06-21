package com.ilyap.taskmanager.service;

import com.ilyap.taskmanager.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public User getUserById(Long userId) {
        return new User();
    }
}
