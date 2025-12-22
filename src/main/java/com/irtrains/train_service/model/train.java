package com.irtrains.train_service.model;


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

    @Column(name = "monday")
    private Boolean monday;

    @Column(name = "tuesday")
    private Boolean tuesday;

    @Column(name = "wednesday")
    private Boolean wednesday;

    @Column(name = "thursday")
    private Boolean thursday;

    @Column(name = "friday")
    private Boolean friday;

    @Column(name = "saturday")
    private Boolean saturday;

    @Column(name = "sunday")
    private Boolean sunday;

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

    public Boolean isMonday() {
        return monday;
    }
    public void setMonday(Boolean monday) {
        this.monday = monday;
    }
    public Boolean isTuesday() {
        return tuesday;
    }
    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean isThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean isFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Boolean isSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }


}
