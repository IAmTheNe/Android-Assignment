package com.iamthene.driverassistant.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.User;
import com.iamthene.driverassistant.presenter.LoginInterface;
import com.iamthene.driverassistant.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginInterface {
    private static final int RC_SIGN_IN = 123;
    TextInputLayout inpUsername, inpPassword;
    EditText etUsername, etPassword;
    Button btnLogin;
    ImageView fabGoogle;
    LinearLayout lySignUp;
    LinearLayout lyForgotPassword;
    ProgressDialog dialog;
    GoogleSignInClient mGoogleSignInClient;
    LoginPresenter mLoginPresenter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);

        // Ánh xạ view
        init();

        // Google Request
        createRequest();

        // Validate
        validate();

        // Login with Email & Password
        btnLogin.setOnClickListener(view -> {
            signInWithEmailPassword();
        });

        // Sign Up Intent
        lySignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Google Login
        fabGoogle.setOnClickListener(view -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        });

        // Forgot Password
        lyForgotPassword.setOnClickListener(view -> {
            onClickforGotPassword();
        });

    }

    private void validate() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = etUsername.getText().toString().trim();
                if (!email.isEmpty()) {
                    inpUsername.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pass = etPassword.getText().toString().trim();
                if (!pass.isEmpty()) {
                    inpPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void init() {
        inpUsername = findViewById(R.id.tilEmailTest);
        inpPassword = findViewById(R.id.tilPasswordTest);
        etUsername = findViewById(R.id.etEmailTest);
        etPassword = findViewById(R.id.etPasswordTest);
        btnLogin = findViewById(R.id.btnLoginTest);
        fabGoogle = findViewById(R.id.fabGoogle);
        lySignUp = findViewById(R.id.lySignUpTest);
        lyForgotPassword = findViewById(R.id.lyForgotPassword);
        dialog = new ProgressDialog(this);
        mLoginPresenter = new LoginPresenter(this);
    }

    // Google Create Request
    private void createRequest() {
        // Config Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                mLoginPresenter.signInWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void signInWithEmailPassword() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        User user = new User(username, password);
        if (!user.isEmptyEmail() && !user.isEmptyPassword()) {
            dialog.setMessage("Đang đăng nhập...!");
            dialog.show();
        }
        mLoginPresenter.signInWithEmailPassword(user);
    }

    @Override
    public void loginSuccess() throws InterruptedException {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference _myRef = mDatabase.getReference("Users");

        if (user != null) {
            sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            _myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String userUuid = ds.child("uuid").getValue(String.class);
                        String email = ds.child("email").getValue(String.class);
                        String firstName = ds.child("firstName").getValue(String.class);
                        String lastName = ds.child("lastName").getValue(String.class);
                        User userInfo = new User();
                        userInfo.setUuid(userUuid);
                        userInfo.setFirstName(firstName);
                        userInfo.setLastName(lastName);
                        userInfo.setEmail(email);

                        if (userInfo.getUuid().equals(user.getUid())) {
                            editor.putString("fullName", userInfo.getFullName());
                            editor.apply();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Thread.sleep(2500);
        dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void loginGoogleSuccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            sharedPreferences = getSharedPreferences("DataUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("fullName", user.getDisplayName());
            editor.apply();
        }
        dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void loginError(User user) {
        if (user.isEmptyEmail()) {
            inpUsername.setError(getResources().getString(R.string.not_null));
        }

        if (user.isEmptyPassword()) {
            inpPassword.setError(getResources().getString(R.string.not_null));
        }
    }

    @Override
    public void loginErrorMessage() {
        dialog.dismiss();
        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
    }

    //@Override
    private void onClickforGotPassword(){
        dialog.setMessage("Mật khẩu đang được gửi đến gmail của bạn...!");
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "nhtuongvy201@gmail.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}