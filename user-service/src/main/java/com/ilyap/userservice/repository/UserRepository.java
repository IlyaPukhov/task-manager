/**
 * Repository for managing TaskManagerUser entities.
 */
package com.ilyap.userservice.repository;

import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Defines the data access object (DAO) for TaskManagerUser entities.
 * Extends JpaRepository to inherit basic CRUD operations.
 */
public interface UserRepository extends JpaRepository<TaskManagerUser, Long> {

    /**
     * Finds a TaskManagerUser by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the TaskManagerUser if found, or an empty Optional if not found
     */
    Optional<TaskManagerUser> findByUsername(String username);

    /**
     * Deletes a TaskManagerUser by their username.
     *
     * @param username the username of the user to delete
     */
    void deleteByUsername(String username);
}
