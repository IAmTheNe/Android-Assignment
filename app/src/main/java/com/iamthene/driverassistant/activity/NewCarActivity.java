package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Vehicle;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.AddCarPresenter;
import com.iamthene.driverassistant.presenter.CarManagerInterface;

public class NewCarActivity extends AppCompatActivity implements CarManagerInterface.AddCar {
    EditText etCarName, etCarType, etCarPlate, etCarBrand, etCapacity, etCarKM;
    Button btnCreate;
    AddCarPresenter mAddCarPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        inIt();

        btnCreate.setOnClickListener(v -> {
            addCar();
        });
    }

    private void addCar() {
        VehicleDetail vehicle = new VehicleDetail();
        vehicle.setName(etCarName.getText().toString());
        vehicle.setBrand(etCarBrand.getText().toString());
        vehicle.setCurrentKM(Integer.parseInt(etCarKM.getText().toString()));
        vehicle.setNumber(etCarPlate.getText().toString());

        mAddCarPresenter.addCar(vehicle);
    }

    private void inIt() {
        etCarName = findViewById(R.id.etCarName);
        etCarType = findViewById(R.id.etCarType);
        etCarPlate = findViewById(R.id.etCarPlate);
        etCarBrand = findViewById(R.id.etCarBrand);
        etCapacity = findViewById(R.id.etCapacity);
        etCarKM = findViewById(R.id.etCarKM);
        btnCreate = findViewById(R.id.btnCreate);
        mAddCarPresenter = new AddCarPresenter(this);
    }

    @Override
    public void addSuccess() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void addError() {

    }
}