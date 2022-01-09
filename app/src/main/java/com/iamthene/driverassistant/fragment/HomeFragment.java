package com.iamthene.driverassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.AlarmActivity;
import com.iamthene.driverassistant.activity.OilActivity;
import com.iamthene.driverassistant.activity.ProfileActivity;
import com.iamthene.driverassistant.activity.RefuelActivity;
import com.iamthene.driverassistant.activity.RepairActivity;
import com.iamthene.driverassistant.activity.WeatherActivity;
import com.iamthene.driverassistant.presenter.UserManagerPresenter;

public class HomeFragment extends Fragment {
    CardView cvLinhKien, cvDoXang, cvThayNhot;
    UserManagerPresenter userManagerPresenter;
    TextView tvYourName;
    ImageView civAvatar;
    FloatingActionButton profile, fabWeather, fabAlarm;

    private ImageView img_AVT;

    private EditText edtFirstName, edtLastName, edtEmail;
    final private ProfileActivity mProfileActivity = new ProfileActivity();
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        setEvent();
        getInfo();
        //showUserInfo();
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        tvYourName.setText(sharedPreferences.getString("fullName", "Người dùng"));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photo = user.getPhotoUrl();
        Glide.with(this).load(photo).error(R.drawable.no_avatar).into(civAvatar);
    }

    public void inIt(View view) {
        cvLinhKien = view.findViewById(R.id.cvThayLinhKien);
        cvDoXang = view.findViewById(R.id.cvDoXang);
        cvThayNhot = view.findViewById(R.id.cvThayNhot);
        tvYourName = view.findViewById(R.id.tvYourName);
        civAvatar = view.findViewById(R.id.civAvatar);
        userManagerPresenter = new UserManagerPresenter();
        profile = view.findViewById(R.id.profile);
        edtFirstName = view.findViewById(R.id.edtFirstName);
        edtLastName = view.findViewById(R.id.edtLastName);
        edtEmail = view.findViewById(R.id.edtEmail);
        img_AVT = view.findViewById(R.id.img_AVT);
        fabWeather = view.findViewById(R.id.fabWeather);
        fabAlarm = view.findViewById(R.id.fabAlarm);
    }

    private void setEvent() {
        cvLinhKien.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), RepairActivity.class);
            startActivity(intent);
        });

        cvDoXang.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), RefuelActivity.class);

            startActivity(intent);
        });

        cvThayNhot.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), OilActivity.class);
            startActivity(intent);
        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        fabWeather.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WeatherActivity.class);
            startActivity(intent);
        });
        fabAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AlarmActivity.class);
            startActivity(intent);
        });

    }
}
