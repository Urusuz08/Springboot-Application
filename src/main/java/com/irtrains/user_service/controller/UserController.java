package com.irtrains.user_service.controller;


import com.irtrains.user_service.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.irtrains.user_service.model.*;
import com.irtrains.user_service.DTO.*;
import com.irtrains.user_service.service.*;


@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins= {"http://localhost:3000"})
public class UserController {
    private final UserService userService;;
    private final JWTUtility jwtUtility;
    private final AuthenticationService AuthenticationService;

    public UserController (UserService userService, JWTUtility jwtUtility, AuthenticationService AuthenticationService) {
        this.userService = userService;
        this.jwtUtility = jwtUtility;
        this.AuthenticationService = AuthenticationService;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/user/register")
    public User createUser(@RequestBody UserDTO user) {
        return userService.create(user);
    }

    @PostMapping("/admin/register")
    public Admin createAdmin(@RequestBody AdminDTO admin) {
        return userService.createAdmin(admin);
    }

    @PutMapping("/user/{userid}")
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

    @GetMapping("/user/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/admin/username/{username}")
    public Admin getAdminByUsername(@PathVariable String username) {
        return userService.getAdminByUsername(username);
    }

    @GetMapping("/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/admin/email/{email}")
    public Admin getAdminByEmail(@PathVariable String email) {
        return userService.getAdminByEmail(email);
    }

    @GetMapping("/user/phone/{phone}")
    public User getUserByPhone(@PathVariable Long phone) {
        return userService.getUserByPhone(phone);
    }

    @GetMapping("/admin/phone/{phone}")
    public Admin getAdminByPhone(@PathVariable Long phone) {
        return userService.getAdminByPhone(phone);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        userService.deleteAdmin(id);
    }

    @PostMapping("/user/login")
    public JWTResponse loginUser(@RequestBody LoginDTO loginDTO) {
        if(loginDTO.getType() != Role.USER) {
            throw new RuntimeException("Invalid Credentials");
        }
        LoginDTO authenticatedUser = userService.authenticate(loginDTO);

        if(!authenticatedUser.getStatus()) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtility.generateToken(loginDTO);

        return new JWTResponse(token);
    }

    @PostMapping("/admin/login")
    public JWTResponse loginAdmin(@RequestBody LoginDTO loginDTO) {
        if(loginDTO.getType() == Role.USER) {
            throw new RuntimeException("Invalid Credentials");
        }
        LoginDTO authenticatedUser = userService.authenticate(loginDTO);

        if(!authenticatedUser.getStatus()) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtility.generateToken(loginDTO);

        return new JWTResponse(token);
    }


}
