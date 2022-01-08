package com.iamthene.driverassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Oil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewOilActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText etFeeOil, etPlaceOil, etTimeOil;
    Oil o;
    Button btnAddOil, btnCancelOil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_oil);

        init();

        String date = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            date = time.format(formatter);
        }

        etTimeOil.setText(date);

        btnAddOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(o);
                finish();
            }
        });

        btnCancelOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData(Oil o) {
        if (o == null) {
            o = new Oil();
        }

        o.setFeeOil(etFeeOil.getText().toString());
        o.setPlaceOil(etPlaceOil.getText().toString());
        o.setTimeOil(etTimeOil.getText().toString());

        myRef = database.getReference("Oil");
        String id = myRef.push().getKey();
        o.setIdOil(id);
        myRef.child(id).setValue(o);
    }

    public void init() {
        etFeeOil = findViewById(R.id.etFeeOil);
        etPlaceOil = findViewById(R.id.etPlaceOil);
        etTimeOil = findViewById(R.id.etTimeOil);
        btnAddOil = findViewById(R.id.btnSaveOil);
        btnCancelOil = findViewById(R.id.btnCancelOil);
    }
}