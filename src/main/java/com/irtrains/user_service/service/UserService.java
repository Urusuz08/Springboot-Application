package com.irtrains.user_service.service;

import com.irtrains.user_service.config.user.SecurityConfig;
import com.irtrains.user_service.model.*;
import com.irtrains.user_service.repository.*;
import com.irtrains.user_service.DTO.*;
import com.irtrains.user_service.config.user.SecurityConfig;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional(readOnly = true)
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User create(UserDTO user){
        User usertemp=new User();
        usertemp.setName(user.getName());
        usertemp.setUsername(user.getUsername());
        usertemp.setEmail(user.getEmail());
        usertemp.setPhone(user.getPhone());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        usertemp.setPassword(encodedPassword);

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
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admintemp.setPassword(encodedPassword);

        return adminRepository.save(admintemp);
    }

    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Such User found."));

        existingUser.setName(userDTO.getName());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());

        return userRepository.save(existingUser);
    }


    public User checkUserCredentials(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user==null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        return user;
    }

    public Admin checkAdminCredentials(String username, String rawPassword) {
        Admin admin = adminRepository.findByUsername(username)
                .orElse(null);


        if (admin==null || !passwordEncoder.matches(rawPassword, admin.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        return admin;
    }
    @Transactional
    public User updateUserPassword(Long id, String newPassword) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such User found."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        return userRepository.save(existingUser);
    }

    @Transactional
    public Admin updateAdmin(Long id, AdminDTO adminDTO) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Such Admin found."));

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
            throw new RuntimeException("No Such User found.");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("No Such Admin found.");
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
