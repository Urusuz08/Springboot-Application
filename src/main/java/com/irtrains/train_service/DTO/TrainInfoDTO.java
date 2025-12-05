package com.irtrains.train_service.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;
import java.time.*;


public class TrainInfoDTO {
    private String trainNumber;
    private String trainName;
    private String trainType;
    private List<Boolean> daysOfOperation;
    private Map<String, Double> totalFare;
    private String sourceStationCode;
    private String sourceStationName;
    private String destinationStationCode;
    private String destinationStationName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime departureTimeFromSource;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime arrivalTimeOnDestination;

    private Map<String, Integer> travelDuration;


    private LocalDate journeyDate;
    private LocalDate arrivalDate;

    private Map<LocalDate,Map<String, Integer>> availableSeatsMap;
    private List<TrainRouteDTO> trainRoute;

    public String getTrainNumber() {
        return trainNumber;
    }
    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
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

    public List<Boolean> getDaysOfOperation() {
        return daysOfOperation;
    }

    public void setDaysOfOperation(List<Boolean> daysOfOperation) {
        this.daysOfOperation = daysOfOperation;
    }

    public Map<String, Double> getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Map<String, Double> totalFare) {
        this.totalFare = totalFare;
    }

    public String getSourceStationCode() {
        return sourceStationCode;
    }

    public void setSourceStationCode(String sourceStationCode) {
        this.sourceStationCode = sourceStationCode;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public void setSourceStationName(String sourceStationName) {
        this.sourceStationName = sourceStationName;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public void setDestinationStationName(String destinationStationName) {
        this.destinationStationName = destinationStationName;
    }

    public LocalTime getDepartureTimeFromSource() {
        return departureTimeFromSource;
    }

    public void setDepartureTimeFromSource(LocalTime departureTimeFromSource) {
        this.departureTimeFromSource = departureTimeFromSource;
    }

    public LocalTime getArrivalTimeOnDestination() {
        return arrivalTimeOnDestination;
    }

    public void setArrivalTimeOnDestination(LocalTime arrivalTimeOnDestination) {
        this.arrivalTimeOnDestination = arrivalTimeOnDestination;
    }

    public Map<String, Integer> getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(Map<String, Integer> travelDuration) {
        this.travelDuration = travelDuration;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Map<LocalDate, Map<String, Integer>> getAvailableSeatsMap() {
        return availableSeatsMap;
    }

    public void setAvailableSeatsMap(Map<LocalDate,Map<String, Integer>> availableSeatsMap) {
        this.availableSeatsMap = availableSeatsMap;
    }

    public List<TrainRouteDTO> getTrainRoute() {
        return trainRoute;
    }

    public void setTrainRoute(List<TrainRouteDTO> trainRoute) {
        this.trainRoute = trainRoute;
    }

}