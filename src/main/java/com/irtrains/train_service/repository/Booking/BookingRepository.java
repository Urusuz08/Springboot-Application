package com.irtrains.train_service.repository.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.irtrains.train_service.model.booking.Booking;

import java.util.*;
import java.time.*;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPnrNumber(String pnrNumber);

    List<Booking> findByUserId(String userId);

    List<Booking> findAllByTrainIdAndJourneyDateAndCoachType(String trainId, LocalDate journeyDate, String coachType);
//    List<passenger> findBytrainIdandJourneyDateandandCoachType(String trainId, Date journeyDate,String coachType);

}
