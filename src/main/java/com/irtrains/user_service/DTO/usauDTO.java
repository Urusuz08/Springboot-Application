package com.irtrains.user_service.DTO;

//this DTO is used for sending user data without sensitive information like password

public class usauDTO {
    private String name;
    private String username;
    private String role;

    public usauDTO(String name, String username, String role) {
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
