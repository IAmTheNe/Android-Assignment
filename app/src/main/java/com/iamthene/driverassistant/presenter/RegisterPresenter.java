package com.iamthene.driverassistant.presenter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.iamthene.driverassistant.model.User;

import java.util.Objects;

public class RegisterPresenter {
    private RegisterInterface mRegisterInterface;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public RegisterPresenter(RegisterInterface mRegisterInterface) {
        this.mRegisterInterface = mRegisterInterface;
    }

    public void register(User user) {
        if (user.isEmptyUser()) {
            mRegisterInterface.registerError(user);
        } else {
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String uuid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        user.setUuid(uuid);

                        UserManagerPresenter userManagerPresenter = new UserManagerPresenter();
                        userManagerPresenter.addUser(user);
                        mRegisterInterface.registerSuccess();
                    } else {
                        mRegisterInterface.registerErrorMessage();
                    }
                }
            });
        }
    }
}
