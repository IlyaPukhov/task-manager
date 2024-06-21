package com.ilyap.taskmanager.repository;

import com.ilyap.taskmanager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
