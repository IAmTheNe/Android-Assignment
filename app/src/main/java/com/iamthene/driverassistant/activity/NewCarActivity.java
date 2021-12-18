package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.iamthene.driverassistant.R;

public class NewCarActivity extends AppCompatActivity {
    EditText etCarName, etCarType, etCarPlate, etCarBrand, etCapacity, etCarKM;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        inIt();

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(NewCarActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }

    private void inIt() {
        etCarName = findViewById(R.id.etCarName);
        etCarType = findViewById(R.id.etCarType);
        etCarPlate = findViewById(R.id.etCarPlate);
        etCarBrand = findViewById(R.id.etCarBrand);
        etCapacity = findViewById(R.id.etCapacity);
        etCarKM = findViewById(R.id.etCarKM);
        btnCreate = findViewById(R.id.btnCreate);
    }
}