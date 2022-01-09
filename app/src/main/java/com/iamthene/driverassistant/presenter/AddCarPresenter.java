package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.Vehicle;
import com.iamthene.driverassistant.model.VehicleDetail;

import java.util.ArrayList;
import java.util.List;

public class AddCarPresenter {
    private final CarManagerInterface.AddCar mAddCar;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference _myRef = mDatabase.getReference("ListCars");

    public AddCarPresenter(CarManagerInterface.AddCar mAddCar) {
        this.mAddCar = mAddCar;
    }

    public void addCar(VehicleDetail vehicle) {
        if (vehicle.isEmptyInput()) {
            mAddCar.addError(vehicle);
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
                _myRef.child(uid).child(keys).child("type").setValue(vehicle.getType());

            }
            mAddCar.addSuccess();
        }
    }

    public List<Vehicle> getCar() {
        List<Vehicle> lstVehicle = new ArrayList<>();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference _myRef = mDatabase.getReference("Vehicles");
        _myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Vehicle v = ds.getValue(Vehicle.class);
                    lstVehicle.add(v);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return lstVehicle;
    }

    public List<VehicleDetail> getOwnerCar() {
        List<VehicleDetail> yourCar = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            _myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        VehicleDetail vd = ds.getValue(VehicleDetail.class);
                        yourCar.add(vd);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return yourCar;
    }

}
