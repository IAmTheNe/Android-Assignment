package com.iamthene.driverassistant.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Repair;
import com.iamthene.driverassistant.model.VehicleDetail;
import com.iamthene.driverassistant.presenter.AddCarPresenter;
import com.iamthene.driverassistant.presenter.CarManagerInterface;
import com.iamthene.driverassistant.presenter.RepairInterface;
import com.iamthene.driverassistant.presenter.RepairPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewRepairActivity extends AppCompatActivity
        implements Toolbar.OnMenuItemClickListener, CarManagerInterface.AddCar, RepairInterface.OnAddRepair {
    MaterialToolbar toolbar;
    EditText etDateRepair, etTimeRepair, etPartRepaired, etPriceRepair;
    TextInputLayout inpDateRepair, inpTimeRepair, inpPartRepair, inpPriceRepair;
    MaterialAutoCompleteTextView etCarNameOption3;
    List<VehicleDetail> lstVehicle;
    ArrayAdapter<VehicleDetail> adapter;
    AddCarPresenter mAddCarPresenter;
    RepairPresenter mRepairPresenter;


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
        inpDateRepair = findViewById(R.id.inpDateRepair);
        inpPartRepair = findViewById(R.id.inpPartRepair);
        inpPriceRepair = findViewById(R.id.inpPriceRepair);
        inpTimeRepair = findViewById(R.id.inpTimeRepair);
        mAddCarPresenter = new AddCarPresenter(this);
        mRepairPresenter = new RepairPresenter(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuSave) {
            Repair repair = new Repair();
            repair.setDate(etDateRepair.getText().toString());
            repair.setTime(etTimeRepair.getText().toString());
            repair.setPart(etPartRepaired.getText().toString());
            repair.setCarId(etCarNameOption3.getText().toString());
            if (etPriceRepair.getText().toString().isEmpty()) {
                repair.setPrice(0);
            } else {
                repair.setPrice(Integer.parseInt(etPriceRepair.getText().toString()));
            }

            mRepairPresenter.addRepair(repair);
        }

        return false;
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String getDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
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

        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
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
    public void addFailed(Repair repair) {
        if (repair.isEmptyPart()) {
            inpPartRepair.setError(getResources().getString(R.string.not_null));
        }

        if (repair.isEmptyPrice()) {
            etPriceRepair.setText(0);
        }
    }

    @Override
    public void addError(VehicleDetail v) {

    }
}