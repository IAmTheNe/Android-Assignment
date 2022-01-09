package com.iamthene.driverassistant.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Refuel;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.AddCarPresenter;
import com.iamthene.driverassistant.presenter.CarManagerInterface;
import com.iamthene.driverassistant.presenter.RefuelInterface;
import com.iamthene.driverassistant.presenter.RefuelPresenter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewRefuelActivity extends AppCompatActivity implements RefuelInterface.OnAddRefuel, CarManagerInterface.AddCar {
    EditText etFee, etPlace, etFuel, etTime, etKm, etDk;
    TextInputLayout tilFee, tilPlace, tilFuel, tilTime, tilKm, tilDk;
    MaterialToolbar toolbar;
    RefuelPresenter mRefuelPresenter;
    List<VehicleDetail> lstVehicle;
    ArrayAdapter<VehicleDetail> adapter;
    MaterialAutoCompleteTextView etCarNameOption3;
    AddCarPresenter mAddCarPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refuel);

        init();
        getCar();
        setDate();
    }

    private void setDate() {
        String date = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            date = time.format(formatter);
        }

        etTime.setText(date);
    }

    private void setData() {
        Refuel p = new Refuel();
        p.setFee(etFee.getText().toString());
        p.setPlace(etPlace.getText().toString());
        p.setFuel(etFuel.getText().toString());
        p.setTime(etTime.getText().toString());
        p.setDistance(etKm.getText().toString());
        p.setExpDistance(etDk.getText().toString());
        p.setCarNo(etCarNameOption3.getText().toString());
        mRefuelPresenter.addRefuel(p);
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar);
        etFee = findViewById(R.id.etFee);
        etPlace = findViewById(R.id.etPlace);
        etFuel = findViewById(R.id.etFuel);
        etTime = findViewById(R.id.etTime);
        etKm = findViewById(R.id.etKm);
        etDk = findViewById(R.id.etDk);
        tilDk = findViewById(R.id.inpDk);
        tilFee = findViewById(R.id.inpFee);
        tilFuel = findViewById(R.id.inpFuel);
        tilKm = findViewById(R.id.inpKm);
        tilPlace = findViewById(R.id.inpPlace);
        tilTime = findViewById(R.id.inpTime);
        etCarNameOption3 = findViewById(R.id.etCarNameOption3);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.mnuSave) {
                setData();
            }
            return false;
        });
        mRefuelPresenter = new RefuelPresenter(this);
        mAddCarPresenter = new AddCarPresenter(this);
    }

    @Override
    public void addSuccess() {
        finish();
    }

    @Override
    public void addError(VehicleDetail v) {

    }

    private void getCar() {
        lstVehicle = mAddCarPresenter.getOwnerCar();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lstVehicle);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        etCarNameOption3.setAdapter(adapter);
    }

    @Override
    public void addFailed(Refuel refuel) {
        if (refuel.isEmptyExpDistance()) {
            tilDk.setError(getResources().getString(R.string.not_null));
        }

        if (refuel.isEmptyTime()) {
            tilTime.setError(getResources().getString(R.string.not_null));
        }

        if (refuel.isEmptyDistance()) {
            tilKm.setError(getResources().getString(R.string.not_null));
        }

        if (refuel.isEmptyFuel()) {
            tilFuel.setError(getResources().getString(R.string.not_null));
        }

        if (refuel.isEmptyPlace()) {
            tilPlace.setError(getResources().getString(R.string.not_null));
        }

        if (refuel.isEmptyFee()) {
            tilFee.setError(getResources().getString(R.string.not_null));
        }
    }
}