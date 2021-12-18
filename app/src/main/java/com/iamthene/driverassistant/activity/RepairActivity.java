package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.iamthene.driverassistant.adapter.LinhKienAdapter;
import com.iamthene.driverassistant.model.LinhKien;

import java.util.ArrayList;
import java.util.List;

public class RepairActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    RecyclerView rvPartChange;
    MaterialToolbar toolbar;
    LinhKienAdapter adapter;
    List<LinhKien> lstLK;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    ArrayList<String> mKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        inIt();
        getData();
        adapter = new LinhKienAdapter(this, lstLK);
        rvPartChange.setAdapter(adapter);
    }

    private void inIt() {
        rvPartChange = findViewById(R.id.rvPartChange);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPartChange.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        toolbar = findViewById(R.id.toolbarOption3);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewRepairActivity.class);
            startActivity(intent);
        }

        return false;
    }

    private void getData() {
        lstLK = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        _myRef = mDatabase.getReference("Repair");
        assert user != null;
        _myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien != null) {
                    lstLK.add(linhKien);
                    mKeys.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                lstLK.set(index, linhKien);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                if (index != -1) {
                    lstLK.remove(index);
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