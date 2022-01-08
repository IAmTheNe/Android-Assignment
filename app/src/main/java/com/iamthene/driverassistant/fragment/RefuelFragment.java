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
import com.iamthene.driverassistant.adapter.RefuelAdapter;
import com.iamthene.driverassistant.model.Refuel;

import java.util.ArrayList;
import java.util.List;

public class RefuelFragment extends Fragment {
    RecyclerView rvRefuel;
    List<Refuel> lstRefuel;
    RefuelAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    List<String> mKeys = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        getData();
        adapter = new RefuelAdapter(lstRefuel, getActivity());
        rvRefuel.setAdapter(adapter);


    }

    private void inIt(View view) {
        rvRefuel = view.findViewById(R.id.rvRefuel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvRefuel.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refuel, container, false);
    }

    private void getData() {
        lstRefuel = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef = database.getReference("Refuel");
        assert user != null;
        myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p != null) {
                    lstRefuel.add(p);
                    String key = snapshot.getKey();
                    mKeys.add(key);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p == null || lstRefuel == null || lstRefuel.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                lstRefuel.set(index, p);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Refuel p = snapshot.getValue(Refuel.class);
                if (p == null || lstRefuel == null || lstRefuel.isEmpty()) {
                    return;
                }
                int index = mKeys.indexOf(snapshot.getKey());
                if (index != -1) {
                    lstRefuel.remove(index);
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
