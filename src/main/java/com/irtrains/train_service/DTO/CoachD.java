package com.irtrains.train_service.DTO;

import java.util.*;
public class CoachD {
    private String coachType; // e.g., "Sleeper", "AC 3-tier"
    private Integer noOfCoaches;
    private Integer availableSeatsperCoach;
    private Set<String> store=new HashSet<>();

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        if(store.contains(coachType)) {
            throw new IllegalArgumentException("Duplicate coach type: " + coachType);
        }
        store.add(coachType);
        this.coachType = coachType;
    }
    public Integer getNoOfCoaches() {
        return noOfCoaches;
    }
    public void setNoOfCoaches(Integer noOfCoaches) {
        this.noOfCoaches = noOfCoaches;
    }


    public Integer getAvailableSeatsperCoach() {
        return availableSeatsperCoach;
    }

    public void setAvailableSeatsperCoach(Integer availableSeats) {
        this.availableSeatsperCoach = availableSeats;
    }
}
