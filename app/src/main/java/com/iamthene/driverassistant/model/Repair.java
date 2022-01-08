package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Repair implements Serializable {
    private String id;
    private String carId;
    private String date;
    private String time;
    private String part;
    private int price;


    public Repair() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return "Đã thay " + getPart() + " trị giá " + getPrice() + "đ";
    }

    public boolean isEmptyPrice() {
        return TextUtils.isEmpty(String.valueOf(price));
    }

    public boolean isEmptyPart() {
        return TextUtils.isEmpty(part);
    }

    public boolean isEmptyInput() {
        return isEmptyPart() || isEmptyPrice();
    }

    public String getPriceFormat() {
        NumberFormat dongFormat = NumberFormat.getCurrencyInstance(new Locale("vi"));
        dongFormat.setMaximumFractionDigits(0);
        return dongFormat.format(getPrice());
    }
}
