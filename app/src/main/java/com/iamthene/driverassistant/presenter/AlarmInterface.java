package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.Alarm;

import java.util.List;

public interface AlarmInterface {
    interface AddAlarmValue {
        void addSuccess();
        void addErrorMessage(Alarm alarm);
    }

    interface OnEmptyAlarm {
        void exists();
        void empty();
    }

    interface OnReadAndWriteData {
        List<Alarm> getList();
    }
}
