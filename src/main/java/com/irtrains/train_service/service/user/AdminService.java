package com.irtrains.train_service.service.user;

import com.irtrains.train_service.model.user.Admin;
import com.irtrains.train_service.repository.enums.Role;
import com.irtrains.train_service.repository.admin.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class AdminService {

    @Autowired
    private admin adminRepository;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Phone validation pattern (10 digits)
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[6-9]\\d{9}$");

    // CREATE ADMIN
    public Admin createAdmin(Admin admin) throws Exception {
        // Validate admin data
        validateAdminForCreation(admin);

        // Set role to ADMIN
//        admin.setRole(role);

        // Save admin
        return adminRepository.save(admin);
    }

    // CREATE ADMIN FROM OAUTH
    public Admin createAdminFromOAuth(String email, String name, String provider,Role role) throws Exception {
        // Check if admin already exists
        if (adminRepository.existsByEmail(email)) {
            throw new Exception("Admin already exists with this email");
        }

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setName(name);
        admin.setUsername(generateUsernameFromEmail(email));
        admin.setRole(role);
        // Add OAuth provider info if needed
        //admin.setOAuthProvider(provider);

        return adminRepository.save(admin);
    }

    // UPDATE ADMIN
    public Admin updateAdmin(String username, Admin updatedAdmin) throws Exception {
        Admin existingAdmin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("Admin not found with username: " + username));

        validateAdminForUpdate(updatedAdmin, existingAdmin.getUsername());

        // Update fields
        if (updatedAdmin.getName() != null) {
            existingAdmin.setName(updatedAdmin.getName());
        }
        if (updatedAdmin.getEmail() != null) {
            existingAdmin.setEmail(updatedAdmin.getEmail());
        }
        if (updatedAdmin.getPhone() != null) {
            existingAdmin.setPhone(updatedAdmin.getPhone());
        }

        // Role remains ADMIN
        existingAdmin.setRole(existingAdmin.getRole());

        return adminRepository.save(existingAdmin);
    }

    // VALIDATION METHODS (OAuth-focused)
    private void validateAdminForCreation(Admin admin) throws Exception {
        // Check required fields for OAuth admins
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new Exception("Email is required");
        }
        if (admin.getName() == null || admin.getName().trim().isEmpty()) {
            throw new Exception("Name is required");
        }

        // Validate email format
        if (!isValidEmail(admin.getEmail())) {
            throw new Exception("Invalid email format");
        }

        // Validate phone if provided
        if (admin.getPhone() != null && !isValidPhone(admin.getPhone())) {
            throw new Exception("Invalid phone number. Must be 10 digits starting with 6-9");
        }

        // Check email uniqueness
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new Exception("Email already registered");
        }

        // Check username uniqueness if provided
        if (admin.getUsername() != null && adminRepository.existsByUsername(admin.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Check phone uniqueness if provided
        if (admin.getPhone() != null && adminRepository.existsByPhone(admin.getPhone())) {
            throw new Exception("Phone number already registered");
        }
    }

    private void validateAdminForUpdate(Admin updatedAdmin, String currentUsername) throws Exception {
        // Validate email if changed
        if (updatedAdmin.getEmail() != null && !updatedAdmin.getEmail().trim().isEmpty()) {
            if (!isValidEmail(updatedAdmin.getEmail())) {
                throw new Exception("Invalid email format");
            }
            Optional<Admin> existingAdminWithEmail = adminRepository.findByEmail(updatedAdmin.getEmail());
            if (existingAdminWithEmail.isPresent() &&
                !existingAdminWithEmail.get().getUsername().equals(currentUsername)) {
                throw new Exception("Email already registered to another admin");
            }
        }

        // Validate phone if changed
        if (updatedAdmin.getPhone() != null) {
            if (!isValidPhone(updatedAdmin.getPhone())) {
                throw new Exception("Invalid phone number. Must be 10 digits starting with 6-9");
            }
            Optional<Admin> existingAdminWithPhone = adminRepository.findByPhone(updatedAdmin.getPhone());
            if (existingAdminWithPhone.isPresent() &&
                !existingAdminWithPhone.get().getUsername().equals(currentUsername)) {
                throw new Exception("Phone number already registered to another admin");
            }
        }

        // Validate name
        if (updatedAdmin.getName() != null && updatedAdmin.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be empty");
        }
    }

    // VALIDATION HELPER METHODS
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(Long phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.toString()).matches();
    }

    // UTILITY METHODS
    private String generateUsernameFromEmail(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        // Ensure username is unique
        while (adminRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    // ADMIN RETRIEVAL METHODS
    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Optional<Admin> findByPhone(Long phone) {
        return adminRepository.findByPhone(phone);
    }

    public List<Admin> searchByName(String name) {
        return adminRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // OAUTH AUTHENTICATION
    public Admin findOrCreateAdminFromOAuth(String email, String name, String provider, Role role) throws Exception {
        Optional<Admin> existingAdmin = adminRepository.findByEmail(email);

        if (existingAdmin.isPresent()) {
            return existingAdmin.get();
        } else {
            return createAdminFromOAuth(email, name, provider, role);
        }
    }

    // DELETE ADMIN
    public void deleteAdmin(String username) throws Exception {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("Admin not found"));

        adminRepository.delete(admin);
    }

    // CHECK ADMIN EXISTENCE
    public boolean adminExists(String username) {
        return adminRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    public boolean phoneExists(Long phone) {
        return adminRepository.existsByPhone(phone);
    }

    // ADMIN STATISTICS
    public long getTotalAdminCount() {
        return adminRepository.count();
    }

    // ADMIN SEARCH OPERATIONS
    public List<Admin> searchAdminsByNameOrEmail(String searchTerm) {
        return adminRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm);
    }

    public List<Admin> getAdminsByEmailDomain(String domain) {
        return adminRepository.findByEmailContaining("@" + domain);
    }

    public List<Admin> getAdminsWithoutPhone() {
        return adminRepository.findByPhoneIsNull();
    }

    // BULK OPERATIONS
    public List<Admin> createMultipleAdmins(List<Admin> admins) throws Exception {
        List<Admin> createdAdmins = new java.util.ArrayList<>();

        for (Admin admin : admins) {
            try {
                Admin createdAdmin = createAdmin(admin);
                createdAdmins.add(createdAdmin);
            } catch (Exception e) {
                // Log error but continue with other admins
                System.err.println("Failed to create admin: " + admin.getEmail() + " - " + e.getMessage());
            }
        }

        return createdAdmins;
    }

    public void deleteMultipleAdmins(List<String> usernames) {
        for (String username : usernames) {
            try {
                deleteAdmin(username);
            } catch (Exception e) {
                // Log error but continue with other admins
                System.err.println("Failed to delete admin: " + username + " - " + e.getMessage());
            }
        }
    }

    // ADMIN PROFILE MANAGEMENT
    public Admin updateAdminProfile(String username, String name, String email, Long phone) throws Exception {
        Admin admin = adminRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("Admin not found"));

        if (name != null && !name.trim().isEmpty()) {
            admin.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            if (!isValidEmail(email)) {
                throw new Exception("Invalid email format");
            }
            // Check if email exists for other admins
            Optional<Admin> existingAdmin = adminRepository.findByEmail(email);
            if (existingAdmin.isPresent() && !existingAdmin.get().getUsername().equals(username)) {
                throw new Exception("Email already registered to another admin");
            }
            admin.setEmail(email);
        }
        if (phone != null) {
            if (!isValidPhone(phone)) {
                throw new Exception("Invalid phone number");
            }
            admin.setPhone(phone);
        }

        return adminRepository.save(admin);
    }

    // ADMIN REPORTING
    public List<Admin> getRecentlyCreatedAdmins(int limit) {
        return adminRepository.findTopNByOrderByCreatedAtDesc(limit);
    }

    public List<Admin> getAdminsOrderedByName() {
        return adminRepository.findAllByOrderByNameAsc();
    }
}