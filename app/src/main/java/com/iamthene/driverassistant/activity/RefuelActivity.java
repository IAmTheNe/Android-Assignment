package com.iamthene.driverassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.adapter.RefuelAdapter;
import com.iamthene.driverassistant.model.Refuel;

import java.util.ArrayList;
import java.util.List;

public class RefuelActivity extends AppCompatActivity {
    RecyclerView rvRefuel;
    List<Refuel> lstRefuel;
    RefuelAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ImageButton btnAdd;
    List<String> mKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);

        rvRefuel = findViewById(R.id.rvRefuel);
        btnAdd = findViewById(R.id.imgBtnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RefuelActivity.this, NewRefuelActivity.class);
                startActivity(i);
            }
        });

        getData();

        rvRefuel.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RefuelAdapter(lstRefuel, RefuelActivity.this);
        rvRefuel.setAdapter(adapter);
    }

    private void getData() {
        lstRefuel = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef = database.getReference("Refuel");
        assert user != null;
        myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p != null) {
                    lstRefuel.add(p);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p == null || lstRefuel == null || lstRefuel.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                lstRefuel.set(index, p);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p == null || lstRefuel == null || lstRefuel.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1) {
                    lstRefuel.remove(index);
                    mKeys.remove(index);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}