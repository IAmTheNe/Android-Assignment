package com.iamthene.driverassistant.presenter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.model.User;

import java.util.List;

public class UserManagerPresenter {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference _myRef = mDatabase.getReference("Users");

    public UserManagerPresenter() {
    }

    public void addUser(User user) {
        _myRef.child(user.getUuid()).child("email").setValue(user.getEmail());
        _myRef.child(user.getUuid()).child("firstName").setValue(user.getFirstName());
        _myRef.child(user.getUuid()).child("lastName").setValue(user.getLastName());
        _myRef.child(user.getUuid()).child("uuid").setValue(user.getUuid());
    }
}
