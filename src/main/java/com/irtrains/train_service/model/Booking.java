package com.irtrains.train_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.*;



@Entity
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id", nullable=false, updatable=false)
    private Long bookingId;

    @Column(name="pnr_number", nullable=false, unique=true, updatable=false)
    private String pnrNumber;

    @Column(name="user_id", nullable=false)
    private String userId;

    @Column(name="contact_number", nullable=false)
    private String contactNumber;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="train_id", nullable=false)
    @Pattern(
            regexp = "^[0-9]{5}$",
            message = "TrainID must be exactly 5 digits (e.g., 12345)."
    )
    private String trainId;

    @Column(name="source_station_code", nullable=false)
    @Pattern(
            regexp = "^[A-Z]{1,5}$",
            message = "Station code must be 2-5 uppercase letters (e.g., VAPI)."
    )
    private String sourceStationCode;

    @Column(name="destination_station_code", nullable=false)
    @Pattern(
            regexp = "^[A-Z]{1,5}$",
            message = "Station code must be 2-5 uppercase letters (e.g., VAPI)."
    )
    private String destinationStationCode;

    @Column(name = "coach_type", nullable = false)
    private String coachType;

    @Column(name="journey_date", nullable=false)
    @Temporal(TemporalType.DATE)
    private LocalDate  journeyDate; //Over here the datatype Date is imported from the util package.

    @Column(name="booking_date", nullable=false, updatable=false)
//    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    @Column(name="status", nullable=false)
    private String status; // e.g., CONFIRMED, CANCELLED, PENDING

    @Column(name = "total_fare", nullable = false)
    private Double fare;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="fare_breakup", columnDefinition = "JSONB", nullable =false)
    private Map<String,Object> fareBreakup; // JSON or String representation of fare details

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getSourceStationCode() {
        return sourceStationCode;
    }

    public void setSourceStationCode(String sourceStationCode) {
        this.sourceStationCode = sourceStationCode;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public LocalDate getTravelDate() {
        return journeyDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.journeyDate = travelDate;
    }

    public java.util.Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(java.util.Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getFare() {
        return fare;
    }
    public void setFare(Double fare) {
        this.fare = fare;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public Map<String, Object> getFareBreakup() {
        return fareBreakup;
    }
    public void setFareBreakup(Map<String, Object> fareBreakup) {
        this.fareBreakup = fareBreakup;
    }
}
