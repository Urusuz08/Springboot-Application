package com.irtrains.train_service.model;

import jakarta.persistence.*;


import java.util.*;
import java.time.*;

@Entity
@Table(name="fare_rule_sets")
public class fareRuleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rule_set_id", nullable=false, updatable=false)
    private int ruleSetId;

    @Column(name="rule_set_name", nullable=false, unique=true)
    private String ruleSetName;

    @Column(name="effective_from")
    @Temporal(TemporalType.DATE)
    private LocalDate effectiveFrom;

    @Column(name="effective_to")
    @Temporal(TemporalType.DATE)
    private LocalDate effectiveTo;

    @Column(name="currency", nullable=false)
    private String currency;

    @Column(name="description")
    private String description;

    @Column(name="created_by", nullable=false, updatable=false)
    private String createdBy;

    @Column(name="created_at", nullable=false, updatable=false)
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;


    public int getRuleSetId() {
        return ruleSetId;
    }
    public void setRuleSetId(int ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public String getRuleSetName() {
        return ruleSetName;
    }

    public void setRuleSetName(String ruleSetName) {
        this.ruleSetName = ruleSetName;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
