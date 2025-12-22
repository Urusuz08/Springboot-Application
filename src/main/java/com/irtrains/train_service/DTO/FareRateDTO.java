package com.irtrains.train_service.DTO;

public class FareRateDTO {

    private String classType;
    private Double ratePerKm;
    private Double baseFare;
    private Double minFare;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Double getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(Double ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getMinFare() {
        return minFare;
    }

    public void setMinFare(Double minFare) {
        this.minFare = minFare;
    }
}
