package com.iamthene.driverassistant.model;

import androidx.annotation.NonNull;

public class Vehicle {
    private int type;
    private String name;

    public Vehicle() {
    }

    public Vehicle(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
