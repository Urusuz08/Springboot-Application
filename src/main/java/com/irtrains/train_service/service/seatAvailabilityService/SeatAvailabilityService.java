package com.irtrains.train_service.service.seatAvailabilityService;

import com.irtrains.train_service.model.trainSeatAvailability.trainSeatAvailability;
import com.irtrains.train_service.repository.seatAvailabilityRepository.SeatAvailabilityRepository;

import org.springframework.beans.factory.annotation.*;
import java.time.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;


@Service
@Transactional(readOnly=true)
public class SeatAvailabilityService {

    private SeatAvailabilityRepository seatAvailabilityRepository;

    public SeatAvailabilityService(SeatAvailabilityRepository seatAvailabilityRepository){
        this.seatAvailabilityRepository=seatAvailabilityRepository;
    }

    @Transactional
    public trainSeatAvailability addSeatAvailability(trainSeatAvailability seatAvailability) {
        return seatAvailabilityRepository.save(seatAvailability);
    }

    public trainSeatAvailability getSeatAvailabilityByTrainIdAndCoachIdAndDateOfJourney(String trainId, String coachId, LocalDate dateOfJourney) {
        return seatAvailabilityRepository.findByTrainIdAndCoachIdAndDateOfJourney(trainId, coachId, dateOfJourney);
    }

    public trainSeatAvailability getSeatAvailabilityByTrainId(String trainId) {
        return seatAvailabilityRepository.findByTrainId(trainId);
    }

    @Transactional
    public trainSeatAvailability updateSeatAvailability(Integer seatAvailabilityId, trainSeatAvailability seatAvailabilityDetails) {
        trainSeatAvailability existingSeatAvailability = seatAvailabilityRepository.findById(seatAvailabilityId)
                .orElseThrow(() -> new RuntimeException("SeatAvailability not found with id: " + seatAvailabilityId));

        if (seatAvailabilityDetails.getTrainId() != null) {
            existingSeatAvailability.setTrainId(seatAvailabilityDetails.getTrainId());
        }
        if (seatAvailabilityDetails.getCoachId() != null) {
            existingSeatAvailability.setCoachId(seatAvailabilityDetails.getCoachId());
        }
        if (seatAvailabilityDetails.getDateOfJourney() != null) {
            existingSeatAvailability.setDateOfJourney(seatAvailabilityDetails.getDateOfJourney());
        }
        if (seatAvailabilityDetails.getAvailableSeats() != null) {
            existingSeatAvailability.setAvailableSeats(seatAvailabilityDetails.getAvailableSeats());
        }
        if (seatAvailabilityDetails.getTotalSeats() != null) {
            existingSeatAvailability.setTotalSeats(seatAvailabilityDetails.getTotalSeats());
        }
        existingSeatAvailability.setLastUpdated(System.currentTimeMillis());

        return seatAvailabilityRepository.save(existingSeatAvailability);
    }

    @Transactional
    public void deleteSeatAvailability(Integer seatAvailabilityId) {
        seatAvailabilityRepository.deleteById(seatAvailabilityId);
    }
}
