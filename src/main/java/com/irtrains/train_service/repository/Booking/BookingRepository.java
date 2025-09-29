package com.irtrains.train_service.repository.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.irtrains.train_service.model.booking.Booking;

import java.util.*;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPnrNumber(String pnrNumber);

    List<Booking> findByUserId(String userId);

}
