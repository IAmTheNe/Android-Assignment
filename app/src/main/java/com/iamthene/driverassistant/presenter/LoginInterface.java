package com.iamthene.driverassistant.presenter;

import com.iamthene.driverassistant.model.User;

public interface LoginInterface {
    void loginSuccess() throws InterruptedException;

    void loginGoogleSuccess();

    void loginError(User user);

    void loginErrorMessage();
}
