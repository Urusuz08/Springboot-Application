package com.irtrains.train_service.DTO;

import java.util.List;
import com.irtrains.train_service.DTO.*;


public class CoachDTO {
    private String trainId;
    private List<CoachD> details;

    public String getTrainId() {
        return trainId;
    }
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
    public List<CoachD> getDetails() {
        return details;
    }
    public void setDetails(List<CoachD> details) {
        this.details = details;
    }
}
