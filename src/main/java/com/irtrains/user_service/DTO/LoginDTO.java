package com.irtrains.user_service.DTO;

import com.irtrains.user_service.model.Role;

public class LoginDTO {
    private String username;
    private String password;
    private Role type;
    private boolean status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = Role.valueOf(type.toUpperCase());
    }

    public Role getType() {
        return type;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

}
