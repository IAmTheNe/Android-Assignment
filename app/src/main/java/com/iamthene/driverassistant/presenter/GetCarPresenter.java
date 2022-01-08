package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.VehicleDetail;

import java.util.ArrayList;
import java.util.List;

public class GetCarPresenter {
    CarManagerInterface.OnCheckEmptyList checkEmptyList;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference _myRef = mDatabase.getReference("ListCars");

    public GetCarPresenter(CarManagerInterface.OnCheckEmptyList checkEmptyList) {
        this.checkEmptyList = checkEmptyList;
    }

    public void hasNoOwnerCar() {
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

                    if (yourCar.isEmpty()) {
                        checkEmptyList.empty();
                    } else {
                        checkEmptyList.exists();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
