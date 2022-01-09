package com.iamthene.driverassistant.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.fragment.NoAlarmFragment;

public class AlarmActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    FrameLayout frlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        inIt();
        inItEvent();
        replaceFragment(new NoAlarmFragment());
    }

    private void inItEvent() {
    }

    private void inIt() {
        frlContent = findViewById(R.id.frlContent);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
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
}