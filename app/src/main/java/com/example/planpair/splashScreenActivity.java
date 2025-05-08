package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashScreenActivity extends AppCompatActivity {
    ImageView logo;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.applogo);
        Animation scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        logo.startAnimation(scale);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Wait for 2.5 seconds before proceeding
        new Handler().postDelayed(() -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null && user.isEmailVerified()) {
                // User is logged in and email is verified
                String userId = user.getUid();

                // Fetch user data from Firestore
                db.collection("UsersData").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Check if user has completed profile sections
                                Boolean isBio = documentSnapshot.getBoolean("isBioCompleted");
                                Boolean isInterest = documentSnapshot.getBoolean("isInterestCompleted");
                                Boolean isWedding = documentSnapshot.getBoolean("isWeddingGoalsCompleted");

                                // Determine where to redirect based on profile completion
                                if (isBio == null || !isBio) {
                                    startActivity(new Intent(splashScreenActivity.this, BioActivity.class));
                                } else if (isInterest == null || !isInterest) {
                                    startActivity(new Intent(splashScreenActivity.this, InterestActivity.class));
                                } else if (isWedding == null || !isWedding) {
                                    startActivity(new Intent(splashScreenActivity.this, WeddingGoalActivity.class));
                                } else {
                                    // All sections completed, proceed to HomeActivity
                                    startActivity(new Intent(splashScreenActivity.this, HomeActivity.class));
                                }
                            } else {
                                // If no user data is found, first-time user
                                startActivity(new Intent(splashScreenActivity.this, BioActivity.class));
                            }
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure when fetching data
                            startActivity(new Intent(splashScreenActivity.this, LoginActivity.class));
                            finish();
                        });
            } else {
                // If no user is logged in or email is not verified
                startActivity(new Intent(splashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 2500); // Splash screen duration
    }
}