package com.expensesplitter.model;

public class Settlement {

    private String from;
    private String to;
    private double amount;

    public Settlement() {}

    public Settlement(String from, String to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public double getAmount() { return amount; }
}