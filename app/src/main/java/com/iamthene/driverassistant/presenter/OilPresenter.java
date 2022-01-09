package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.Oil;

import java.util.ArrayList;
import java.util.List;

public class OilPresenter {
    private OilInterface.OnCheckEmptyList onCheckEmptyList;
    private OilInterface.OnAddOil onAddOil;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef = mDatabase.getReference("Oil");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public OilPresenter(OilInterface.OnCheckEmptyList onCheckEmptyList) {
        this.onCheckEmptyList = onCheckEmptyList;
    }

    public OilPresenter(OilInterface.OnAddOil onAddOil) {
        this.onAddOil = onAddOil;
    }

    public void isEmptyList() {
        List<Oil> lstOil = new ArrayList<>();
        if (user != null) {
            String uid = user.getUid();
            _myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Oil oil = ds.getValue(Oil.class);
                        lstOil.add(oil);
                    }

                    if (lstOil.isEmpty()) {
                        onCheckEmptyList.empty();
                    } else {
                        onCheckEmptyList.exists();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void addOil(Oil o) {
        if (o.isEmptyInput()) {
            onAddOil.addFailed(o);
        } else {
            if (user != null) {
                String uid = user.getUid();
                String keys = _myRef.push().getKey();
                o.setIdOil(keys);
                if (keys != null) {
                    DatabaseReference dr = _myRef.child(uid).child(keys);
                    dr.child("idOil").setValue(o.getIdOil());
                    dr.child("carName").setValue(o.getCarName());
                    dr.child("feeOil").setValue(o.getFeeOil());
                    dr.child("timeOil").setValue(o.getTimeOil());
                    dr.child("placeOil").setValue(o.getPlaceOil());
                }
                onAddOil.addSuccess();
            }
        }
    }
}
