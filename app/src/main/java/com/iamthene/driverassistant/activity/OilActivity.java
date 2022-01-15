package com.iamthene.driverassistant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.iamthene.driverassistant.adapter.OilAdapter;
import com.iamthene.driverassistant.model.Oil;
import com.iamthene.driverassistant.utils.ExportFile;
import com.iamthene.driverassistant.utils.ExportFileOil;

import java.util.ArrayList;
import java.util.List;

public class OilActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    RecyclerView rvOil;
    List<Oil> lstOil;
    OilAdapter adapter;
    LinearLayout lyEmpty;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    List<String> mKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        inIt();
        getData();
        adapter = new OilAdapter(lstOil, this);
        rvOil.setAdapter(adapter);
    }

    private void getData() {
        lstOil = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference("Oil");
        if (user != null) {
            myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Oil o = snapshot.getValue(Oil.class);
                    if (o != null) {
                        lstOil.add(o);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }

                    if (lstOil.isEmpty()) {
                        lyEmpty.setVisibility(View.VISIBLE);
                        rvOil.setVisibility(View.GONE);
                    } else {
                        lyEmpty.setVisibility(View.GONE);
                        rvOil.setVisibility(View.VISIBLE);
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
                    if (lstOil.isEmpty()) {
                        lyEmpty.setVisibility(View.VISIBLE);
                        rvOil.setVisibility(View.GONE);
                    } else {
                        lyEmpty.setVisibility(View.GONE);
                        rvOil.setVisibility(View.VISIBLE);
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
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mnuAdd) {
                Intent intent = new Intent(OilActivity.this, NewOilActivity.class);
                startActivity(intent);
            }
            if (id == R.id.mnuExport) {
                if (lstOil.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Xuất file")
                            .setMessage("Hiện tại không có dữ liệu để xuất file")
                            .setNegativeButton("OK", null)
                            .show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                    ExportFileOil exportFileOil = new ExportFileOil();
                    exportFileOil.exportOil(this, "Thống kê thay nhớt", lstOil);
                }
            }
            return false;
        });
        rvOil = findViewById(R.id.rvListOil);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOil.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        lyEmpty = findViewById(R.id.lyEmpty);
    }
}