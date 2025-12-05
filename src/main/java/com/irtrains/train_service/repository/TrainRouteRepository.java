package com.irtrains.train_service.repository;

import com.irtrains.train_service.model.trainRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRouteRepository extends JpaRepository<trainRoute, Integer> {

    List<trainRoute> findByTrainIdOrderByDayNumber(String trainId);

    Optional<trainRoute> findByTrainIdAndStationCode(String trainId, String stationCode);

    List<trainRoute> findByStationCode(String stationCode);

    List<trainRoute> findByPlace(String place);

    @Query("""
            SELECT tr2.distanceFromSource - tr1.distanceFromSource as distance
            FROM trainRoute tr1 JOIN trainRoute tr2 ON tr1.trainId=tr2.trainId
            WHERE tr1.stationCode = :sourceStationCode
            AND tr2.stationCode = :destinationStationCode
            AND tr1.trainId = :trainId
            """)
    int distance(String trainId, String sourceStationCode, String destinationStationCode);

    @Query("""
    SELECT tr.stationCode from trainRoute tr 
    WHERE tr.trainId=:trainId AND tr.place=:place
        ORDER BY tr.sequence ASC LIMIT 1

    """)
    String stationCode(String trainId, String place);

    @Query("SELECT tr.arrivalTime from trainRoute tr WHERE tr.trainId=:trainId AND tr.stationCode=:stationCode")
    LocalTime arrivalTime(String trainId, String stationCode);

    @Query("SELECT tr.departureTime from trainRoute tr WHERE tr.trainId=:trainId AND tr.stationCode=:stationCode")
    LocalTime departureTime(String trainId, String stationCode);

    @Query("SELECT tr.dayNumber from trainRoute tr WHERE tr.trainId=:trainId AND tr.stationCode=:stationCode")
    int dayNumber(String trainId, String stationCode);

    List<trainRoute> findByArrivalTime(LocalTime arrivalTime);

    List<trainRoute> findByDepartureTime(LocalTime departureTime);

    List<trainRoute> findByDayNumber(Integer dayNumber);

    List<trainRoute> findByDistanceFromSource(Integer distanceFromSource);

    List<trainRoute> findByStationCodeOrderByDayNumberAscArrivalTimeAsc(String stationCode);

}
