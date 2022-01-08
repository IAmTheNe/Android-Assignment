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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.adapter.OilAdapter;
import com.iamthene.driverassistant.model.Oil;

import java.util.ArrayList;
import java.util.List;

public class OilActivity extends AppCompatActivity {

    RecyclerView rvOil;
    List<Oil> lstOil;
    OilAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ImageButton btnAddOil;
    List<String> mKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);

        rvOil = findViewById(R.id.rvOil);
        btnAddOil = findViewById(R.id.BtnAddOil);
        btnAddOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OilActivity.this, NewOilActivity.class);
                startActivity(i);
            }
        });

        getData();
        rvOil.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OilAdapter(lstOil, OilActivity.this);
        rvOil.setAdapter(adapter);
    }

    private void getData() {
        lstOil = new ArrayList<>();
        myRef = database.getReference("Oil");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Oil o = snapshot.getValue(Oil.class);
                if (o != null) {
                    lstOil.add(o);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Oil o = snapshot.getValue(Oil.class);
                if (o == null || lstOil == null || lstOil.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                lstOil.set(index, o);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Oil o = snapshot.getValue(Oil.class);
                if (o == null || lstOil == null || lstOil.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1) {
                    lstOil.remove(index);
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