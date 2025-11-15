package com.irtrains.user_service.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.*;
import com.irtrains.user_service.model.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users", key = "#username")
    Optional<User> findByUsername(String username);
//    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(Long phone);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(Long phone);
}
