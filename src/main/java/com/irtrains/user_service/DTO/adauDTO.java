package com.irtrains.user_service.DTO;

//this dto is being used to send admin data without sensitive information like password
public class adauDTO {
    private String name;
    private String username;
    private String role;

    public adauDTO(String name, String username, String role) {
        this.name = name;
        this.username = username;
        this.role = role;
    }

    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
