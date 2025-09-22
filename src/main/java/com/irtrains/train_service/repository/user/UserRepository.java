package com.irtrains.train_service.repository.user;

import com.irtrains.train_service.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find user by phone number
    Optional<User> findByPhone(Long phone);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find users by name (partial match)
    List<User> findByNameContainingIgnoreCase(String name);

    // Find user by Username
    Optional<User> findByUsername(String username);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if phone exists
    boolean existsByPhone(Long phone);

    // Check if email exists
    boolean existsByEmail(String email);

    // Custom query to find user by username or email
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    // Delete user by phone
    void deleteByPhone(Long phone);

    // Delete user by email
    void deleteByEmail(String email);
}