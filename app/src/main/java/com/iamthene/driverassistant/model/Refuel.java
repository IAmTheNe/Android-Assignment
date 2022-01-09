package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Refuel implements Serializable {
    private String id;
    private String carNo;
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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public boolean isEmptyFee() {
        return TextUtils.isEmpty(fee);
    }

    public boolean isEmptyPlace() {
        return TextUtils.isEmpty(place);
    }

    public boolean isEmptyFuel() {
        return TextUtils.isEmpty(fuel);
    }

    public boolean isEmptyTime() {
        return TextUtils.isEmpty(time);
    }

    public boolean isEmptyDistance() {
        return TextUtils.isEmpty(distance);
    }

    public boolean isEmptyExpDistance() {
        return TextUtils.isEmpty(expDistance);
    }

    public boolean isEmptyInput() {
        return isEmptyFee() || isEmptyPlace() || isEmptyFuel() || isEmptyTime() || isEmptyDistance() || isEmptyExpDistance();
    }

}
