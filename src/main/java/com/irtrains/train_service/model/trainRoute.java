package com.irtrains.train_service.model;

import jakarta.persistence.*;
import java .time.*;


@Entity
@Table(name="train_route")
public class trainRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="route_id", nullable=false, updatable=false)
    private Integer routeId;

    @Column(name="train_id", nullable=false)
    private String trainId;

    @Column(name="station_code", nullable=false)
    private String stationCode;

    @Column(name="place", nullable=false)
    private String place;

    @Column(name="sequence", nullable=false)
    private Integer sequence; // Order of the station in the route

    @Column(name="arrival_time", nullable=false)
    private LocalTime arrivalTime; // Format: "HH:mm"

    @Column(name="departure_time", nullable=false)
    private LocalTime departureTime; // Format: "HH:mm"

    @Column(name="day", nullable=false)
    private Integer dayNumber; // e.g., 1 for first day, 2 for second day

    @Column(name="distance", nullable=false)
    private Integer distanceFromSource; // in kilometers

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public  String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Integer getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(Integer distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }
}
