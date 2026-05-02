package com.expensesplitter.model;

public class Expense {

    private int id;
    private String title;
    private double amount;
    private String paidBy;

   
    public Expense() {}

    public Expense(String title, double amount, String paidBy) {
        this.title = title;
        this.amount = amount;
        this.paidBy = paidBy;
    }

   
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
}