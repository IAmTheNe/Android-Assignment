package com.iamthene.driverassistant.model;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String uuid;
    public String firstName;
    public String lastName;
    public String email;
    public String password;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return getFirstName().concat(" ").concat(getLastName());
    }

    public boolean isEmptyEmail() {
        return TextUtils.isEmpty(email);
    }

    public boolean isEmptyPassword() {
        return TextUtils.isEmpty(password);
    }

    public boolean isEmptyFirstName() {
        return TextUtils.isEmpty(firstName);
    }

    public boolean isEmptyLastName() {
        return TextUtils.isEmpty(lastName);
    }

    public boolean isEmptyUser() {
        return isEmptyFirstName() || isEmptyLastName() || isEmptyEmail() || isEmptyPassword();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);

        return result;
    }
}
