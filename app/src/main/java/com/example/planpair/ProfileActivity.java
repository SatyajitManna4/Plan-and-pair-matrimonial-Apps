/*
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private boolean isLiked = false; // Track like status
    private ImageView likeButton;
    private LinearLayout chatSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        CompatibilityScoreView compatibilityView = findViewById(R.id.compatibilityView);
        likeButton = findViewById(R.id.likeButton); // Heart icon as like button
        chatSection = findViewById(R.id.chatSection); // Chat section layout

        if (compatibilityView != null) {
            compatibilityView.setCompatibilityScore(85);
            compatibilityView.invalidate();
            compatibilityView.requestLayout();
        }

        // Set initial chat section visibility
        chatSection.setVisibility(View.GONE);

        // Like button functionality
        likeButton.setOnClickListener(v -> {
            isLiked = !isLiked; // Toggle like status
            updateLikeButton();
        });

        // Chat button click listener
        findViewById(R.id.chatSection).setOnClickListener(v -> {
            if (isLiked) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
            }
        });
    }

    private void updateLikeButton() {
        if (isLiked) {
            likeButton.setImageResource(R.drawable.baseline_favorite_24); // Filled heart
            chatSection.setVisibility(View.VISIBLE); // Show chat section
        } else {
            likeButton.setImageResource(R.drawable.baseline_favorite_border_24); // Outline heart
            chatSection.setVisibility(View.GONE); // Hide chat section
        }
    }
}
*/

//Compatibility score calling from compatibilityCalculator.java file

package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private boolean isLiked = false;
    private ImageView likeButton;
    private LinearLayout chatSection;
    private String currentUser;
    private String otherUserId;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private CompatibilityScoreView compatibilityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        otherUserId = getIntent().getStringExtra("otherUserId");
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        likeButton = findViewById(R.id.likeButton);
        chatSection = findViewById(R.id.chatSection);
        compatibilityView = findViewById(R.id.compatibilityView);

        if (firebaseUser == null || otherUserId == null) return;

        String currentUserId = firebaseUser.getUid();

        db.collection("UsersData").document(currentUserId).get().addOnSuccessListener(currentSnapshot -> {
            if (currentSnapshot.exists()) {
                currentUser = String.valueOf(currentSnapshot.toObject(UserProfile.class));
                currentUser.strip();

                db.collection("UsersData").document(otherUserId).get().addOnSuccessListener(otherSnapshot -> {
                    if (otherSnapshot.exists()) {
                        UserProfile otherUser = otherSnapshot.toObject(UserProfile.class);
                        otherUser.setUid(otherUserId);

//                        int score = CompatibilityCalculator.calculateAndStoreCompatibility(currentUser, String.valueOf(otherUser));
                        int score =10;
                        if (compatibilityView != null) {
                            compatibilityView.setCompatibilityScore(score);
                            compatibilityView.invalidate();
                            compatibilityView.requestLayout();
                        }

                    }
                });
            }
        });

        chatSection.setVisibility(View.GONE);

        likeButton.setOnClickListener(v -> {
            isLiked = !isLiked;
            updateLikeButton();
        });

        chatSection.setOnClickListener(v -> {
            if (isLiked) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
            }
        });
    }


    private void updateLikeButton() {
        if (isLiked) {
            likeButton.setImageResource(R.drawable.baseline_favorite_24); // Filled heart
            chatSection.setVisibility(View.VISIBLE); // Show chat section
        } else {
            likeButton.setImageResource(R.drawable.baseline_favorite_border_24); // Outline heart
            chatSection.setVisibility(View.GONE); // Hide chat section
        }
    }
}
