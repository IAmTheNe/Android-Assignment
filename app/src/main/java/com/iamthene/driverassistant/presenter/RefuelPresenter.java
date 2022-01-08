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
    private final RefuelInterface.OnCheckEmptyList refuel;

    public RefuelPresenter(RefuelInterface.OnCheckEmptyList refuel) {
        this.refuel = refuel;
    }

    public void isEmptyList() {
        List<Refuel> lstRefuel = new ArrayList<>();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference _myRef = mDatabase.getReference("Refuel");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
}
