package com.irtrains.user_service.model;

public enum Role {
    ADMIN("Admin"),
    USER("User"),
    TRAIN_MANAGER("Train Manager"),
    STATION_MANAGER("Station Manager"),
    TICKET_MANAGER("Ticket Manager"),
    ROLE_MANAGER("Role Manager");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
