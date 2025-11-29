package com.irtrains.train_service.model;

import com.irtrains.train_service.model.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "stations")
public class Station {

    @Id
    @Column(name = "code", nullable = false, length = 10)
    @Pattern(
            regexp = "^[A-Z]{1,5}$",
            message = "Station code must be 2-5 uppercase letters (e.g., VAPI)."
    )
    private String code;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 255)
    @NotNull(message = "State cannot be null.")
    private State state;

    @Column(name = "place", nullable = false, length = 255)
    private String place;

    public Station() {}

    public Station(String code, String name, State state, String place) {
        this.code = code;
        this.name = name;
        this.state = state;
        this.place = place;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
}
