package com.iamthene.driverassistant.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.iamthene.driverassistant.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewPartChangeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    MaterialToolbar toolbar;
    EditText etDateRepair, etTimeRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_part_change);

        inIt();
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(NewPartChangeActivity.this, (view, year1, month1, day1) -> {
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

        etTimeRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewPartChangeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        calendar.set(0, 0, 0, hourOfDay, minute);
                        etTimeRepair.setText(format.format(calendar.getTime()));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }
}