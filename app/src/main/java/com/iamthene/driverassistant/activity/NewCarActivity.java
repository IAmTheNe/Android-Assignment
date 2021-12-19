package com.iamthene.driverassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Vehicle;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.CarPresenter;
import com.iamthene.driverassistant.presenter.CarManagerInterface;

import java.util.List;

public class NewCarActivity extends AppCompatActivity implements CarManagerInterface.AddCar {
    EditText etCarName, etCarPlate, etCarBrand, etCapacity, etCarKM;
    AutoCompleteTextView etCarType;
    Button btnCreate;
    CarPresenter mCarPresenter;
    TextInputLayout tilCarName, tilCarType, tilCarBrand, tilCapacity, tilCarKM, tilCarPlate;
    int positionVehicle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        inIt();
        getCar();

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

        etCarType.setOnItemClickListener((parent, view, position, id) -> positionVehicle = position);
        vehicle.setType((Vehicle) etCarType.getAdapter().getItem(positionVehicle));
        mCarPresenter.addCar(vehicle);

    }

    private void inIt() {
        etCarName = findViewById(R.id.etCarName);
        etCarType = findViewById(R.id.etCarType);
        etCarPlate = findViewById(R.id.etCarPlate);
        etCarBrand = findViewById(R.id.etCarBrand);
        etCapacity = findViewById(R.id.etCapacity);
        etCarKM = findViewById(R.id.etCarKM);
        btnCreate = findViewById(R.id.btnCreate);
        tilCarName = findViewById(R.id.tilCarName);
        tilCapacity = findViewById(R.id.tilCapacity);
        tilCarBrand = findViewById(R.id.tilBrand);
        tilCarKM = findViewById(R.id.tilCarKM);
        tilCarType = findViewById(R.id.tilCarType);
        tilCarPlate = findViewById(R.id.tilCarPlate);
        mCarPresenter = new CarPresenter(this);
    }

    @Override
    public void addSuccess() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void addError(VehicleDetail vehicleDetail) {
        if (vehicleDetail.isEmptyPlate()) {
            tilCarPlate.setError(getResources().getString(R.string.not_null));
        }

        if (vehicleDetail.isEmptyBrand()) {
            tilCarBrand.setError(getResources().getString(R.string.not_null));
        }

        if (vehicleDetail.isVehicleName()) {
            tilCarName.setError(getResources().getString(R.string.not_null));
        }

        if (etCarKM.getText().toString().isEmpty()) {
            tilCarKM.setError(getResources().getString(R.string.not_null));
        }

        if (etCarType.getText().toString().isEmpty()) {
            tilCarType.setError(getResources().getString(R.string.not_null));
        }
    }

    private void getCar() {
        List<Vehicle> lstVehicle = mCarPresenter.getCar();
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, lstVehicle);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        etCarType.setAdapter(adapter);
    }
}