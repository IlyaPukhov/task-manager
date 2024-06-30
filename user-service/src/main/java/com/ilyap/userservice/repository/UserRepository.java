package com.ilyap.userservice.repository;

import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TaskManagerUser, Long> {

    Optional<TaskManagerUser> findByUsername(String username);

    void deleteByUsername(String username);
}
