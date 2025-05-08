/*
package com.example.planpair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, contactEditText;
    private Button signupButton;
    private ImageView passwordEye, confirmPasswordEye;
    private boolean isPasswordVisible = false, isConfirmPasswordVisible = false;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Initialize UI elements
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        contactEditText = findViewById(R.id.contactNo);
        signupButton = findViewById(R.id.signup);

        // Initialize eye icons for password visibility toggle
        passwordEye = findViewById(R.id.password_eye);
        confirmPasswordEye = findViewById(R.id.confirmpassword_eye);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set click listeners for eye icons
        passwordEye.setOnClickListener(view -> togglePasswordVisibility(passwordEditText, passwordEye));
        confirmPasswordEye.setOnClickListener(view -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordEye));

        // Signup Button Click
        signupButton.setOnClickListener(v -> validateAndSignup());
    }

    private void togglePasswordVisibility(EditText editText, ImageView eyeIcon) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeIcon.setImageResource(R.drawable.is_eye_on); // Change to open eye icon
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.is_eye_off); // Change to closed eye icon
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean validateAndSignup() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();

        // Validation Checks
        if (TextUtils.isEmpty(name)) {
            showToast("Name is required");
            return false;
        }
        if (name.length() > 20) {
            showToast("Name must be max 20 characters");
            return false;
        }
        if (Pattern.compile("[0-9]").matcher(name).find()) {
            nameEditText.setError("Username cannot contain numbers");
            return false;
        }

        // Validate email
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email");
            return false;
        }
        // Validate password
        if (!isValidPassword(password)) {
            showToast("Password must be 8-12 characters, include 1 special char, 1 digit, 1 uppercase letter");
            return false;
        }
        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }
        //Validate Contact Number
        if (contact.length() != 10 || !contact.matches("\\d+")) {
            showToast("Contact number must be exactly 10 digits");
            return false;
        }

        // If all validations pass, navigate to LoginActivity
        // If all validations pass, create user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = auth.getCurrentUser().getUid();

                    // Create a map to store user data
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("username", name);
                    userMap.put("email", email);
                    userMap.put("contactNumber", contact);

                    // Store user data in Firestore under "Users" collection

                    firestore.collection("UsersData").document(userId).set(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to login page
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Firestore Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;

       */
/* showToast("Signup successful!");
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();*//*

    }

    private boolean isValidPassword(String password) {
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$"
        );
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}*/
//email verify link send features

package com.example.planpair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, contactEditText;
    private Button signupButton;
    private ImageView passwordEye, confirmPasswordEye;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Initialize UI elements
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        contactEditText = findViewById(R.id.contactNo);
        signupButton = findViewById(R.id.signup);
        passwordEye = findViewById(R.id.password_eye);
        confirmPasswordEye = findViewById(R.id.confirmpassword_eye);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set click listeners for eye icons
        passwordEye.setOnClickListener(view -> togglePasswordVisibility(passwordEditText, passwordEye));
        confirmPasswordEye.setOnClickListener(view -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordEye));

        // Signup Button Click
        signupButton.setOnClickListener(v -> validateAndSignup());
    }

    private void togglePasswordVisibility(EditText editText, ImageView eyeIcon) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeIcon.setImageResource(R.drawable.is_eye_on); // Change to open eye icon
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.is_eye_off); // Change to closed eye icon
        }
        editText.setSelection(editText.getText().length());
    }

    private boolean validateAndSignup() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String contact = contactEditText.getText().toString().trim();

        // Validation Checks
        if (TextUtils.isEmpty(name)) {
            showToast("Name is required");
            return false;
        }
        if (name.length() > 20) {
            showToast("Name must be max 20 characters");
            return false;
        }
        if (Pattern.compile("[0-9]").matcher(name).find()) {
            nameEditText.setError("Username cannot contain numbers");
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email");
            return false;
        }
        if (!isValidPassword(password)) {
            showToast("Password must be 8-12 characters, include 1 special char, 1 digit, 1 uppercase letter");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }
        if (contact.length() != 10 || !contact.matches("\\d+")) {
            showToast("Contact number must be exactly 10 digits");
            return false;
        }

        // Create user in Firebase Auth
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    // Send Email Verification
                    user.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                        if (verificationTask.isSuccessful()) {
                            // Store user data in Firestore only after sending verification
                            storeUserData(user.getUid(), name, email, contact);

                            showToast("Verification email sent. Please check your inbox.");
                            auth.signOut(); // Sign out to force user to verify email
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            showToast("Failed to send verification email.");
                        }
                    });
                }
            } else {
                showToast("Sign Up Failed: " + task.getException().getMessage());
            }
        });

        return true;
    }

    private void storeUserData(String userId, String name, String email, String contact) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", name);
        userMap.put("email", email);
        userMap.put("contactNumber", contact);
        userMap.put("emailVerified", false); // Store verification status

        firestore.collection("UsersData").document(userId).set(userMap)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        showToast("Firestore Error: " + task.getException().getMessage());
                    }
                });
    }

    private boolean isValidPassword(String password) {
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$"
        );
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}