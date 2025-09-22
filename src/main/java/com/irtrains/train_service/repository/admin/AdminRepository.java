package com.irtrains.train_service.repository.admin;

import com.irtrains.train_service.model.admin.Admin;
import com.irtrains.train_service.repository.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("admin")
public interface AdminRepository extends JpaRepository<Admin, Long>{
    // Define methods specific to admin operations if needed
    // For example, methods to manage users, view reports, etc.
    // This interface can extend UserRepository or define additional methods

    //Check if a user with a specific role exists
    boolean existsByRole(Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


    //To find user by role
    Optional<Admin> findByRole(Role role);

    // Add to UserRepository interface
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    List<Admin> findByNameContainingIgnoreCaseAndRole(String name, Role role);
    boolean existsByUsernameAndRole(String username, Role role);
    boolean existsByEmailAndRole(String email, Role role);
    long countByRole(Role role);
    List<Admin> findByEmailContaining(String emailDomain);
    List<Admin> findByPhoneIsNull();

    // Basic CRUD operations (extend JpaRepository)

    Optional<Admin> findByPhone(Long phone);
    List<Admin> findByNameContainingIgnoreCase(String name);

    // Existence checks

    boolean existsByPhone(Long phone);

    // Search operations
    List<Admin> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);


    // Ordering operations
    List<Admin> findAllByOrderByNameAsc();
    List<Admin> findTopNByOrderByCreatedAtDesc(int limit);

    long count();

    void delete(Admin admin);

    List<Admin> findAll();

    Admin save(Admin existingAdmin);
}
