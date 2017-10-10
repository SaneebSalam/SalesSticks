package com.salessticks.www.salessticks.adapter;

/**
 * Created by Saneeb Salam
 * on 6/11/2017.
 */

public class POJO_Customer {

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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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
    private String Price;
}
