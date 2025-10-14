package com.irtrains.train_service.repository.seatAvailabilityRepository;

import com.irtrains.train_service.model.trainSeatAvailability.trainSeatAvailability;

import java.time.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;


@Repository
public interface SeatAvailabilityRepository extends JpaRepository<trainSeatAvailability, Integer> {

    trainSeatAvailability findByTrainIdAndCoachIdAndDateOfJourney(String trainId, String coachId, LocalDate dateOfJourney);

    trainSeatAvailability findByTrainId(String trainId);

    trainSeatAvailability findByTrainIdAndCoachId(String trainId, String coachId);

}
