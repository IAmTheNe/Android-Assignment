package com.iamthene.driverassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

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
import com.iamthene.driverassistant.fragment.EmptyFragment;
import com.iamthene.driverassistant.fragment.RefuelFragment;
import com.iamthene.driverassistant.model.Refuel;
import com.iamthene.driverassistant.presenter.RefuelInterface;
import com.iamthene.driverassistant.presenter.RefuelPresenter;

import java.util.ArrayList;
import java.util.List;

public class RefuelActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, RefuelInterface.OnCheckEmptyList {
    FrameLayout frlContent;
    MaterialToolbar toolbar;
    RefuelPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);
        inIt();
        inItEvent();
    }

    private void inItEvent() {
        mPresenter.isEmptyList();
    }

    private void inIt() {
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        mPresenter = new RefuelPresenter(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(this, NewRefuelActivity.class);
            startActivity(intent);
        }
        return false;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frlContent, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void empty() {
        replaceFragment(new EmptyFragment());
    }

    @Override
    public void exists() {
        replaceFragment(new RefuelFragment());
    }
}