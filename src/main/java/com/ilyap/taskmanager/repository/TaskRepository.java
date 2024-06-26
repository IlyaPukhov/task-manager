package com.ilyap.taskmanager.repository;

import com.ilyap.taskmanager.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t " +
            "from Task t " +
            "join UserTask ut " +
            "where ut.user.username = :username")
    List<Task> getAllByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("delete from Task t " +
            "where t.id in " +
            "(select t1.id from Task t1 " +
            "join UserTask ut " +
            "where ut.user.username = :username)")
    void deleteAllByUsername(String username);
}
