package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class VehicleDetail {
    private int no;
    private String name;
    private String number;
    private int currentKM;
    private String brand;
    private Vehicle type;

    public VehicleDetail() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCurrentKM() {
        return currentKM;
    }

    public void setCurrentKM(int currentKM) {
        this.currentKM = currentKM;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Vehicle getType() {
        return type;
    }

    public void setType(Vehicle type) {
        this.type = type;
    }

    public boolean isVehicleName() {
        return TextUtils.isEmpty(name);
    }

    public boolean isEmptyPlate() {
        return TextUtils.isEmpty(number);
    }

    public boolean isEmptyBrand() {
        return TextUtils.isEmpty(brand);
    }

    public boolean isEmptyKM() {
        return TextUtils.isEmpty(String.valueOf(currentKM));
    }

    public boolean isEmptyCarType() {
        return type == null;
    }

    public boolean isEmptyInput() {
        return isVehicleName() || isEmptyBrand() || isEmptyPlate() || isEmptyKM() || isEmptyCarType();
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
