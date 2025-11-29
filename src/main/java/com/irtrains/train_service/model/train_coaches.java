package com.irtrains.train_service.model;

import jakarta.persistence.*;

@Entity
@Table(name="train_coaches")
public class train_coaches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coach_id" , nullable=false, updatable=false)
    private Integer coachId; // e.g., "SL1", "3A2"

    @Column(name="train_id", nullable=false)
    private String trainId; // e.g., "12345"

    @Column(name="coach_type", nullable=false)
    private String coachType; // e.g., "SL", "3A", "2

    @Column(name="total_available_seats", nullable=false)
    private Integer totalAvailableSeats;

    @Column(name="coach_number", nullable=false)
    private Integer coachNumber; // e.g., "1", "2"

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public Integer getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(Integer totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
    }

    public Integer getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(int coachNumber) {
        this.coachNumber = coachNumber;
    }

}
