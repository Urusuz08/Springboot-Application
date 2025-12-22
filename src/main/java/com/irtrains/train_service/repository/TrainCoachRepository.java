package com.irtrains.train_service.repository;

import com.irtrains.train_service.model.train_coaches;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface TrainCoachRepository extends JpaRepository<train_coaches, Integer> {

    List<train_coaches> findByTrainId(String trainId);

    List<train_coaches> findByCoachType(String coachType);

    List<train_coaches> findByCoachTypeAndTrainId(String coachType, String trainId);

    Optional<train_coaches> findByTrainIdAndCoachNumber(String trainId, Integer coachNumber);

    Optional<train_coaches> findBycoachId(Integer coachId);

    @Query("""
        SELECT count(tc.coachType) as totalCoaches, sum(tc.totalAvailableSeats) as totalSeats
        FROM train_coaches tc where tc.trainId=:trainId and tc.coachType=:coachType group by tc.coachType
    """)
    List<Integer> getCoachCountAndSeatsByType(@Param("trainId") String trainId,@Param("coachType") String coachType);

    @Query("""
        SELECT  ps.coachNumber, ps.seatNumber FROM passenger ps
        JOIN Booking bs on bs.bookingId=ps.bookingId
        WHERE bs.trainId=:trainId AND bs.journeyDate=:journeyDate AND bs.coachType=:coachType AND bs.status='CONFIRMED'
    """)
    List<int[]> getBookedSeats(@Param("trainId") String trainId,@Param("journeyDate") LocalDate journeyDate,@Param("coachType") String coachType);


//    List<train_coaches> getAllTrainCoaches();

}
