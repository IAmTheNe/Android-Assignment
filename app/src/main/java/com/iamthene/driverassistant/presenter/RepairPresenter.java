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
    private final RepairInterface.OnCheckEmptyList mGetRepair;

    public RepairPresenter(RepairInterface.OnCheckEmptyList mGetRepair) {
        this.mGetRepair = mGetRepair;
    }

    public void isEmptyList() {
        List<Repair> lstLinKien = new ArrayList<>();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference _myRef = mDatabase.getReference("Repair");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
}
