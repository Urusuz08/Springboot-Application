package com.irtrains.user_service.service;

import com.irtrains.user_service.config.user.SecurityConfig;
import com.irtrains.user_service.model.*;
import com.irtrains.user_service.repository.*;
import com.irtrains.user_service.DTO.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;


    public UserService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public User create(UserDTO user){
        User usertemp=new User();
        usertemp.setName(user.getName());
        usertemp.setUsername(user.getUsername());
        usertemp.setEmail(user.getEmail());
        usertemp.setPhone(user.getPhone());

//        User savedUser=userRepository.save(usertemp);

        return userRepository.save(usertemp);
    }

    @Transactional
    public Admin createAdmin(AdminDTO admin) {
        Admin admintemp = new Admin();
        admintemp.setName(admin.getName());
        admintemp.setUsername(admin.getUsername());
        admintemp.setEmail(admin.getEmail());
        admintemp.setPhone(admin.getPhone());
        admintemp.setRole(Role.valueOf(admin.getRole().toUpperCase()));

        return adminRepository.save(admintemp);
    }

    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(userDTO.getName());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());

        return userRepository.save(existingUser);
    }

    @Transactional
    public Admin updateAdmin(Long id, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        existingAdmin.setName(adminDTO.getName());
        existingAdmin.setUsername(adminDTO.getUsername());
        existingAdmin.setEmail(adminDTO.getEmail());
        existingAdmin.setPhone(adminDTO.getPhone());
        existingAdmin.setRole(Role.valueOf(adminDTO.getRole().toUpperCase()));

        return adminRepository.save(existingAdmin);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    public User getUserById(Long Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + Id));
    }

    public Admin getAdminById(Long Id) {
        return adminRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + Id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found with username: " + username));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));
    }

    public User getUserByPhone(Long phone) {
        return userRepository.findByPhone(phone)
            .orElseThrow(() -> new RuntimeException("User not found with phone: " + phone));
    }

    public Admin getAdminByPhone(Long phone) {
        return adminRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Admin not found with phone: " + phone));
    }



}
