package com.irtrains.train_service.DTO;

import java.util.*;
import java.time.*;

public class FareRuleDTO {

    private String ruleSetName;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String currency;
    private String description;
    private List<FareRateDTO> fareRates;

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

    public List<FareRateDTO> getFareRates() {
        return fareRates;
    }

    public void setFareRates(List<FareRateDTO> fareRates) {
        this.fareRates = fareRates;
    }
}
