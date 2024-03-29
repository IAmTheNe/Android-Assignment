package com.iamthene.driverassistant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamthene.driverassistant.R;
import com.iamthene.driverassistant.model.User;
import com.iamthene.driverassistant.presenter.CarManagerInterface;
import com.iamthene.driverassistant.presenter.GetCarPresenter;
import com.iamthene.driverassistant.presenter.LoginInterface;
import com.iamthene.driverassistant.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginInterface, CarManagerInterface.OnCheckEmptyList {
    private static final int RC_SIGN_IN = 123;
    TextInputLayout inpUsername, inpPassword;
    EditText etUsername, etPassword;
    Button btnLogin;
    ImageView fabGoogle;
    ImageView fabFacebook;
    LinearLayout lySignUp;
    LinearLayout lyForgotPassword;
    ProgressDialog dialog;
    GoogleSignInClient mGoogleSignInClient;
    LoginPresenter mLoginPresenter;
    SharedPreferences sharedPreferences;
    ImageView civAvatar;
    GetCarPresenter mPresenter;

    CallbackManager mCallbackManager;
    private String TAG = "Facebook Activity";
    private FirebaseAuth mAuth;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);

        //printHashKey(LoginActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();

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

        LoginButton loginButton = findViewById(R.id.fabFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
    /* public static void printHashKey(Context pContext) {
        String TAG = "Hash key";
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    } */

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
        //fabFacebook = findViewById(R.id.fabFacebook);
        loginButton = findViewById(R.id.fabFacebook);
        lySignUp = findViewById(R.id.lySignUpTest);
        lyForgotPassword = findViewById(R.id.lyForgotPassword);
        dialog = new ProgressDialog(this);
        mLoginPresenter = new LoginPresenter(this);
        civAvatar = findViewById(R.id.civAvatar);
        mPresenter = new GetCarPresenter(this);
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
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        try {
            Toast.makeText(LoginActivity.this, currentUser.getUid(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
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

        mPresenter.hasNoOwnerCar();
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
        mPresenter.hasNoOwnerCar();
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
    private void onClickforGotPassword() {
        dialog.setMessage("Mật khẩu đang được gửi đến gmail của bạn...!");
        dialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "nguyenthe.tnt@gmail.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void exists() {
        dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void empty() {
        dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, NewCarActivity.class);
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finishAffinity();
    }
}