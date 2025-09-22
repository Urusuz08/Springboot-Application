package com.irtrains.train_service.controller.admin;

import com.irtrains.train_service.model.admin.Admin;
import com.irtrains.train_service.service.admin.AdminService;
import com.irtrains.train_service.repository.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class adminController {

    @Autowired
    private AdminService adminService;

    // Create new admin
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        try {
            Admin createdAdmin = adminService.createAdmin(admin);
            return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Create admin from OAuth (with correct parameters)
    @PostMapping("/oauth")
    public ResponseEntity<Admin> createAdminFromOAuth(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String picture,
            @RequestParam Role role) {
        try {
            Admin createdAdmin = adminService.createAdminFromOAuth(name, email, picture, role);
            return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Create multiple admins
    @PostMapping("/bulk")
    public ResponseEntity<List<Admin>> createMultipleAdmins(@RequestBody List<Admin> admins) {
        try {
            List<Admin> createdAdmins = adminService.createMultipleAdmins(admins);
            return new ResponseEntity<>(createdAdmins, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get admin by ID - using findById instead
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id) {
        try {
            Optional<Admin> admin = adminService.findByUsername(id);
            if (admin.isPresent()) {
                return new ResponseEntity<>(admin.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all admins
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            return new ResponseEntity<>(admins, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update admin
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String id, @RequestBody Admin admin) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(id, admin);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Update admin profile (with correct parameters)
    @PutMapping("/{id}/profile")
    public ResponseEntity<Admin> updateAdminProfile(
            @PathVariable String id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam Long phone) {
        try {
            Admin updatedAdmin = adminService.updateAdminProfile(id, name, email, phone);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete admin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String id) {
        try {
            adminService.deleteAdmin(id);
            return new ResponseEntity<>("Admin deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete admin", HttpStatus.BAD_REQUEST);
        }
    }

    // Delete multiple admins
    @DeleteMapping("/bulk")
    public ResponseEntity<String> deleteMultipleAdmins(@RequestBody List<String> adminIds) {
        try {
            adminService.deleteMultipleAdmins(adminIds);
            return new ResponseEntity<>("Admins deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete admins", HttpStatus.BAD_REQUEST);
        }
    }

    // Find admin by email
    @GetMapping("/search/email/{email}")
    public ResponseEntity<Admin> findByEmail(@PathVariable String email) {
        try {
            Optional<Admin> admin = adminService.findByEmail(email);
            if (admin.isPresent()) {
                return new ResponseEntity<>(admin.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if admin exists by email
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        try {
            boolean exists = adminService.emailExists(email);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if admin exists by username
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        try {
            boolean exists = adminService.adminExists(username);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if admin exists by phone
    @GetMapping("/exists/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(@PathVariable Long phone) {
        try {
            boolean exists = adminService.phoneExists(phone);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}