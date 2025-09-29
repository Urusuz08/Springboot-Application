package com.irtrains.train_service.service.passenger;

import com.irtrains.train_service.model.passenger.passenger;
import com.irtrains.train_service.repository.passenger.PassengerRepository;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Transactional
    public passenger addPassenger(passenger passenger) {

        return passengerRepository.save(passenger);
    }

    public List<passenger> getPassengersByBookingId(Long bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }

    @Transactional
    public void cancelPassengersByBookingId(Long bookingId) {
        List<passenger> passengers = passengerRepository.findByBookingId(bookingId);
        for (passenger passenger : passengers) {
            passenger.setSeatStatus("CANCELLED");
        }
        passengerRepository.saveAll(passengers);
    }

    /**
     * Updates the status of a specific passenger.
     * @param passengerId The ID of the passenger to update.
     * @param newStatus The new status (e.g., "CANCELLED").
     * @return The updated passenger.
     */
    @Transactional
    public passenger updatePassengerStatus(Long passengerId, String newStatus) {
        passenger passengerToUpdate = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with ID: " + passengerId));

        passengerToUpdate.setSeatStatus(newStatus);
        return passengerRepository.save(passengerToUpdate);
    }
}
