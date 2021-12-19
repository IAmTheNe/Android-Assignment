package com.iamthene.driverassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iamthene.driverassistant.R;

public class ProfileActivity extends AppCompatActivity {

    EditText edtName;
    EditText edtTenDN;
    Button btnThietLap;
    Button btnDangXuat;
    int img;

    private ImageView imgAVT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        showUserInfo();
        btnDangXuat.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void init(){
        imgAVT = findViewById(R.id.ivPic);
        edtName = findViewById(R.id.edtTen);
        edtTenDN = findViewById(R.id.edtTenDN);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnThietLap = findViewById(R.id.btnThietLap);
    }

    private void showUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        // Name, email address, and profile photo Url
        String name = user.getDisplayName();
        String tenDN = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        /* if (name == null){
            edtName.setVisibility(View.GONE);
        } else {
            edtName.setVisibility(View.VISIBLE);
            edtName.setText(name);
        } */
        edtName.setText(sharedPreferences.getString("fullName", "Người dùng"));

        edtTenDN.setText(tenDN);
        Glide.with(this).load(photoUrl).error(R.drawable.no_avatar).into(imgAVT);
    }

}