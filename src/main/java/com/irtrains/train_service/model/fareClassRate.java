package com.irtrains.train_service.model;

import jakarta.persistence.*;
import java.util.*;
import java.time.*;

@Entity
@Table(name="fare_class_rate")
public class fareClassRate {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="fare_id", nullable=false, updatable=false)
    private int fareId;

    @Column(name="fare_rule_set_id" , nullable=false)
    private int fareRuleSetId;

    @Column(name="class_type", nullable=false)
    private String classType;

    @Column(name="rate_per_km", nullable=false)
    private Double ratePerKm;

    @Column(name="base_fare", nullable=false)
    private Double baseFare;

    @Column(name="min_fare", nullable=false)
    private Double minFare;

    @Column(name="created_at", nullable=false, updatable=false)
    private Date createdAt;

    @Column(name="updated_at", nullable=false)
    private Date updatedAt;

    public int getFareId() {
        return fareId;
    }

    public void setFareId(int fareId) {
        this.fareId = fareId;
    }

    public int getFareRuleSetId() {
        return fareRuleSetId;
    }

    public void setFareRuleSetId(int fareRuleSetId) {
        this.fareRuleSetId = fareRuleSetId;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
