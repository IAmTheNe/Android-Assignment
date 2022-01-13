package com.iamthene.driverassistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
import com.iamthene.driverassistant.adapter.OilAdapter;
import com.iamthene.driverassistant.model.Oil;

import java.util.ArrayList;
import java.util.List;

public class OilFragment extends Fragment {
    RecyclerView rvOil;
    List<Oil> lstOil;
    OilAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    List<String> mKeys = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        getData();
        adapter = new OilAdapter(lstOil, getActivity());
        rvOil.setAdapter(adapter);
    }

    private void getData() {
        lstOil = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference("Oil");
        if (user != null) {
            myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Oil o = snapshot.getValue(Oil.class);
                    if (o != null) {
                        lstOil.add(o);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Oil o = snapshot.getValue(Oil.class);
                    if (o == null || lstOil == null || lstOil.isEmpty()) {
                        return;
                    }
                    int index = mKeys.indexOf(snapshot.getKey());
                    lstOil.set(index, o);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Oil o = snapshot.getValue(Oil.class);
                    if (o == null || lstOil == null || lstOil.isEmpty()) {
                        return;
                    }
                    int index = mKeys.indexOf(snapshot.getKey());
                    if (index != -1) {
                        lstOil.remove(index);
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

    private void inIt(View view) {
        rvOil = view.findViewById(R.id.rvListOil);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvOil.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oil, container, false);
    }
}
