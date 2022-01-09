package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.fragment.AlarmFragment;
import com.iamthene.driverassistant.fragment.NoAlarmFragment;
import com.iamthene.driverassistant.presenter.AlarmInterface;
import com.iamthene.driverassistant.presenter.AlarmPresenter;

public class AlarmActivity extends AppCompatActivity implements AlarmInterface.OnEmptyAlarm {
    MaterialToolbar toolbar;
    FrameLayout frlContent;
    FloatingActionButton fabAdd;
    AlarmPresenter mAlarmPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        inIt();
        inItEvent();
        mAlarmPresenter.isEmptyList();
    }

    private void inItEvent() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AlarmActivity.this, NewAlarmActivity.class);
            startActivity(intent);
        });
    }

    private void inIt() {
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        fabAdd = findViewById(R.id.fabAdd);
        mAlarmPresenter = new AlarmPresenter(this);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frlContent, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void exists() {
        replaceFragment(new AlarmFragment());
    }

    @Override
    public void empty() {
        replaceFragment(new NoAlarmFragment());
    }
}