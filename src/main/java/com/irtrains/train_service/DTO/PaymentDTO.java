package com.irtrains.train_service.DTO;


public class PaymentDTO {
    private double amount;
    private String name;
    private String currency;
    private String transactionId;


    public PaymentDTO(double amount, String name, String currency) {
        this.amount = amount;
        this.name = name;
        this.currency = currency;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
