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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.adapter.RepairAdapter;
import com.iamthene.driverassistant.model.Repair;

import java.util.ArrayList;
import java.util.List;

public class RepairFragment extends Fragment {
    RecyclerView rvPartChange;
    RepairAdapter adapter;
    List<Repair> lstLK;
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
        adapter = new RepairAdapter(getActivity(), lstLK);
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
                Repair repair = snapshot.getValue(Repair.class);
                if (repair != null) {
                    lstLK.add(repair);
                    mKeys.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Repair repair = snapshot.getValue(Repair.class);
                if (repair == null || lstLK == null || lstLK.isEmpty()) {
                    return;
                }
                String key = snapshot.getKey();
                int index = mKeys.indexOf(key);
                lstLK.set(index, repair);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Repair repair = snapshot.getValue(Repair.class);
                if (repair == null || lstLK == null || lstLK.isEmpty()) {
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
