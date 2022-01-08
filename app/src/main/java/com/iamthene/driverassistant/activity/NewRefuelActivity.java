package com.iamthene.driverassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Refuel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class NewRefuelActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    EditText etFee, etPlace, etFuel, etTime, etKm, etDk;
    Refuel p;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refuel);

        init();

        String date = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            date = time.format(formatter);
        }

        etTime.setText(date);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(p);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData(Refuel p) {
        if (p == null) {
            p = new Refuel();
        }

        p.setFee(etFee.getText().toString());
        p.setPlace(etPlace.getText().toString());
        p.setFuel(etFuel.getText().toString());
        p.setTime(etTime.getText().toString());
        p.setDistance(etKm.getText().toString());
        p.setExpDistance(etDk.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            myRef = database.getReference("Refuel");
            String keys = Objects.requireNonNull(myRef.push().getKey());
            myRef.child(user.getUid()).child(keys).setValue(p);
        }
    }

    public void init() {
        etFee = findViewById(R.id.etFee);
        etPlace = findViewById(R.id.etPlace);
        etFuel = findViewById(R.id.etFuel);
        etTime = findViewById(R.id.etTime);
        etKm = findViewById(R.id.etKm);
        etDk = findViewById(R.id.etDk);
        btnAdd = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }
}