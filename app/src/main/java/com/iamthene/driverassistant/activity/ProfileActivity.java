package com.iamthene.driverassistant.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.iamthene.driverassistant.R;

public class ProfileActivity extends AppCompatActivity {

    EditText edtName;
    EditText edtTenDN;
    Button btnThietLap;
    Button btnDangXuat;
    Button btnCapNhat;
    TextView tv;
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

        btnCapNhat.setOnClickListener(view -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Jane Q. User")
                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        });

    }

    /* private void initListener() {
        imgAVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
    }
    private void onClickRequestPermission(){
        DashboardActivity dashboardActivity = (DashboardActivity) getActivity();
        if (dashboardActivity == null){
            return;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            dashboardActivity.openGallery();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            dashboardActivity.openGallery();
        }
        else{
            String [] permisstions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermisstion(permisstions, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }
    public void openGallery(){

    } */

    /* private void setUserInfor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        edtName.setText(user.getDisplayName());
        edtTenDN.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.no_avatar).into(imgAVT);
    } */

    private void init(){
        imgAVT = findViewById(R.id.ivPic);
        edtName = findViewById(R.id.edtTen);
        edtTenDN = findViewById(R.id.edtTenDN);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnThietLap = findViewById(R.id.btnThietLap);
        btnCapNhat = findViewById(R.id.btnCapNhat);
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

        if (name == null){
            edtName.setVisibility(View.GONE);
        } else {
            edtName.setVisibility(View.VISIBLE);
            edtName.setText(name);
        }
        edtName.setText(sharedPreferences.getString("fullName", "Người dùng"));

        edtTenDN.setText(tenDN);
        Glide.with(this).load(photoUrl).error(R.drawable.no_avatar).into(imgAVT);
    }

}