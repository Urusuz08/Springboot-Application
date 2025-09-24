package com.irtrains.train_service.model.enums;

public enum Type {
    RAJDHANI("Rajdhani"),
    SHATABDI("Shatabdi"),
    DURONTO("Duronto"),
    SF_EXPRESS("Superfast Express"),
    EXPRES("Express"),
    PASSENGER("Passenger"),
    SHUTTLE("Shuttle"),
    LOCAL("Local"),
    OTHER("Other");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
