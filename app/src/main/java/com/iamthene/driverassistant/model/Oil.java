package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Oil implements Serializable {
    private String carName;
    private String idOil;
    private String feeOil;
    private String placeOil;
    private String timeOil;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getIdOil() {
        return idOil;
    }

    public void setIdOil(String idOil) {
        this.idOil = idOil;
    }

    public String getFeeOil() {
        return feeOil;
    }

    public void setFeeOil(String feeOil) {
        this.feeOil = feeOil;
    }

    public String getPlaceOil() {
        return placeOil;
    }

    public void setPlaceOil(String placeOil) {
        this.placeOil = placeOil;
    }

    public String getTimeOil() {
        return timeOil;
    }

    public void setTimeOil(String timeOil) {
        this.timeOil = timeOil;
    }

    public boolean isEmptyFee() {
        return TextUtils.isEmpty(feeOil);
    }

    public boolean isEmptyPlace() {
        return TextUtils.isEmpty(placeOil);
    }

    public boolean isEmptyInput() {
        return isEmptyFee() || isEmptyPlace();
    }
}
