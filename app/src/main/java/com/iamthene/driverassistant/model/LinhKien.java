package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LinhKien implements Serializable {
    private String carId;
    private String date;
    private String time;
    private String part;
    private int price;


    public LinhKien() {
    }

    public LinhKien(String carId, String date, String time, String part, int price) {
        this.carId = carId;
        this.date = date;
        this.time = time;
        this.part = part;
        this.price = price;
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

    public static List<LinhKien> init() {
        List<LinhKien> lstLK = new ArrayList<>();
        lstLK.add(new LinhKien("1", "25/11/2021", "23:11", "Tay đông", 500000));
        lstLK.add(new LinhKien("2", "26/11/2021", "23:11", "Tay đông", 250000));
        lstLK.add(new LinhKien("3", "27/11/2021", "23:11", "Tay đông", 90000));
        lstLK.add(new LinhKien("4", "28/11/2021", "23:11", "Tay đông", 150000));
        lstLK.add(new LinhKien("5", "29/11/2021", "23:11", "Tay đông", 100000));
        lstLK.add(new LinhKien("6", "1/12/2021", "23:11", "Tay đông", 300000));
        lstLK.add(new LinhKien("7", "4/12/2021", "23:11", "Tay đông", 900000));
        lstLK.add(new LinhKien("8", "10/12/2021", "23:11", "Tay đông", 1500000));
        return lstLK;
    }
}
