package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.Refuel;

import java.util.ArrayList;
import java.util.List;

public class RefuelPresenter {
    private RefuelInterface.OnCheckEmptyList refuel;
    private RefuelInterface.OnAddRefuel addRefuel;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef = mDatabase.getReference("Refuel");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public RefuelPresenter(RefuelInterface.OnCheckEmptyList refuel) {
        this.refuel = refuel;
    }

    public RefuelPresenter(RefuelInterface.OnAddRefuel addRefuel) {
        this.addRefuel = addRefuel;
    }

    public void isEmptyList() {
        List<Refuel> lstRefuel = new ArrayList<>();

        if (user != null) {
            String uid = user.getUid();
            _myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Refuel r = ds.getValue(Refuel.class);
                        lstRefuel.add(r);
                    }

                    if (lstRefuel.isEmpty()) {
                        refuel.empty();
                    } else {
                        refuel.exists();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void addRefuel(Refuel refuel) {
        if (refuel.isEmptyInput()) {
            addRefuel.addFailed(refuel);
        } else {
            if (user != null) {
                String uid = user.getUid();
                String keys = _myRef.push().getKey();
                refuel.setId(keys);
                if (keys != null) {
                    DatabaseReference dr = _myRef.child(uid).child(keys);
                    dr.child("id").setValue(refuel.getId());
                    dr.child("distance").setValue(refuel.getDistance());
                    dr.child("expDistance").setValue(refuel.getExpDistance());
                    dr.child("fee").setValue(refuel.getFee());
                    dr.child("fuel").setValue(refuel.getFuel());
                    dr.child("place").setValue(refuel.getPlace());
                    dr.child("time").setValue(refuel.getTime());
                    dr.child("carNo").setValue(refuel.getCarNo());
                }
                addRefuel.addSuccess();
            }
        }
    }
}
