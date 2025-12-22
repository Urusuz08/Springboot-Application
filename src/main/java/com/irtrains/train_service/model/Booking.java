package com.irtrains.train_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.*;

import java.time.LocalDate;
import java.util.*;



@Entity
@Table(name="bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id", nullable=false, updatable=false)
    @Setter
    @Getter
    private Long bookingId;

    @Column(name="pnr_number", nullable=false, unique=true, updatable=false)
    @Setter
    @Getter
    private String pnrNumber;

    @Column(name="user_id", nullable=false)
    @Setter
    @Getter
    private String userId;

    @Column(name="contact_number", nullable=false)
    @Setter
    @Getter
    private String contactNumber;

    @Column(name="email", nullable=false)
    @Setter
    @Getter
    private String email;

    @Column(name="train_id", nullable=false)
    @Setter
    @Getter
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
    @Setter
    @Getter
    private String sourceStationCode;

    @Column(name="destination_station_code", nullable=false)
    @Pattern(
            regexp = "^[A-Z]{1,5}$",
            message = "Station code must be 2-5 uppercase letters (e.g., VAPI)."
    )
    @Setter
    @Getter
    private String destinationStationCode;

    @Column(name = "coach_type", nullable = false)
    @Setter
    @Getter
    private String coachType;

    @Column(name="journey_date", nullable=false)
    @Temporal(TemporalType.DATE)
    @Setter
    @Getter
    private LocalDate  journeyDate; //Over here the datatype Date is imported from the util package.

    @Column(name="booking_date", nullable=false, updatable=false)
    @Setter
    @Getter
//    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    @Setter
    @Getter
    @Column(name="status", nullable=false)
    private String status; // e.g., CONFIRMED, CANCELLED, PENDING

    @Column(name = "total_fare", nullable = false)
    @Setter
    @Getter
    private Double fare;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="fare_breakup", columnDefinition = "JSONB", nullable =false)
    @Setter
    @Getter
    private Map<String,Object> fareBreakup; // JSON or String representation of fare details

}
