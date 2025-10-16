package com.irtrains.user_service.DTO;

public class AdminDTO {

    private String username;
    private String email;
    private Long phone;
    private String role;

    public AdminDTO() {
    }

    public AdminDTO( String username, String email, Long phone, String role) {

        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
