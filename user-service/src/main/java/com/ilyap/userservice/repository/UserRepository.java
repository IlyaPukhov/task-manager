/**
 * Repository for managing User entities.
 */
package com.ilyap.userservice.repository;

import com.ilyap.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Defines the repository layer for {@link User} entities.
 * Extends JpaRepository to inherit basic Jpa/CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} of {@link User} if found, or an {@link Optional#empty} if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Deletes a {@link User} by their username.
     *
     * @param username the username of the user to delete
     */
    void deleteByUsername(String username);
}
