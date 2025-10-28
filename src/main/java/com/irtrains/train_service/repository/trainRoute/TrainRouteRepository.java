package com.irtrains.train_service.repository.trainRoute;

import com.irtrains.train_service.model.train_route.trainRoute;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<trainRoute> findByArrivalTime(LocalTime arrivalTime);

    List<trainRoute> findByDepartureTime(LocalTime departureTime);

    List<trainRoute> findByDayNumber(Integer dayNumber);

    List<trainRoute> findByDistanceFromSource(Integer distanceFromSource);

    List<trainRoute> findByStationCodeOrderByDayNumberAscArrivalTimeAsc(String stationCode);

}
