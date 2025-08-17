package com.irtrains.train_service.repository.enums;

public enum Role {
    TrainADMIN("Train Admin"),
    USERAdmin("User Admin");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return roleName;
    }
}
