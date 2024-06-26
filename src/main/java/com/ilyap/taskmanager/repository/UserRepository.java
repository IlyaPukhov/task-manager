package com.ilyap.taskmanager.repository;

import com.ilyap.taskmanager.model.entity.TaskManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<TaskManagerUser, Long> {

    Optional<TaskManagerUser> findByUsername(String username);

    void deleteByUsername(String username);

    @Query("select u " +
            "from TaskManagerUser u " +
            "join UserTask ut " +
            "where ut.task.id = :taskId")
    List<TaskManagerUser> getAllByTaskId(Long taskId);
}
