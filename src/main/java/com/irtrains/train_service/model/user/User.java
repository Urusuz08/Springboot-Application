package com.irtrains.train_service.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

//import javax.persistence.*;
@Entity
@Table(name="User")
public class User {
    @Column (nullable=false, unique=true)
    @NotNull(message="Phone number cannot be null.")
    @Pattern(
            regexp= "^[6-9]\\d{9}",
            message="Phone number must be 10 digits and start with 6, 7, 8, or 9."
    )
    private Long phone;

    @Id
    private String username;

    @Column(nullable=false)
    private String name;

    @Column(unique=true, nullable=false)
    @Email
    private String email;

    // Getters and Setters
    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
