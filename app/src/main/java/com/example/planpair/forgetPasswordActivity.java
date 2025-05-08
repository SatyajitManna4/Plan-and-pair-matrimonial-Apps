package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;


public class forgetPasswordActivity extends AppCompatActivity {
    private EditText emailEdiText;
    private Button resetPasswordButton;
    private TextView backToLoginText;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize views
        emailEdiText = findViewById(R.id.emailEText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backToLoginText = findViewById(R.id.backToLoginText);

        // Reset Password Button Click Listener
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdiText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEdiText.setError("Please enter your email");
                    return;
                }

                sendPasswordResetEmail(email);
            }
        });

        // Back to Login Button Click Listener
        backToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the login activity
                Intent intent = new Intent(forgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Email sent successfully
                        Toast.makeText(forgetPasswordActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                        // Redirect to Login screen
                        Intent intent = new Intent(forgetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sending the reset link fails
                        Toast.makeText(forgetPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
