/*
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
            }, 1000); // Splash screen duration
        }
    }*/

//updated splash screen

package com.example.planpair;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashScreenActivity extends AppCompatActivity {

    private ImageView femaleRing, maleRing;
    private TextView planPairText;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        femaleRing = findViewById(R.id.femaleRing);
        maleRing = findViewById(R.id.maleRing);
        planPairText = findViewById(R.id.planPairText);

        femaleRing.setVisibility(View.VISIBLE);
        maleRing.setVisibility(View.VISIBLE);
        planPairText.setVisibility(View.INVISIBLE);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Step 1: Pop animation
        ObjectAnimator femalePop = ObjectAnimator.ofPropertyValuesHolder(
                femaleRing,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        );
        femalePop.setDuration(500);

        ObjectAnimator malePop = ObjectAnimator.ofPropertyValuesHolder(
                maleRing,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        );
        malePop.setDuration(500);

        // Step 2: Move rings toward center (adjust values based on screen and layout)
        ObjectAnimator femaleMoveX = ObjectAnimator.ofFloat(femaleRing, "translationX", 0f, 300f);
        ObjectAnimator maleMoveX = ObjectAnimator.ofFloat(maleRing, "translationX", 0f, -300f);

        // Slight Y offset to look entangled
        ObjectAnimator femaleMoveY = ObjectAnimator.ofFloat(femaleRing, "translationY", 0f, -20f);
        ObjectAnimator maleMoveY = ObjectAnimator.ofFloat(maleRing, "translationY", 0f, 20f);

        femaleMoveX.setDuration(1000);
        maleMoveX.setDuration(1000);
        femaleMoveY.setDuration(1000);
        maleMoveY.setDuration(1000);

        // Step 3: Rotate rings for entangled effect
        ObjectAnimator femaleRotate = ObjectAnimator.ofFloat(femaleRing, "rotation", 0f, -30f);
        ObjectAnimator maleRotate = ObjectAnimator.ofFloat(maleRing, "rotation", 0f, 30f);
        femaleRotate.setDuration(500);
        maleRotate.setDuration(500);

        // Step 4: Fade in text
        ObjectAnimator fadeInText = ObjectAnimator.ofFloat(planPairText, "alpha", 0f, 1f);
        fadeInText.setDuration(800);
        fadeInText.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                planPairText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // After animation ends, check Firebase and navigate accordingly
                checkUserAndRedirect();
            }
        });

        // Combine all animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                createTogetherSet(femalePop, malePop),
                createTogetherSet(femaleMoveX, maleMoveX, femaleMoveY, maleMoveY),
                createTogetherSet(femaleRotate, maleRotate),
                fadeInText
        );
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private AnimatorSet createTogetherSet(Animator... animators) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        return set;
    }

    private void checkUserAndRedirect() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null && user.isEmailVerified()) {
            String userId = user.getUid();
            db.collection("UsersData").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean isBio = documentSnapshot.getBoolean("isBioCompleted");
                            Boolean isInterest = documentSnapshot.getBoolean("isInterestCompleted");
                            Boolean isWedding = documentSnapshot.getBoolean("isWeddingGoalsCompleted");

                            // If all completed, go to HomeActivity
                            if (Boolean.TRUE.equals(isBio) && Boolean.TRUE.equals(isInterest) && Boolean.TRUE.equals(isWedding)) {
                                startActivity(new Intent(splashScreenActivity.this, HomeActivity.class));
                            } else {
                                // If any incomplete, go to the first incomplete activity in order:
                                if (!Boolean.TRUE.equals(isBio)) {
                                    // Bio incomplete → go to BioActivity (replace with your actual activity)
                                    startActivity(new Intent(splashScreenActivity.this, BioActivity.class));
                                } else if (!Boolean.TRUE.equals(isInterest)) {
                                    // Interest incomplete → go to InterestActivity
                                    startActivity(new Intent(splashScreenActivity.this, InterestActivity.class));
                                } else if (!Boolean.TRUE.equals(isWedding)) {
                                    // Wedding Goals incomplete → go to WeddingGoalsActivity
                                    startActivity(new Intent(splashScreenActivity.this, WeddingGoalActivity.class));
                                } else {
                                    // Fallback: if somehow none matches, go to SliderActivity
                                    startActivity(new Intent(splashScreenActivity.this, SliderActivity.class));
                                }
                            }
                        } else {
                            // No document (first time user), go to SliderActivity
                            startActivity(new Intent(splashScreenActivity.this, SliderActivity.class));
                        }
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // On failure, fallback to SliderActivity
                        startActivity(new Intent(splashScreenActivity.this, SliderActivity.class));
                        finish();
                    });
        } else {
            // No user or email not verified, go to SliderActivity
            startActivity(new Intent(splashScreenActivity.this, SliderActivity.class));
            finish();
        }
    }

}