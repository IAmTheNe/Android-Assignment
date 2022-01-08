package com.iamthene.driverassistant.model;

import java.io.Serializable;

public class Oil implements Serializable {
    private String idOil;
    private String feeOil;
    private String placeOil;
    private String timeOil;

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
}
