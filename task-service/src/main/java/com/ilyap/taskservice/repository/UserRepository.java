package com.ilyap.taskservice.repository;

import com.ilyap.taskservice.model.entity.TaskManagerUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TaskManagerUser, Long> {

    @Query("select u " +
            "from TaskManagerUser u " +
            "join UserTask ut " +
            "where ut.task.id = :taskId")
    Page<TaskManagerUser> findAllByTaskId(Long taskId, Pageable pageable);

    Optional<TaskManagerUser> findByUsername(String username);

    void deleteByUsername(String username);
}
