package com.irtrains.user_service.service;

import com.irtrains.user_service.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.irtrains.user_service.repository.*;
import com.irtrains.user_service.DTO.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@Transactional(readOnly = true)
@Qualifier("adminService")
public class AuthenticationService implements UserDetailsService {

        private AdminRepository adminRepository;


        public AuthenticationService(AdminRepository adminRepository) {
            this.adminRepository = adminRepository;

        }


    @Override
    public UserDetails loadUserByUsername(String username){
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles(admin.getRole().getRoleName().toUpperCase())
                .build();
    }
}
