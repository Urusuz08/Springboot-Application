package com.irtrains.train_service.model.train;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import com.irtrains.train_service.model.enums.Type;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="trains")
public class train {
    @Id
    @Column(name="train_id",nullable=false, unique=true, updatable = false)
    @Pattern(
            regexp = "^[0-9]{5}$",
            message = "TrainID must be exactly 5 digits (e.g., 12345)."
    )
    private String trainId;

    @Column(nullable=false, unique=true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @NotNull(message="Types cannot be null.")
    private Type type;

    @Column(name="source_station_code",nullable=false)
    private String sourceStation;

    @Column(name="destination_station_code",nullable=false)
    private String destinationStation;

    public String getTrainId() {
        return trainId;
    }

    public void setTrainID(String trainId) {
        this.trainId = trainId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }


}
