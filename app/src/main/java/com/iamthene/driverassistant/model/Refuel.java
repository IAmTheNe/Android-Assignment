package com.iamthene.driverassistant.model;

import java.io.Serializable;

public class Refuel implements Serializable {
    private String id;
    private String fee;
    private String place;
    private String fuel;
    private String time;
    private String distance;
    private String expDistance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getExpDistance() {
        return expDistance;
    }

    public void setExpDistance(String expDistance) {
        this.expDistance = expDistance;
    }
}
