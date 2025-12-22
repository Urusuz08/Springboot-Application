package com.irtrains.train_service.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.*;
import com.irtrains.train_service.model.*;

@Repository
public interface FareRuleSetRepo extends JpaRepository<fareRuleSet,Integer> {

    @Query("""
            SELECT frs FROM fareRuleSet frs
                        WHERE frs.effectiveFrom <= :effectiveDate
                        ORDER BY frs.effectiveFrom DESC LIMIT 1
            """ )
    fareRuleSet findByEffectiveDate(LocalDate effectiveDate);

    Optional<fareRuleSet> findByRuleSetName(String ruleSetName);
}
