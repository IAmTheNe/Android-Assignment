package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.Repair;

import java.util.ArrayList;
import java.util.List;

public class RepairPresenter {
    private RepairInterface.OnCheckEmptyList mGetRepair;
    private RepairInterface.OnAddRepair onAddRepair;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef = mDatabase.getReference("Repair");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public RepairPresenter(RepairInterface.OnCheckEmptyList mGetRepair) {
        this.mGetRepair = mGetRepair;
    }

    public RepairPresenter(RepairInterface.OnAddRepair onAddRepair) {
        this.onAddRepair = onAddRepair;
    }

    public void isEmptyList() {
        List<Repair> lstLinKien = new ArrayList<>();
        if (user != null) {
            String uid = user.getUid();
            _myRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Repair lk = ds.getValue(Repair.class);
                        lstLinKien.add(lk);
                    }

                    if (lstLinKien.isEmpty()) {
                        mGetRepair.empty();
                    } else {
                        mGetRepair.exists();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void addRepair(Repair repair) {
        if (repair.isEmptyInput()) {
            onAddRepair.addFailed(repair);
        } else {
            if (user != null) {
                String uid = user.getUid();
                String keys = _myRef.push().getKey();
                repair.setId(keys);
                if (keys != null) {
                    DatabaseReference dr = _myRef.child(uid).child(keys);
                    dr.child("id").setValue(repair.getId());
                    dr.child("carId").setValue(repair.getCarId());
                    dr.child("date").setValue(repair.getDate());
                    dr.child("time").setValue(repair.getTime());
                    dr.child("part").setValue(repair.getPart());
                    dr.child("price").setValue(repair.getPrice());
                }
                onAddRepair.addSuccess();
            }
        }
    }
}
