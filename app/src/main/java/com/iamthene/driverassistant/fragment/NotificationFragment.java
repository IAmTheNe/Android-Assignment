package com.iamthene.driverassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.DashboardActivity;
import com.iamthene.driverassistant.adapter.AlarmAdapter;
import com.iamthene.driverassistant.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationFragment extends Fragment {
    MaterialToolbar toolbar;
    LinearLayout lyEmpty;
    RecyclerView rvAlarm;
    AlarmAdapter adapter;
    ArrayList<String> mKeys = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    List<Alarm> lstAlarm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        getData();
        adapter = new AlarmAdapter(getContext(), lstAlarm);
        rvAlarm.setAdapter(adapter);
    }

    private void inIt(View view) {
        lyEmpty = view.findViewById(R.id.lyEmpty);
        rvAlarm = view.findViewById(R.id.rvAlarm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAlarm.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        toolbar = view.findViewById(R.id.toolbarOption3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        lstAlarm = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        _myRef = mDatabase.getReference("Alarm");
        if (user != null) {
            _myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Alarm alarm = snapshot.getValue(Alarm.class);
                    if (alarm != null) {
                        String today = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi")).format(new Date());
                        if (today.equals(alarm.getAlarmDate())) {
                            lstAlarm.add(alarm);
                            mKeys.add(snapshot.getKey());
                            adapter.notifyDataSetChanged();
                        }

                        if (lstAlarm.isEmpty()) {
                            lyEmpty.setVisibility(View.VISIBLE);
                            rvAlarm.setVisibility(View.GONE);
                        } else {
                            lyEmpty.setVisibility(View.GONE);
                            rvAlarm.setVisibility(View.VISIBLE);
                        }
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
                    adapter.notifyDataSetChanged();
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
                    adapter.notifyDataSetChanged();
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
}
