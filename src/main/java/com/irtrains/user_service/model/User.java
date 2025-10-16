package com.irtrains.user_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false, unique = true)
    private Long phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public void setId(Long id) {
        Id = id;
    }
    public Long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
