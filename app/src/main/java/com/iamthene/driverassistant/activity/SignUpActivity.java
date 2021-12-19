package com.iamthene.driverassistant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.User;
import com.iamthene.driverassistant.presenter.RegisterInterface;
import com.iamthene.driverassistant.presenter.RegisterPresenter;

public class SignUpActivity extends AppCompatActivity implements RegisterInterface {
    TextInputLayout inpFirstName, inpLastName, inpNewUsername, inpNewPassword;
    EditText etFirstName, etLastName, etNewUsername, etNewPassword;
    Button btnCreateNewAccount;
    ProgressDialog progressDialog;
    RegisterPresenter mRegisterPresenter;
    SharedPreferences sharedPreferences;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        initListener();
        textChange();
    }

    private void initListener() {
        btnCreateNewAccount.setOnClickListener(view -> onClickSignUp());
    }

    private void init() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnCreateNewAccount = findViewById(R.id.btnCreateAccount);
        inpFirstName = findViewById(R.id.inpFirstName);
        inpLastName = findViewById(R.id.inpLastName);
        inpNewUsername = findViewById(R.id.inpNewUsername);
        inpNewPassword = findViewById(R.id.inpNewPassword);
        progressDialog = new ProgressDialog(this);
        mRegisterPresenter = new RegisterPresenter(this);
    }

    private void onClickSignUp() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etNewUsername.getText().toString().trim();
        String password = etNewPassword.getText().toString().trim();

        User user = new User(firstName, lastName, username, password);
        if (!user.isEmptyUser()) {
            progressDialog.setMessage("Đang tạo tài khoản!");
            progressDialog.show();
            sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fullName", user.getFirstName().concat(" ").concat(user.getLastName()));
            editor.apply();
        }
        mRegisterPresenter.register(user);
    }

    private void textChange() {
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etFirstName.getText().toString().isEmpty()) {
                    inpFirstName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etLastName.getText().toString().isEmpty()) {
                    inpLastName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etNewUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etNewUsername.getText().toString().isEmpty()) {
                    inpNewUsername.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etNewPassword.getText().toString().isEmpty()) {
                    inpNewPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void registerSuccess() {
        progressDialog.dismiss();
        _myRef = mDatabase.getReference("OwnerCar");
        _myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(SignUpActivity.this, NewCarActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void registerError(User user) {
        if (user.isEmptyEmail()) {
            inpNewUsername.setError(getResources().getString(R.string.not_null));
        }

        if (user.isEmptyPassword()) {
            inpNewPassword.setError(getResources().getString(R.string.not_null));
        }

        if (user.isEmptyFirstName()) {
            inpFirstName.setError(getResources().getString(R.string.not_null));
        }

        if (user.isEmptyLastName()) {
            inpLastName.setError(getResources().getString(R.string.not_null));
        }
    }

    @Override
    public void registerErrorMessage() {
        progressDialog.dismiss();
        Toast.makeText(SignUpActivity.this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
    }
}