package com.irtrains.train_service.repository.passenger;

import com.irtrains.train_service.model.passenger.passenger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;


@Repository
public interface PassengerRepository extends JpaRepository<passenger, Long> {

    List<passenger> findByBookingId(Long bookingId);

}
