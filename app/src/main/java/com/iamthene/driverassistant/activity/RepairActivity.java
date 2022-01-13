package com.iamthene.driverassistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.iamthene.driverassistant.adapter.RepairAdapter;
import com.iamthene.driverassistant.model.Repair;
import com.iamthene.driverassistant.utils.ExportFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepairActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    MaterialToolbar toolbar;
    LinearLayout lyEmpty;
    RecyclerView rvPartChange;
    RepairAdapter adapter;
    List<Repair> lstLK;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    ArrayList<String> mKeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        inIt();
        getData();
        adapter = new RepairAdapter(this, lstLK);
        rvPartChange.setAdapter(adapter);
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbarOption3);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        rvPartChange = findViewById(R.id.rvPartChange);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPartChange.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        lyEmpty = findViewById(R.id.lyEmpty);
    }

    private void getData() {
        lstLK = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        _myRef = mDatabase.getReference("Repair");
        assert user != null;
        _myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Repair repair = snapshot.getValue(Repair.class);
                if (repair != null) {
                    lstLK.add(repair);
                    mKeys.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }

                if (lstLK.isEmpty()) {
                    lyEmpty.setVisibility(View.VISIBLE);
                    rvPartChange.setVisibility(View.GONE);
                } else {
                    lyEmpty.setVisibility(View.GONE);
                    rvPartChange.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Repair repair = snapshot.getValue(Repair.class);
                if (repair == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                lstLK.set(index, repair);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Repair repair = snapshot.getValue(Repair.class);
                if (repair == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                if (index != -1) {
                    lstLK.remove(index);
                    mKeys.remove(index);
                }

                if (lstLK.isEmpty()) {
                    lyEmpty.setVisibility(View.VISIBLE);
                    rvPartChange.setVisibility(View.GONE);
                } else {
                    lyEmpty.setVisibility(View.GONE);
                    rvPartChange.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewRepairActivity.class);
            startActivity(intent);
        }
        if (id == R.id.mnuExport) {
            if (lstLK.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Xuất file")
                        .setMessage("Hiện tại không có dữ liệu để xuất file")
                        .setNegativeButton("OK", null)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                ExportFile exportFile = new ExportFile();
                exportFile.exportRepair(this, "Thống kê thay linh kiện", lstLK);
            }
        }
        return false;
    }
}