package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.Oil;

public interface OilInterface {
    interface OnCheckEmptyList {
        void exists();

        void empty();
    }

    interface OnAddOil {
        void addSuccess();

        void addFailed(Oil oil);
    }
}
