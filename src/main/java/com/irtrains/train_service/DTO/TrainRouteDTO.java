package com.irtrains.train_service.DTO;

import java.time.LocalTime;

public class TrainRouteDTO {
        private String stationCode;
        private Integer sequence; // Order of the station in the route
        private LocalTime arrivalTime; // Format: "HH:mm"
        private LocalTime departureTime; // Format: "HH:mm"
        private Integer dayNumber; // e.g., 1 for first day, 2 for second day
        private Integer distanceFromSource; // in kilometers

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
