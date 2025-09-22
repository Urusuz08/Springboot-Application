package com.irtrains.train_service.model.admin;

import com.irtrains.train_service.model.user.User;
import com.irtrains.train_service.repository.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class Admin extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @NotNull(message="State cannot be null.")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
