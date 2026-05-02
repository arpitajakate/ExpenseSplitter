package com.expensesplitter.model;

public class Split {

    private int expenseId;
    private String participant;
    private double amount;

    public Split(int expenseId, String participant, double amount) {
        this.expenseId = expenseId;
        this.participant = participant;
        this.amount = amount;
    }

    public int getExpenseId() { return expenseId; }
    public String getParticipant() { return participant; }
    public double getAmount() { return amount; }
}