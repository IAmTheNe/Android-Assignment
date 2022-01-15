package com.iamthene.driverassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.AlarmActivity;
import com.iamthene.driverassistant.activity.DashboardActivity;
import com.iamthene.driverassistant.activity.FindGasStationActivity;
import com.iamthene.driverassistant.activity.OilActivity;
import com.iamthene.driverassistant.activity.ProfileActivity;
import com.iamthene.driverassistant.activity.RefuelActivity;
import com.iamthene.driverassistant.activity.RepairActivity;
import com.iamthene.driverassistant.activity.SOSActivity;
import com.iamthene.driverassistant.activity.WeatherActivity;
import com.iamthene.driverassistant.model.Alarm;
import com.iamthene.driverassistant.presenter.UserManagerPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    CardView cvLinhKien, cvDoXang, cvThayNhot;
    UserManagerPresenter userManagerPresenter;
    TextView tvYourName;
    ImageView civAvatar;
    FloatingActionButton profile, fabWeather, fabAlarm, faFindGasStation, fabEmergencyCall;
    BottomNavigationView bottomNavigationView = DashboardActivity.mBottomNavigationView;
    List<Alarm> lstAlarm;
    List<String> mKeys = new ArrayList<>();

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
        setAlarm();
    }

    private void setAlarm() {
        lstAlarm = new ArrayList<>();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference _myRef = mDatabase.getReference("Alarm");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference dr = _myRef.child(user.getUid());
            dr.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Alarm alarm = snapshot.getValue(Alarm.class);
                    if (alarm == null) {
                        return;
                    }
                    lstAlarm.add(alarm);
                    mKeys.add(snapshot.getKey());
                    String today = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi")).format(new Date());
                    Log.e("Get Checked", alarm.isChecked() + "");
                    if (today.equals(alarm.getAlarmDate()) && !alarm.isChecked()) {
                        new AlertDialog.Builder(requireActivity())
                                .setTitle("Nhắc hẹn hôm nay")
                                .setMessage("Hôm nay bạn có việc cần làm! Vào thông báo để xem chi tiết!")
                                .setPositiveButton("Chuyển đến", (dialog, which) -> {
                                    bottomNavigationView.setSelectedItemId(R.id.nav_alarm);
                                    String keys = snapshot.getKey();
                                    if (keys != null) {
                                        dr.child(keys).child("checked").setValue(true);
                                    }
                                })
                                .setNegativeButton("Nhắc tôi sau", null)
                                .show();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Alarm alarm = snapshot.getValue(Alarm.class);
                    if (alarm == null || lstAlarm == null || lstAlarm.isEmpty()) {
                        return;
                    }
                    String key = snapshot.getKey();
                    int index = mKeys.indexOf(key);
                    lstAlarm.set(index, alarm);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Alarm alarm = snapshot.getValue(Alarm.class);
                    if (alarm == null || lstAlarm == null || lstAlarm.isEmpty()) {
                        return;
                    }
                    String key = snapshot.getKey();
                    int index = mKeys.indexOf(key);
                    if (index != -1) {
                        lstAlarm.remove(index);
                        mKeys.remove(index);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
        faFindGasStation = view.findViewById(R.id.faFindGasStation);
        tvYourName = view.findViewById(R.id.tvYourName);
        civAvatar = view.findViewById(R.id.civAvatar);
        userManagerPresenter = new UserManagerPresenter();
        profile = view.findViewById(R.id.profile);
        fabWeather = view.findViewById(R.id.fabWeather);
        fabAlarm = view.findViewById(R.id.fabAlarm);
        fabEmergencyCall = view.findViewById(R.id.fabEmergencyCall);
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

        faFindGasStation.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), FindGasStationActivity.class);
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
        fabEmergencyCall.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SOSActivity.class);
            startActivity(intent);
        });
    }
}
