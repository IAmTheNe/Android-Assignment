package com.iamthene.driverassistant.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Alarm;
import com.iamthene.driverassistant.presenter.AlarmInterface;
import com.iamthene.driverassistant.presenter.AlarmPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewAlarmActivity extends AppCompatActivity implements AlarmInterface.AddAlarmValue {
    TextInputLayout tilTitle, tilDesc, tilType, tilDate;
    EditText etTitle, etDesc, etType, etDate;
    MaterialToolbar toolbar;
    AlarmPresenter mAlarmPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        inIt();
        inItEvent();
    }

    private void inItEvent() {
        // Lấy thời gian
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(NewAlarmActivity.this, (view, year1, month1, dayOfMonth) -> {
                month1 = month1 + 1;
                String mm = String.valueOf(month1);
                if (mm.length() == 1) {
                    mm = "0" + mm;
                }
                String date = dayOfMonth + "/" + mm + "/" + year1;
                etDate.setText(date);
            }, year, month, day);

            datePickerDialog.show();
        });

        //
        etType.setOnClickListener(v -> {
            String[] list = {"Đổ xăng", "Thay nhớt", "Thay linh kiện", "Mua bảo hiểm xe"};
            final String[] type = {""};
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(NewAlarmActivity.this);
            alertDialogBuilder.setTitle(getResources().getText(R.string.app_name));
            alertDialogBuilder.setNegativeButton("Hủy", null);
            alertDialogBuilder.setPositiveButton("Xác nhận", (dialog, which) -> etType.setText(type[0]));
            alertDialogBuilder.setSingleChoiceItems(list, 0, (dialog, which) -> type[0] = list[which]);
            alertDialogBuilder.show();
        });
    }

    private void inIt() {
        tilDate = findViewById(R.id.tilDate);
        tilTitle = findViewById(R.id.tilTitle);
        tilDesc = findViewById(R.id.tilDescription);
        tilType = findViewById(R.id.tilType);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etType = findViewById(R.id.etType);
        etDate = findViewById(R.id.etDate);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            setData();
            return false;
        });
        mAlarmPresenter = new AlarmPresenter(this);
    }

    private void setData() {
        Alarm alarm = new Alarm();
        alarm.setTitle(etTitle.getText().toString());
        alarm.setDesc(etDesc.getText().toString());
        alarm.setAlarmDate(etDate.getText().toString());
        alarm.setType(etType.getText().toString());
        alarm.setCreatedAt(new SimpleDateFormat("dd/MM/yyyy", new Locale("vi")).format(new Date()));
        alarm.setChecked(false);
        mAlarmPresenter.addAlarm(alarm);
    }


    @Override
    public void addSuccess() {
        finish();
    }

    @Override
    public void addErrorMessage(Alarm alarm) {
        if (alarm.isEmptyTitle()) {
            tilTitle.setError(getResources().getString(R.string.not_null));
        }

        if (alarm.isEmptyDesc()) {
            tilDesc.setError(getResources().getString(R.string.not_null));
        }

        if (alarm.isEmptyType()) {
            tilType.setError(getResources().getString(R.string.not_null));
        }

        if (alarm.isEmptyAlarmDate()) {
            tilDate.setError(getResources().getString(R.string.not_null));
        }
    }
}