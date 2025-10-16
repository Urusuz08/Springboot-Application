package com.irtrains.user_service.repository;

import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.*;
import com.irtrains.user_service.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(Long phone);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(Long phone);
}
