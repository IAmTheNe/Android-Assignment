package com.iamthene.driverassistant.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Oil;

public class DetailOilActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    TextView tvPlaceOil, tvFeeOil, tvTimeOil;
    Button btnDeleteOil, btnCloseOil;
    Oil o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_oil);

        init();
        getData();

        btnCloseOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDeleteOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDelete(o);
            }
        });
    }

    private void onClickDelete(Oil o) {
        new AlertDialog.Builder(this)
                .setTitle("Driver Assistant")
                .setMessage("Do you readly want to delete this ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            myRef = database.getReference("Oil");
                            myRef.child(user.getUid()).child(o.getIdOil()).removeValue();
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        o = (Oil) bundle.get("oil");
        tvPlaceOil.setText(o.getPlaceOil());
        tvFeeOil.setText(o.getFeeOil());
        tvTimeOil.setText(o.getTimeOil());
    }

    public void init() {
        tvPlaceOil = findViewById(R.id.tvPlaceOil);
        tvFeeOil = findViewById(R.id.tvFeeOil);
        tvTimeOil = findViewById(R.id.tvTimeOil);
        btnDeleteOil = findViewById(R.id.btnDeleteOil);
        btnCloseOil = findViewById(R.id.btnCloseOil);
    }

}
