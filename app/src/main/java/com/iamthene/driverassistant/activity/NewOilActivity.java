package com.iamthene.driverassistant.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Oil;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.AddCarPresenter;
import com.iamthene.driverassistant.presenter.CarManagerInterface;
import com.iamthene.driverassistant.presenter.OilInterface;
import com.iamthene.driverassistant.presenter.OilPresenter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewOilActivity extends AppCompatActivity implements CarManagerInterface.AddCar, OilInterface.OnAddOil {
    MaterialToolbar toolbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText etFeeOil, etPlaceOil, etTimeOil;
    TextInputLayout inpFeeOil, inpPlaceOil, inpTimeOil;
    MaterialAutoCompleteTextView etCarNameOption3;
    List<VehicleDetail> lstVehicle;
    ArrayAdapter<VehicleDetail> adapter;
    AddCarPresenter mAddCarPresenter;
    OilPresenter mOilPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_oil);

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

        etTimeOil.setText(date);
    }

    private void setData() {
        Oil o = new Oil();
        o.setFeeOil(etFeeOil.getText().toString());
        o.setPlaceOil(etPlaceOil.getText().toString());
        o.setTimeOil(etTimeOil.getText().toString());
        o.setCarName(etCarNameOption3.getText().toString());
        mOilPresenter.addOil(o);
    }

    public void init() {
        etFeeOil = findViewById(R.id.etFeeOil);
        etPlaceOil = findViewById(R.id.etPlaceOil);
        etTimeOil = findViewById(R.id.etTimeOil);
        inpTimeOil = findViewById(R.id.inpTimeOil);
        inpFeeOil = findViewById(R.id.inpFeeOil);
        inpPlaceOil = findViewById(R.id.inpPlaceOil);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            setData();
            return false;
        });
        mAddCarPresenter = new AddCarPresenter(this);
        mOilPresenter = new OilPresenter(this);
        etCarNameOption3 = findViewById(R.id.etCarNameOption3);
    }

    private void getCar() {
        lstVehicle = mAddCarPresenter.getOwnerCar();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lstVehicle);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        etCarNameOption3.setAdapter(adapter);
    }

    @Override
    public void addSuccess() {
        finish();
    }

    @Override
    public void addFailed(Oil oil) {
        if (oil.isEmptyPlace()) {
            inpPlaceOil.setError(getResources().getString(R.string.not_null));
        }

        if (oil.isEmptyFee()) {
            inpFeeOil.setError(getResources().getString(R.string.not_null));
        }
    }

    @Override
    public void addError(VehicleDetail v) {

    }
}