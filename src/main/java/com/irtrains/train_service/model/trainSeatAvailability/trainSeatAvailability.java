package com.irtrains.train_service.model.trainSeatAvailability;

import jakarta.persistence.*;

@Entity
@Table(name="train_seat_availability")
public class trainSeatAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="availability_id", nullable=false, updatable=false)
    private Integer seatAvailabilityId;

    @Column(name="train_id", nullable=false)
    private String trainId;

    @Column(name="seat_class", nullable=false)
    private String coachId;

    @Column(name="journey_date", nullable=false)
    private String dateOfJourney; // Format: "YYYY-MM-DD"

    @Column(name="available_seats", nullable=false)
    private Integer availableSeats;

    @Column(name="total_seats", nullable=false)
    private Integer totalSeats;

    @Column(name="last_updated", nullable=false)
    private Long lastUpdated;

    public Integer getSeatAvailabilityId() {
        return seatAvailabilityId;
    }
    public void setSeatAvailabilityId(Integer seatAvailabilityId) {
        this.seatAvailabilityId = seatAvailabilityId;
    }
    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



}
