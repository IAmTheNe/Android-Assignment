package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.Refuel;

public interface RefuelInterface {
    interface OnCheckEmptyList {
        void empty();

        void exists();
    }

    interface OnAddRefuel {
        void addSuccess();

        void addFailed(Refuel refuel);
    }
}
