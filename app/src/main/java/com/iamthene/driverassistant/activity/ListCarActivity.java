package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.adapter.CarAdapter;
import com.iamthene.driverassistant.model.VehicleDetail;

import java.util.ArrayList;
import java.util.List;

public class ListCarActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    RecyclerView rvListCar;
    MaterialToolbar toolbar;
    CarAdapter adapter;
    List<VehicleDetail> detailList;
    ArrayList<String> mKeys = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_car);
        inIt();
        getData();
        adapter = new CarAdapter(this, detailList);
        rvListCar.setAdapter(adapter);
    }

    private void getData() {
        detailList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        _myRef = mDatabase.getReference("ListCars");
        if (user != null) {
            String uid = user.getUid();
            _myRef.child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    VehicleDetail vd = snapshot.getValue(VehicleDetail.class);
                    if (vd != null) {
                        detailList.add(vd);
                        mKeys.add(_myRef.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    VehicleDetail vd = snapshot.getValue(VehicleDetail.class);
                    if (vd == null || detailList == null || detailList.isEmpty()) {
                        return;
                    }
                    String key = snapshot.getKey();
                    int index = mKeys.indexOf(key);
                    detailList.set(index, vd);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    VehicleDetail vd = snapshot.getValue(VehicleDetail.class);
                    if (vd == null || detailList == null || detailList.isEmpty()) {
                        return;
                    }
                    String key = snapshot.getKey();
                    int index = mKeys.indexOf(key);
                    if (index != -1) {
                        detailList.remove(index);
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

    private void inIt() {
        rvListCar = findViewById(R.id.rvListCar);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListCar.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewCarActivity.class);
            startActivity(intent);
        }
        return false;
    }
}