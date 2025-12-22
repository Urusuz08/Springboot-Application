package com.irtrains.train_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.irtrains.train_service.model.*;
import java.util.*;

@Repository
public interface FareClassRateRepo extends JpaRepository<fareClassRate,Integer> {

    Optional<fareClassRate> findByFareRuleSetId(int fareRuleSetId);

    Optional<fareClassRate> findByClassTypeAndFareRuleSetId(String classType, int fareRuleSetId);

}
