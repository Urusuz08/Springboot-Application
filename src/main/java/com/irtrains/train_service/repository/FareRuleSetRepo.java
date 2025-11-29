package com.irtrains.train_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import com.irtrains.train_service.model.*;

@Repository
public interface FareRuleSetRepo extends JpaRepository<fareRuleSet,Integer> {

    Optional<fareRuleSet> findByRuleSetName(String ruleSetName);
}
