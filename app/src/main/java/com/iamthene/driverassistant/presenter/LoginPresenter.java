package com.iamthene.driverassistant.presenter;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.iamthene.driverassistant.model.User;

import java.util.Objects;

public class LoginPresenter {
    private final LoginInterface mLoginInterface;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public LoginPresenter(LoginInterface mLoginInterface) {
        this.mLoginInterface = mLoginInterface;
    }

    public void signInWithEmailPassword(User user) {
        if (user.isEmptyEmail() || user.isEmptyPassword()) {
            mLoginInterface.loginError(user);
        } else {
            mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    try {
                        mLoginInterface.loginSuccess();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    mLoginInterface.loginErrorMessage();
                }
            });
        }
    }

    public void signInWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    User user1 = new User();
                    UserManagerPresenter userManagerPresenter = new UserManagerPresenter();
                    user1.setUuid(user.getUid());
                    user1.setEmail(user.getEmail());

                    String[] name = Objects.requireNonNull(user.getDisplayName()).split(" ");
                    user1.setFirstName(name[0]);
                    String lastName = "";
                    for (int i = 1; i < name.length; i++) {
                        lastName = lastName.concat(name[i]).concat(" ");
                    }

                    user1.setLastName(lastName.trim());
                    userManagerPresenter.addUser(user1);
                }

                mLoginInterface.loginGoogleSuccess();
            } else {
                mLoginInterface.loginErrorMessage();
            }
        });
    }
}
