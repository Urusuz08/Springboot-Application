package com.irtrains.train_service.service.user;

import com.irtrains.train_service.model.user.User;
import com.irtrains.train_service.repository.enums.Role;
import com.irtrains.train_service.repository.user.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    @Qualifier("user")
    @Autowired
    private user userRepository; // Fixed: Use interface name, not lowercase

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Phone validation pattern (10 digits)
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[6-9]\\d{9}$");

    // CREATE USER (OAuth-based - no password)
    public User createUser(User user) throws Exception {
        // Validate user data
        validateUserForCreation(user);

        // Set default role if not provided
//        if (user.getRole() == null) {
//            user.setRole(Role.USER);
//        }

        // No password encoding needed for OAuth users
        return userRepository.save(user);
    }

    // CREATE USER FROM OAUTH PROVIDER
    public User createUserFromOAuth(String email, String name, String provider) throws Exception {
        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new Exception("User already exists with this email");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(generateUsernameFromEmail(email));
//        user.setRole(Role.USER);
        // Add OAuth provider info if needed
        // user.setOAuthProvider(provider);

        return userRepository.save(user);
    }

    // UPDATE USER
    public User updateUser(String username, User updatedUser) throws Exception {
        User existingUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("User not found with username: " + username));

        validateUserForUpdate(updatedUser, existingUser.getUsername());

        // Update fields
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }

        // Role changes (admin only)
//        if (updatedUser.getRole() != null && !updatedUser.getRole().equals(existingUser.getRole())) {
//            existingUser.setRole(updatedUser.getRole());
//        }

        return userRepository.save(existingUser);
    }

    // VALIDATION METHODS (OAuth-focused)
    private void validateUserForCreation(User user) throws Exception {
        // Check required fields for OAuth users
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new Exception("Email is required");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new Exception("Name is required");
        }

        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            throw new Exception("Invalid email format");
        }

        // Validate phone if provided
        if (user.getPhone() != null && !isValidPhone(user.getPhone())) {
            throw new Exception("Invalid phone number. Must be 10 digits starting with 6-9");
        }

        // Check email uniqueness
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already registered");
        }

        // Check username uniqueness if provided
        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Check phone uniqueness if provided
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new Exception("Phone number already registered");
        }
    }

    private void validateUserForUpdate(User updatedUser, String currentUsername) throws Exception {
        // Validate email if changed
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
            if (!isValidEmail(updatedUser.getEmail())) {
                throw new Exception("Invalid email format");
            }
            Optional<User> existingUserWithEmail = userRepository.findByEmail(updatedUser.getEmail());
            if (existingUserWithEmail.isPresent() &&
                !existingUserWithEmail.get().getUsername().equals(currentUsername)) {
                throw new Exception("Email already registered to another user");
            }
        }

        // Validate phone if changed
        if (updatedUser.getPhone() != null) {
            if (!isValidPhone(updatedUser.getPhone())) {
                throw new Exception("Invalid phone number. Must be 10 digits starting with 6-9");
            }
            Optional<User> existingUserWithPhone = userRepository.findByPhone(updatedUser.getPhone());
            if (existingUserWithPhone.isPresent() &&
                !existingUserWithPhone.get().getUsername().equals(currentUsername)) {
                throw new Exception("Phone number already registered to another user");
            }
        }

        // Validate name
        if (updatedUser.getName() != null && updatedUser.getName().trim().isEmpty()) {
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
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    // USER RETRIEVAL METHODS
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(Long phone) {
        return userRepository.findByPhone(phone);
    }

//    public List<User> findByRole(Role role) {
//        return userRepository.findByRole(role);
//    }

    public List<User> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // OAUTH AUTHENTICATION
    public User findOrCreateUserFromOAuth(String email, String name, String provider) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            return createUserFromOAuth(email, name, provider);
        }
    }

    // DELETE USER
    public void deleteUser(String username) throws Exception {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new Exception("User not found"));

        userRepository.delete(user);
    }

    // CHECK USER EXISTENCE
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean phoneExists(Long phone) {
        return userRepository.existsByPhone(phone);
    }
}