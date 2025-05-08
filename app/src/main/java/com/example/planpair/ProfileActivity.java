/*
package com.example.planpair;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize CompatibilityScoreView
        CompatibilityScoreView compatibilityView = findViewById(R.id.compatibilityView);
        if (compatibilityView != null) {
            compatibilityView.setCompatibilityScore(85); // Example score
            compatibilityView.invalidate();
            compatibilityView.requestLayout();

        }
    }
}

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

//Compatibility score algo
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private boolean isLiked = false; // Track like status
    private ImageView likeButton;
    private LinearLayout chatSection;
    private UserProfile currentUser;  // Define a variable to hold the current user profile
    private String otherUserId;  // Variable to hold the other user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Retrieve the other user's ID passed via the Intent
        otherUserId = getIntent().getStringExtra("otherUserId");

        // Initialize UI components
        CompatibilityScoreView compatibilityView = findViewById(R.id.compatibilityView);
        likeButton = findViewById(R.id.likeButton); // Heart icon as like button
        chatSection = findViewById(R.id.chatSection); // Chat section layout

        // Get the current logged-in user ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the reference to the current user profile in Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UsersData").child(currentUserId);

        // Fetch current user's profile data
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Retrieve the current user data and map it to the UserProfile object
                currentUser = task.getResult().getValue(UserProfile.class);

                // Fetch the other user's profile dynamically using the otherUserId
                DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference("UsersData").child(otherUserId);

                otherUserRef.get().addOnCompleteListener(otherTask -> {
                    if (otherTask.isSuccessful()) {
                        UserProfile otherUser = otherTask.getResult().getValue(UserProfile.class);

                        // Calculate compatibility score between currentUser and otherUser
                        double compatibilityScore = CompatibilityCalculator.calculateCompatibility(currentUser, otherUser);

                        // Set the score to the CompatibilityScoreView
                        if (compatibilityView != null) {
                            compatibilityView.setCompatibilityScore((float) compatibilityScore);  // Convert to float if needed
                            compatibilityView.invalidate();
                            compatibilityView.requestLayout();
                        }
                    }
                });
            }
        });

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