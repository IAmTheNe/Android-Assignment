package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.Repair;

public interface RepairInterface {
    interface OnCheckEmptyList {
        void empty();

        void exists();
    }

    interface OnAddRepair {
        void addSuccess();

        void addFailed(Repair repair);
    }
}
