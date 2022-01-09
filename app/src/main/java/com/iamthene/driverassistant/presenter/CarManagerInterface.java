package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.VehicleDetail;

public interface CarManagerInterface {
    interface AddCar {
        void addSuccess();

        void addError(VehicleDetail v);
    }

    interface OnCheckEmptyList {
        void exists();
        void empty();
    }
}
