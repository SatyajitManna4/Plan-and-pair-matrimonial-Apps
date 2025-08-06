package com.example.planpair;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    private EditText emailNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextLink;
    private TextView forgotPassword;
    private ImageView passwordEye;
    private FirebaseAuth auth;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        emailNameEditText = findViewById(R.id.emailName);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupTextLink = findViewById(R.id.signup_text);
        forgotPassword = findViewById(R.id.forgot_password);
        passwordEye = findViewById(R.id.password_eye_login);

        passwordEye.setOnClickListener(view -> togglePasswordVisibility());

        loginButton.setOnClickListener(v -> {
            String email = emailNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (validateInputs(email, password)) {
                loginUser(email, password);
            }
        });

        signupTextLink.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, forgetPasswordActivity.class)));
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailNameEditText.setError("Email cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void loginUser(String email, String password) {
        if (!isNetworkConnected()) {
            Toast.makeText(LoginActivity.this, "Mobile data is off or no internet connection.", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.reload().addOnCompleteListener(reloadTask -> {
                                if (user.isEmailVerified()) {
                                    String userId = user.getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    db.collection("UsersData").document(userId).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    // Store FCM Token
                                                    FirebaseMessaging.getInstance().getToken()
                                                            .addOnCompleteListener(tokenTask -> {
                                                                if (tokenTask.isSuccessful()) {
                                                                    String token = tokenTask.getResult();
                                                                    db.collection("UsersData").document(userId)
                                                                            .update("fcmToken", token);
                                                                }
                                                            });

                                                    Boolean isBio = documentSnapshot.getBoolean("isBioCompleted");
                                                    Boolean isInterest = documentSnapshot.getBoolean("isInterestCompleted");
                                                    Boolean isWedding = documentSnapshot.getBoolean("isWeddingGoalsCompleted");

                                                    if (isBio == null || !isBio) {
                                                        startActivity(new Intent(LoginActivity.this, BioActivity.class));
                                                    } else if (isInterest == null || !isInterest) {
                                                        startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                                                    } else if (isWedding == null || !isWedding) {
                                                        startActivity(new Intent(LoginActivity.this, WeddingGoalActivity.class));
                                                    } else {
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    }
                                                    finish();
                                                } else {
                                                    startActivity(new Intent(LoginActivity.this, BioActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(LoginActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception != null && exception.getMessage() != null &&
                                exception.getMessage().contains("There is no user record")) {
                            Toast.makeText(LoginActivity.this, "No account found. Please create an account.", Toast.LENGTH_LONG).show();
                        } else {
                            String errorMsg = exception != null ? exception.getMessage() : "Login failed";
                            Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_on);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_off);
        }

        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
