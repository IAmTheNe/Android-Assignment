package com.iamthene.driverassistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
import com.iamthene.driverassistant.adapter.RefuelAdapter;
import com.iamthene.driverassistant.model.Refuel;
import com.iamthene.driverassistant.utils.ExportFile;

import java.util.ArrayList;
import java.util.List;

public class RefuelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    FrameLayout frlContent;
    MaterialToolbar toolbar;
    RecyclerView rvRefuel;
    List<Refuel> lstRefuel;
    LinearLayout lyEmpty;
    RefuelAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    List<String> mKeys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);
        inIt();
        getData();
        adapter = new RefuelAdapter(lstRefuel, this);
        rvRefuel.setAdapter(adapter);
    }


    private void inIt() {
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        rvRefuel = findViewById(R.id.rvRefuel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRefuel.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        lyEmpty = findViewById(R.id.lyEmpty);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewRefuelActivity.class);
            startActivity(intent);
        }
        if (id == R.id.mnuExport) {
            if (lstRefuel.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Xuất file")
                        .setMessage("Hiện tại không có dữ liệu để xuất file")
                        .setNegativeButton("OK", null)
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                ExportFile exportFile = new ExportFile();
                exportFile.exportRefuel(this, "Thống kê các lần đổ xăng", lstRefuel);
            }
        }
        return false;
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

                if (lstRefuel.isEmpty()) {
                    lyEmpty.setVisibility(View.VISIBLE);
                    rvRefuel.setVisibility(View.GONE);
                } else {
                    lyEmpty.setVisibility(View.GONE);
                    rvRefuel.setVisibility(View.VISIBLE);
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
                if (lstRefuel.isEmpty()) {
                    lyEmpty.setVisibility(View.VISIBLE);
                    rvRefuel.setVisibility(View.GONE);
                } else {
                    lyEmpty.setVisibility(View.GONE);
                    rvRefuel.setVisibility(View.VISIBLE);
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