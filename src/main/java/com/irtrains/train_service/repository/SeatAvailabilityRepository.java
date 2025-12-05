package com.irtrains.train_service.repository;

import com.irtrains.train_service.model.trainSeatAvailability;

import java.time.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;


@Repository
public interface SeatAvailabilityRepository extends JpaRepository<trainSeatAvailability, Integer> {

    trainSeatAvailability findByTrainIdAndCoachIdAndDateOfJourney(String trainId, String coachId, LocalDate dateOfJourney);

    trainSeatAvailability findByTrainId(String trainId);

    trainSeatAvailability findByTrainIdAndCoachId(String trainId, String coachId);

    @Query("""
        SELECT sa.availableSeats from trainSeatAvailability sa 
        WHERE sa.trainId=:trainId AND sa.dateOfJourney=:dateOfJourney AND sa.coachId=:seatType
    """)
    int availableSeats(String trainId, LocalDate dateOfJourney, String seatType);

    @Query(
            """
            SELECT DISTINCT sa.coachId FROM trainSeatAvailability sa 
            WHERE sa.trainId=:trainId
    """
    )
    List<String> getCoachTypes(String trainId);

}
