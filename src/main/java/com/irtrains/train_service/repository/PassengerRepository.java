package com.irtrains.train_service.repository;

import com.irtrains.train_service.model.passenger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;


@Repository
public interface PassengerRepository extends JpaRepository<passenger, Long> {

    List<passenger> findByBookingId(Long bookingId);

    List<passenger> findAllByBookingIdIn(List<Long> bookingIds);



}
