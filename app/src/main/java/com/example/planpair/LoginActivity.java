/* This is login emil verify and login status code

package com.example.planpair;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // If user is already logged in, redirect to HomeActivity
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish(); // Close LoginActivity
        }

        // Set transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Initialize views
        emailNameEditText = findViewById(R.id.emailName);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupTextLink = findViewById(R.id.signup_text);
        forgotPassword = findViewById(R.id.forgot_password);
        passwordEye = findViewById(R.id.password_eye_login);

        // Toggle password visibility
        passwordEye.setOnClickListener(view -> togglePasswordVisibility());

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                loginUser(email, password);
            }
        });

        // Navigate to Signup Activity
        signupTextLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Navigate to Forgot Password Activity
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, forgetPasswordActivity.class);
            startActivity(intent);
        });
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
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> reloadTask) {
                                        if (user.isEmailVerified()) {
                                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, BioActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            String errorMsg;
                            Exception e = task.getException();
                            if (e != null) {
                                if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no user record")) {
                                    errorMsg = "Please create an account. You have no account.";
                                } else {
                                    errorMsg = "Authentication failed: " + e.getMessage();
                                }
                            } else {
                                errorMsg = "Login failed. Try again.";
                            }
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void togglePasswordVisibility() {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_on); // Change to open eye icon
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_off); // Change to closed eye icon
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
 */

/*
package com.example.planpair;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextLink;
    private TextView forgotPassword;
    private ImageView passwordEye;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.reload().addOnCompleteListener(reloadTask -> {
                                if (user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, BioActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_on);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_off);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}*/
//new

package com.example.planpair;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupTextLink;
    private TextView forgotPassword;
    private ImageView passwordEye;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Set status bar color for devices with SDK >= Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Initialize UI components
        emailNameEditText = findViewById(R.id.emailName);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signupTextLink = findViewById(R.id.signup_text);
        forgotPassword = findViewById(R.id.forgot_password);
        passwordEye = findViewById(R.id.password_eye_login);

        // Toggle password visibility
        passwordEye.setOnClickListener(view -> togglePasswordVisibility());

        // Set login button listener
        loginButton.setOnClickListener(v -> {
            String email = emailNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (validateInputs(email, password)) {
                loginUser(email, password);
            }
        });

        // Set signup text link listener
        signupTextLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        // Set forgot password text link listener
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, forgetPasswordActivity.class)));
    }

    private boolean validateInputs(String email, String password) {
        // Validate email and password inputs
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
        // Sign in user with email and password
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Reload user data to check email verification status
                            user.reload().addOnCompleteListener(reloadTask -> {
                                if (user.isEmailVerified()) {
                                    // Check if the user has completed their profile
                                    String userId = user.getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    db.collection("UsersData").document(userId).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    // Retrieve profile completion status
                                                    Boolean isBio = documentSnapshot.getBoolean("isBioCompleted");
                                                    Boolean isInterest = documentSnapshot.getBoolean("isInterestCompleted");
                                                    Boolean isWedding = documentSnapshot.getBoolean("isWeddingGoalsCompleted");

                                                    // Redirect based on completion
                                                    if (isBio == null || !isBio) {
                                                        startActivity(new Intent(LoginActivity.this, BioActivity.class));
                                                    } else if (isInterest == null || !isInterest) {
                                                        startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                                                    } else if (isWedding == null || !isWedding) {
                                                        startActivity(new Intent(LoginActivity.this, WeddingGoalActivity.class));
                                                    } else {
                                                        // If all steps are completed, go to HomeActivity
                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                    }
                                                    finish();  // Close the login activity after redirection
                                                } else {
                                                    // If no user data exists, redirect to BioActivity (first-time user)
                                                    startActivity(new Intent(LoginActivity.this, BioActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle failure to fetch data
                                                Toast.makeText(LoginActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Login failed";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        // Toggle password visibility on button click
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_on);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEye.setImageResource(R.drawable.is_eye_off);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
