package com.expensesplitter.model;

import java.sql.Date;

public class Expense {

    private int id;
    private String title;
    private double amount;
    private String paidBy;
    private Date date;  
     public Expense() {}

    // Parameterized constructor
    public Expense(String title, double amount, String paidBy) {
        this.title = title;
        this.amount = amount;
        this.paidBy = paidBy;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public Date getDate() {  
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}