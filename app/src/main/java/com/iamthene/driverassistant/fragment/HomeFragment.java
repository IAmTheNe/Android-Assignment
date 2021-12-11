package com.iamthene.driverassistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.PartChangeActivity;
import com.iamthene.driverassistant.presenter.UserManagerPresenter;

public class HomeFragment extends Fragment {
    CardView cvLinhKien;
    UserManagerPresenter userManagerPresenter;
    TextView tvYourName;
    ImageView civAvatar;

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
    }

    private void inIt(View view) {
        cvLinhKien = view.findViewById(R.id.cvThayLinhKien);
        tvYourName = view.findViewById(R.id.tvYourName);
        civAvatar = view.findViewById(R.id.civAvatar);
        userManagerPresenter = new UserManagerPresenter();
    }

    private void setEvent() {
        cvLinhKien.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PartChangeActivity.class);
            startActivity(intent);
        });
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataUser", Context.MODE_PRIVATE);
        tvYourName.setText(sharedPreferences.getString("fullName", "Người dùng"));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uri photo = user.getPhotoUrl();
        Glide.with(this).load(photo).error(R.drawable.no_avatar).into(civAvatar);
    }
}