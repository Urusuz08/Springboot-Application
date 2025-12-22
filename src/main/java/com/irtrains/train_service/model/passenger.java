package com.irtrains.train_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "passengers")
public class passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id", nullable = false, updatable = false)
    private Long passengerId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "passenger_name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    @Min(value = 0, message = "Age cannot be negative.")
    @Max(value = 130, message = "Age seems unrealistic.")
    private Integer age;

    @Column(name="gender", nullable=false)
    @Pattern(
            regexp = "^(Male|Female|Other)$"
    )
    private String gender;

     // e.g., SL, 3A, 2A

    @Column(name = "coach_number", nullable = false)
    private Integer coachNumber;

    @Column(name="seat_number", nullable=false)
    private String seatNumber;

    @Column(name="berth_type", nullable=false)
    private String berthType; // e.g., LOWER, MIDDLE, UPPER, SIDE

    @Column(name="seat_status", nullable=false)
    private String seatStatus; // e.g., CONFIRMED, WAITING, RAC



    public Long getPassengerId() {
        return passengerId;
    }
    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getBookingId() {
        return bookingId;
    }
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender){
        this.gender=gender;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    public String getSeatStatus() {
        return seatStatus;
    }
    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }



    public Integer getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(Integer coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getBerthType() {
        return berthType;
    }

    public void setBerthType(String berthType) {
        this.berthType = berthType;
    }
}
