package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.fragment.EmptyFragment;
import com.iamthene.driverassistant.fragment.OilFragment;
import com.iamthene.driverassistant.presenter.OilInterface;
import com.iamthene.driverassistant.presenter.OilPresenter;

public class OilActivity extends AppCompatActivity implements OilInterface.OnCheckEmptyList {
    OilPresenter mOilPresenter;
    FrameLayout frlContent;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        inIt();
        inItEvent();
    }

    private void inItEvent() {
        mOilPresenter.isEmptyList();
    }

    private void inIt() {
        mOilPresenter = new OilPresenter(this);
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mnuAdd) {
                Intent intent = new Intent(OilActivity.this, NewOilActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frlContent, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void exists() {
        replaceFragment(new OilFragment());
    }

    @Override
    public void empty() {
        replaceFragment(new EmptyFragment());
    }
}