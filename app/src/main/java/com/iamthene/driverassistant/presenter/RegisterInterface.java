package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.User;

public interface RegisterInterface {
    void registerSuccess();
    void registerError(User user);
    void registerErrorMessage();
}
