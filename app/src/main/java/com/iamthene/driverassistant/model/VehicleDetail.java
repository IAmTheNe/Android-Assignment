package com.iamthene.driverassistant.model;

public class VehicleDetail {
    private int no;
    private String name;
    private String number;
    private int currentKM;
    private String brand;
    private Vehicle type;

    public VehicleDetail() {
    }

    public VehicleDetail(int no, String name, String number, int currentKM, String brand, Vehicle type) {
        this.no = no;
        this.name = name;
        this.number = number;
        this.currentKM = currentKM;
        this.brand = brand;
        this.type = type;
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
}
