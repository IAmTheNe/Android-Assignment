package com.iamthene.driverassistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.iamthene.driverassistant.adapter.LinhKienAdapter;
import com.iamthene.driverassistant.model.LinhKien;

import java.util.ArrayList;
import java.util.List;

public class RepairFragment extends Fragment {
    RecyclerView rvPartChange;
    LinhKienAdapter adapter;
    List<LinhKien> lstLK;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    ArrayList<String> mKeys = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repair, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        getData();
        adapter = new LinhKienAdapter(getActivity(), lstLK);
        rvPartChange.setAdapter(adapter);
    }

    private void getData() {
        lstLK = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        _myRef = mDatabase.getReference("Repair");
        assert user != null;
        _myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien != null) {
                    lstLK.add(linhKien);
                    mKeys.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                lstLK.set(index, linhKien);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LinhKien linhKien = snapshot.getValue(LinhKien.class);
                if (linhKien == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                if (index != -1) {
                    lstLK.remove(index);
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

    private void inIt(View view) {
        rvPartChange = view.findViewById(R.id.rvPartChange);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPartChange.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
    }
}
