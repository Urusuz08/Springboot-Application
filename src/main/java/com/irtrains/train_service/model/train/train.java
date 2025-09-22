package com.irtrains.train_service.model.train;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import com.irtrains.train_service.model.enums.State;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="stations")
public class train {
    @Id
    @Pattern(
            regexp = "^[A-Z]{5}$",
            message = "Train code must be in the format of five digits (e.g., VAPI)."
    )
    private String code;

    @Column(nullable=false, unique=true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @NotNull(message="State cannot be null.")
    private State state;

    @Column(nullable=false)
    private String place;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


}
