package com.iamthene.driverassistant.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Refuel;

public class DetailRefuelActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    TextView tvPlace, tvFuel, tvFee, tvTime, tvKm, tvDk;
    Button btnDelete, btnClose;
    Refuel p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_refuel);

        inIt();
        getData();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDelete(p);
            }
        });
    }

    private void onClickDelete(Refuel p) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
        new AlertDialog.Builder(this)
                .setTitle("Driver Assistant")
                .setMessage("Do you ready want to delete this ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            myRef = database.getReference("Refuel");
                            myRef.child(user.getUid()).child(p.getId()).removeValue();
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
//        }
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        p = (Refuel) bundle.get("refuel");
        tvPlace.setText(p.getPlace());
        tvFuel.setText(p.getFuel());
        tvFee.setText(p.getFee());
        tvTime.setText(p.getTime());
        tvKm.setText(p.getDistance());
        tvDk.setText(p.getExpDistance());
    }

    public void inIt() {
        tvPlace = findViewById(R.id.tvPlace);
        tvFuel = findViewById(R.id.tvFuel);
        tvFee = findViewById(R.id.tvFee);
        tvTime = findViewById(R.id.tvTime);
        tvKm = findViewById(R.id.tvKm);
        tvDk = findViewById(R.id.tvDk);
        btnDelete = findViewById(R.id.btnDelete);
        btnClose = findViewById(R.id.btnClose);
    }

}