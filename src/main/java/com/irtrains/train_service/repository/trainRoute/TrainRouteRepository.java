package com.irtrains.train_service.repository.trainRoute;

import java.util.*;

import com.irtrains.train_service.model.train_route.trainRoute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface TrainRouteRepository extends JpaRepository<trainRoute, Integer> {

    List<trainRoute> findByTrainIdOrderByDayNumber(String trainId);

    Optional<trainRoute> findByTrainIdAndStationCode(String trainId, String stationCode);

    List<trainRoute> findByStationCode(String stationCode);

    List<trainRoute> findByArrivalTime(String arrivalTime);

    List<trainRoute> findByDepartureTime(String departureTime);

    List<trainRoute> findByDayOfJourney(Integer dayOfJourney);

    List<trainRoute> findByDistanceFromSource(Double distanceFromSource);

    List<trainRoute> findByStationCodeOrderByDayOfJourneyAscArrivalTimeAsc(String stationCode);
    
}
