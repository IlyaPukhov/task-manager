package com.ilyap.taskservice.repository;

import com.ilyap.taskservice.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t " +
            "from Task t " +
            "join UserTask ut " +
            "where ut.user.username = :username")
    Page<Task> findAllByUsername(String username, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("delete from Task t " +
            "where t.id in " +
            "(select t1.id from Task t1 " +
            "join UserTask ut " +
            "where ut.user.username = :username)")
    void deleteAllByUsername(String username);
}
