package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.adapter.LinhKienAdapter;
import com.iamthene.driverassistant.model.LinhKien;

import java.util.List;

public class PartChangeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    RecyclerView rvPartChange;
    MaterialToolbar toolbar;
    LinhKienAdapter adapter;
    List<LinhKien> lstLK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_change);

        inIt();
    }

    private void inIt() {
        rvPartChange = findViewById(R.id.rvPartChange);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPartChange.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        lstLK = LinhKien.init();
        adapter = new LinhKienAdapter(this, lstLK);
        rvPartChange.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbarOption3);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(PartChangeActivity.this, DashboardActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewPartChangeActivity.class);
            startActivity(intent);
        }

        return false;
    }
}