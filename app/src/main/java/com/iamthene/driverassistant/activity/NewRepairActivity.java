package com.iamthene.driverassistant.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.LinhKien;
import com.iamthene.driverassistant.model.Vehicle;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.CarManagerInterface;
import com.iamthene.driverassistant.presenter.CarPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewRepairActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, CarManagerInterface.AddCar {
    MaterialToolbar toolbar;
    EditText etDateRepair, etTimeRepair, etPartRepaired, etPriceRepair;
    MaterialAutoCompleteTextView etCarNameOption3;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _mRef;
    List<VehicleDetail> lstVehicle;
    ArrayAdapter<VehicleDetail> adapter;
    CarPresenter mCarPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_repair);

        inIt();
        getCar();
        getDate();
        getTime();
    }

    private void inIt() {
        toolbar = findViewById(R.id.toolbarOption3);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        etDateRepair = findViewById(R.id.etDateRepair);
        etTimeRepair = findViewById(R.id.etTimeRepair);
        etPartRepaired = findViewById(R.id.etPartRepaired);
        etPriceRepair = findViewById(R.id.etPriceRepair);
        etCarNameOption3 = findViewById(R.id.etCarNameOption3);
        mCarPresenter = new CarPresenter(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuSave) {
            LinhKien linhKien = new LinhKien();
            linhKien.setDate(etDateRepair.getText().toString());
            linhKien.setTime(etTimeRepair.getText().toString());
            linhKien.setPart(etPartRepaired.getText().toString());
            linhKien.setCarId(etCarNameOption3.getText().toString());
            linhKien.setPrice(Integer.parseInt(etPriceRepair.getText().toString()));

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                _mRef = mDatabase.getReference("Repair");
                String keys = Objects.requireNonNull(_mRef.push().getKey());
                _mRef.child(user.getUid()).child(keys).setValue(linhKien);
            }

            finish();
        }

        return false;
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String getDate = day + "/" + month + "/" + year;
        etDateRepair.setText(getDate);

        etDateRepair.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewRepairActivity.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = day1 + "/" + month1 + "/" + year1;
                etDateRepair.setText(date);
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void getTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String now = hour + ":" + minute;
        etTimeRepair.setText(now);

        etTimeRepair.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewRepairActivity.this, (view, hourOfDay, minute1) -> {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute1);
                etTimeRepair.setText(format.format(calendar.getTime()));
            }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void getCar() {
        lstVehicle = mCarPresenter.getOwnerCar();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lstVehicle);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        etCarNameOption3.setAdapter(adapter);
    }

    @Override
    public void addSuccess() {

    }

    @Override
    public void addError(VehicleDetail v) {

    }
}