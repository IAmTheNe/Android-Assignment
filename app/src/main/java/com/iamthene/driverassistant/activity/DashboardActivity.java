package com.iamthene.driverassistant.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.fragment.HomeFragment;
import com.iamthene.driverassistant.fragment.NotificationFragment;
import com.iamthene.driverassistant.fragment.ReportFragment;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView mBottomNavigationView;
    ProgressDialog progressDialog;

    private static final int FRAG_HISTORY = 0;
    private static final int FRAG_REPORT = 1;
    private static final int FRAG_ALARM = 2;
    private static final int FRAG_PROFILE = 3;

    private int mCurrentFragment = FRAG_HISTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        inIt();

        // When Home is opened, History Fragment is chosen.
        replaceFragment(new HomeFragment());

        mBottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) {
                if (mCurrentFragment != FRAG_HISTORY) {
                    replaceFragment(new HomeFragment());
                    mCurrentFragment = FRAG_HISTORY;

                }
            } else if (id == R.id.nav_report) {
                if (mCurrentFragment != FRAG_REPORT) {
                    replaceFragment(new ReportFragment());
                    mCurrentFragment = FRAG_REPORT;
                }
            } else if (id == R.id.nav_profile) {


                progressDialog.show();
                progressDialog.setMessage("Đang đăng xuất...");
                SharedPreferences sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                progressDialog.dismiss();
                startActivity(intent);
                finish();
            } else if (id == R.id.nav_alarm) {
                if (mCurrentFragment != FRAG_ALARM) {
                    replaceFragment(new NotificationFragment());
                    mCurrentFragment = FRAG_ALARM;
                }
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frlContent, fragment);
        transaction.commit();
    }


    private void inIt() {
        progressDialog = new ProgressDialog(this);
        mBottomNavigationView = findViewById(R.id.nav_bottom_view);
    }

}