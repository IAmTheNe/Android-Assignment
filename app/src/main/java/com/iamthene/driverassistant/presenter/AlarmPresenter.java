package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmPresenter {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef = mDatabase.getReference("Alarm");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private AlarmInterface.AddAlarmValue addAlarmValue;
    private AlarmInterface.OnEmptyAlarm onEmptyAlarm;

    public AlarmPresenter(AlarmInterface.AddAlarmValue alarmInterface) {
        this.addAlarmValue = alarmInterface;
    }

    public AlarmPresenter(AlarmInterface.OnEmptyAlarm onEmptyAlarm) {
        this.onEmptyAlarm = onEmptyAlarm;
    }

    public void addAlarm(Alarm alarm) {
        if (alarm.isEmptyInput()) {
            addAlarmValue.addErrorMessage(alarm);
        } else {
            if (user != null) {
                String uid = user.getUid();
                String keys = _myRef.push().getKey();
                alarm.setId(keys);
                if (keys != null) {
                    DatabaseReference dr = _myRef.child(uid).child(keys);
                    dr.child("id").setValue(alarm.getId());
                    dr.child("title").setValue(alarm.getTitle());
                    dr.child("createdAt").setValue(alarm.getCreatedAt());
                    dr.child("alarmDate").setValue(alarm.getAlarmDate());
                    dr.child("desc").setValue(alarm.getDesc());
                    dr.child("type").setValue(alarm.getType());
                    dr.child("checked").setValue(alarm.isChecked());
                }
                addAlarmValue.addSuccess();
            }
        }
    }

    public void isEmptyList() {
        List<Alarm> alarmList = new ArrayList<>();
        if (user != null) {
            _myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Alarm alarm = ds.getValue(Alarm.class);
                        alarmList.add(alarm);
                    }

                    if (alarmList.isEmpty()) {
                        onEmptyAlarm.empty();
                    } else {
                        onEmptyAlarm.exists();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
