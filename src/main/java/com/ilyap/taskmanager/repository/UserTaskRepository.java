package com.ilyap.taskmanager.repository;

import com.ilyap.taskmanager.model.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {

    Optional<UserTask> findByTaskIdAndUserUsername(Long taskId, String username);

    void deleteByTaskIdAndUserUsername(Long taskId, String username);
}
