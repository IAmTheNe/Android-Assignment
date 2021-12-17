package com.iamthene.driverassistant.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iamthene.driverassistant.fragment.ReportGeneralFragment;
import com.iamthene.driverassistant.fragment.ReportOilFragment;
import com.iamthene.driverassistant.fragment.ReportRefuelFragment;
import com.iamthene.driverassistant.fragment.ReportRepairFragment;

public class ReportAdapter extends FragmentStateAdapter {
    public ReportAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ReportGeneralFragment();
        } else if (position == 1) {
            return new ReportRefuelFragment();
        } else if (position == 2) {
            return new ReportOilFragment();
        } else if (position == 3) {
            return new ReportRepairFragment();
        } else {
            return new ReportGeneralFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
