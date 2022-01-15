package com.iamthene.driverassistant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.Refuel;

import java.util.ArrayList;
import java.util.List;

public class ReportGeneralFragment extends Fragment {
    PieChart mPieChart;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText etReportMoney;
    List<Refuel> refuels;
    List<String> mKeysRefuel = new ArrayList<>();
    ArrayList<PieEntry> entries;
    int sumRefuel, sumOil, sumRepair = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        setupPieChart();
        loadPieChartData();
//        getRefuelMoney();
    }

    private void inIt(View view) {
        mPieChart = view.findViewById(R.id.pieChart);
        etReportMoney = view.findViewById(R.id.etReportMoney);
    }

    private void setupPieChart() {
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(12);
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setCenterText("Thống kê chi tiêu");
        mPieChart.setCenterTextSize(24);
        mPieChart.getDescription().setEnabled(false);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        sumRefuel = 0;
        refuels = new ArrayList<>();
        entries = new ArrayList<>();

        DatabaseReference _myRef = mDatabase.getReference("Refuel");
        if (user != null) {
            _myRef.child(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Refuel r = snapshot.getValue(Refuel.class);
                    if (r == null) {
                        return;
                    }
                    refuels.add(r);
                    mKeysRefuel.add(snapshot.getKey());

                    for (Refuel rs: refuels) {
                        sumRefuel += Integer.parseInt(rs.getFee());
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Refuel r = snapshot.getValue(Refuel.class);
                    if (r == null) {
                        return;
                    }
                    int index = mKeysRefuel.indexOf(snapshot.getKey());
                    refuels.set(index, r);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Toast.makeText(requireContext(), sumRefuel+"", Toast.LENGTH_SHORT).show();

        entries.add(new PieEntry(0.25f, "Đổ xăng"));
        entries.add(new PieEntry(0.55f, "Thay nhớt"));
        entries.add(new PieEntry(0.2f, "Sửa chữa"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(mPieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        mPieChart.setData(data);
        mPieChart.invalidate();

        mPieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void getRefuelMoney() {
        sumRefuel = 0;
        refuels = new ArrayList<>();

    }

    private void getRepairMoney() {
        sumOil = 0;
    }

    private void getOilMoney() {

    }
}
