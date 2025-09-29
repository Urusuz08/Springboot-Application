package com.irtrains.train_service.repository.train_coach;

import com.irtrains.train_service.model.train_coaches.train_coaches;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface TrainCoachRepository extends JpaRepository<train_coaches, Integer> {

    List<train_coaches> findByTrainId(String trainId);

    List<train_coaches> findByCoachType(String coachType);

    List<train_coaches> findByCoachTypeAndTrainId(String coachType, String trainId);

    Optional<train_coaches> findByTrainIdAndCoachNumber(String trainId, Integer coachNumber);

    Optional<train_coaches> findBycoachId(Integer coachId);

//    List<train_coaches> getAllTrainCoaches();

}
