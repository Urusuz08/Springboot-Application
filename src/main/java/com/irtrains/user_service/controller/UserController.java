package com.irtrains.user_service.controller;


import com.irtrains.user_service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.irtrains.user_service.model.*;
import com.irtrains.user_service.DTO.*;
import com.irtrains.user_service.service.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


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


    private <T> ResponseEntity<T> notFound(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (ex != null) body.put("detail", ex.getMessage());
        return ResponseEntity.status(status).body(body);
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
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        if(loginDTO.getType() != Role.USER) {
            return error(HttpStatus.BAD_REQUEST, "Invalid Credentials", null);
        }
        LoginDTO authenticatedUser = userService.authenticate(loginDTO);

        if(!authenticatedUser.getStatus()) {
            return error(HttpStatus.BAD_REQUEST, "Invalid Credentials", null);
        }

        String token = jwtUtility.generateToken(loginDTO);


        User user = userService.getUserByUsername(loginDTO.getUsername());
        usauDTO userDTO = new usauDTO(user.getName(),user.getUsername(), "USER");


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        response.put("user", userDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?>  loginAdmin(@RequestBody LoginDTO loginDTO) {
        if(loginDTO.getType() == Role.USER) {
            return error(HttpStatus.BAD_REQUEST, "Invalid Credentials", null);
        }
        LoginDTO authenticatedUser = userService.authenticate(loginDTO);

        if(!authenticatedUser.getStatus()) {
            return error(HttpStatus.BAD_REQUEST, "Invalid Credentials", null);
        }

        String token = jwtUtility.generateToken(loginDTO);
        Admin admin =userService.getAdminByUsername(loginDTO.getUsername());
        Map<String, Object> response = new LinkedHashMap<>();

        adauDTO adminDTO = new adauDTO(admin.getName(),admin.getUsername(), admin.getPassword());
        response.put("token", token);
        response.put("admin", adminDTO);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            boolean isValid = jwtUtility.validateToken(jwtToken);
            if (isValid) {
                User user= userService.getUserByUsername(jwtUtility.extractUsername(jwtToken));
                usauDTO userDTO = new usauDTO(user.getName(),user.getUsername(), "USER");
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("user", userDTO);
                return  ResponseEntity.status(HttpStatus.OK).body(response);

            } else {
                return error(HttpStatus.UNAUTHORIZED, "Invalid token", null);
            }
        } catch (Exception e) {
            return error(HttpStatus.BAD_REQUEST, "Error validating token", e);
        }
    }


}
