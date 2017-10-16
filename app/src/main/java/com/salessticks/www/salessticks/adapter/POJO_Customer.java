package com.salessticks.www.salessticks.adapter;

/**
 * Created by Saneeb Salam
 * on 6/11/2017.
 */

public class POJO_Customer {

    public POJO_Customer(String customerid, String id, String name, int quantity, double price) {
        this.customerid = customerid;
        this.id = id;
        this.name = name;
        this.Quantity = quantity;
        this.price = price;
    }

    public POJO_Customer() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public void setPrice(float price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    double price;

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    int Quantity;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private String customerName, customerid;
    private String name;
    private String routarea;

    public String getRoutID() {
        return routID;
    }

    public void setRoutID(String routID) {
        this.routID = routID;
    }

    private String routID;

    public String getRoutarea() {
        return routarea;
    }

    public void setRoutarea(String routarea) {
        this.routarea = routarea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeIn() {
        return TimeIn;
    }

    public void setTimeIn(String timeIn) {
        TimeIn = timeIn;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }

    private String TimeIn, TimeOut;
    private String Date;
}
