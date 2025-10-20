package com.irtrains.user_service.controller;


import com.irtrains.user_service.model.User;
import org.springframework.web.bind.annotation.*;

import com.irtrains.user_service.model.*;
import com.irtrains.user_service.DTO.*;
import com.irtrains.user_service.service.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody UserDTO user) {
        return userService.create(user);
    }

    @PostMapping("/admin")
    public Admin createAdmin(@RequestBody AdminDTO admin) {
        return userService.createAdmin(admin);
    }

    @PutMapping("/{userid}")
    public User updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @PutMapping("/admin/{adminid}")
    public Admin updateAdmin(@PathVariable Long id, @RequestBody AdminDTO adminDTO) {
        return userService.updateAdmin(id, adminDTO);
    }

    @GetMapping("/admin/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        return userService.getAdminById(id);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/admin/username/{username}")
    public Admin getAdminByUsername(@PathVariable String username) {
        return userService.getAdminByUsername(username);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/admin/email/{email}")
    public Admin getAdminByEmail(@PathVariable String email) {
        return userService.getAdminByEmail(email);
    }

    @GetMapping("/phone/{phone}")
    public User getUserByPhone(@PathVariable Long phone) {
        return userService.getUserByPhone(phone);
    }

    @GetMapping("/admin/phone/{phone}")
    public Admin getAdminByPhone(@PathVariable Long phone) {
        return userService.getAdminByPhone(phone);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        userService.deleteAdmin(id);
    }



}
