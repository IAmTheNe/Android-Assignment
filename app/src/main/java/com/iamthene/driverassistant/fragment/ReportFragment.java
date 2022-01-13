package com.iamthene.driverassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.activity.DashboardActivity;
import com.iamthene.driverassistant.adapter.ReportAdapter;

public class ReportFragment extends Fragment {
    TabLayout tab_layout;
    ViewPager2 viewPager2;
    ReportAdapter mReportAdapter;
    MaterialToolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inIt(view);
        initTabLayout();
    }

    private void inIt(View view) {
        tab_layout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager_2);
        mReportAdapter = new ReportAdapter(requireActivity());
        viewPager2.setAdapter(mReportAdapter);
        toolbar = view.findViewById(R.id.toolbarOption3);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
        });
    }

    private void initTabLayout() {
        new TabLayoutMediator(tab_layout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Tổng quan");
                    break;
                case 1:
                    tab.setText("Đổ xăng");
                    break;
                case 2:
                    tab.setText("Thay nhớt");
                    break;
                case 3:
                    tab.setText("Sửa chữa");
                    break;
            }
        }).attach();
    }
}
