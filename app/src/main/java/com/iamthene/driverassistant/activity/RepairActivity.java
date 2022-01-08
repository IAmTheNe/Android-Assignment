package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.fragment.EmptyFragment;
import com.iamthene.driverassistant.fragment.RepairFragment;
import com.iamthene.driverassistant.model.LinhKien;
import com.iamthene.driverassistant.presenter.CarPresenter;
import com.iamthene.driverassistant.presenter.RepairInterface;
import com.iamthene.driverassistant.presenter.RepairPresenter;

import java.util.ArrayList;
import java.util.List;

public class RepairActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, RepairInterface.GetRepairList {
    MaterialToolbar toolbar;
    FrameLayout frlContent;
    RepairPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        inIt();
        initEvent();
    }

    private void inIt() {
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbarOption3);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        mPresenter = new RepairPresenter(this);
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frlContent, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void initEvent() {
        mPresenter.fragmentTransactions();
    }

    @Override
    public void onSuccess() {
        replaceFragment(new EmptyFragment());
    }

    @Override
    public void onFail() {
        replaceFragment(new RepairFragment());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}