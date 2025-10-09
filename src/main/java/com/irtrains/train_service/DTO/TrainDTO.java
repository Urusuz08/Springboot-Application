package com.irtrains.train_service.DTO;

import java.util.*;
import com.irtrains.train_service.DTO.TrainRouteDTO;

public class TrainDTO {
    private String trainId;
    private String trainName;
    private String trainType;
    private List<TrainRouteDTO> route;

    public String getTrainId() {
        return trainId;
    }
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public List<TrainRouteDTO> getRoute() {
        return route;
    }

    public void setRoute(List<TrainRouteDTO> route) {
        this.route = route;
    }
}
