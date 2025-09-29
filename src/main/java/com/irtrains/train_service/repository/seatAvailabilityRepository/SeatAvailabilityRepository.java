package com.irtrains.train_service.repository.seatAvailabilityRepository;

import com.irtrains.train_service.model.trainSeatAvailability.trainSeatAvailability;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;


@Repository
public interface SeatAvailabilityRepository extends JpaRepository<trainSeatAvailability, Integer> {

    trainSeatAvailability findByTrainIdAndCoachIdAndDateOfJourney(String trainId, String coachId, String dateOfJourney);

    trainSeatAvailability findByTrainId(String trainId);

}
