package com.iamthene.driverassistant.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.model.VehicleDetail;

public class AddCarPresenter {
    private final CarManagerInterface.AddCar mAddCar;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference _myRef = mDatabase.getReference("ListCars");

    public AddCarPresenter(CarManagerInterface.AddCar mAddCar) {
        this.mAddCar = mAddCar;
    }

    public void addCar(VehicleDetail vehicle) {
        if (vehicle.isEmptyInput()) {
            mAddCar.addError();
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                String keys = _myRef.push().getKey();
                assert keys != null;
                _myRef.child(uid).child(keys).child("name").setValue(vehicle.getName());
                _myRef.child(uid).child(keys).child("number").setValue(vehicle.getNumber());
                _myRef.child(uid).child(keys).child("currentKM").setValue(vehicle.getCurrentKM());
                _myRef.child(uid).child(keys).child("brand").setValue(vehicle.getBrand());
                _myRef.child(uid).child(keys).child("type").setValue(vehicle.getBrand());

            }
            mAddCar.addSuccess();
        }
    }
}
