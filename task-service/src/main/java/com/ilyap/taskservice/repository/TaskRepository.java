package com.ilyap.taskservice.repository;

import com.ilyap.taskservice.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByOwnerId(Long ownerId, Pageable pageable);

    void deleteAllByOwnerId(Long ownerId);
}
