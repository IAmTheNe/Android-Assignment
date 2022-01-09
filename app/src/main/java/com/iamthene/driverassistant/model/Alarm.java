package com.iamthene.driverassistant.model;

import android.text.TextUtils;

public class Alarm {
    private String id;
    private String title;
    private String createdAt;
    private String alarmDate;
    private String desc;
    private String type;

    public Alarm() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEmptyTitle() {
        return TextUtils.isEmpty(title);
    }

    public boolean isEmptyDesc() {
        return TextUtils.isEmpty(desc);
    }

    public boolean isEmptyAlarmDate() {
        return TextUtils.isEmpty(alarmDate);
    }

    public boolean isEmptyType() {
        return TextUtils.isEmpty(type);
    }

    public boolean isEmptyInput() {
        return isEmptyAlarmDate() || isEmptyDesc() || isEmptyType() || isEmptyTitle();
    }
}
