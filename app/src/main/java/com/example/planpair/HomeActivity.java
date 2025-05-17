//fetch and show data from firestore database to UI

package com.example.planpair;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.UserAdapter;
import com.example.planpair.models.CompatibilityScore;
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private boolean doubleBackPressed = false;
    private FrameLayout blurOverlay;
    private TextView welCurrentUserName;
    private RecyclerView recyclerView;
    private Button getPremium;
    private Boolean premium = false;
    private FirebaseFirestore firestore;
    private boolean isCurrentUserPremium = false;
    private List<User> userList;
    private UserAdapter userAdapter;
    private DocumentSnapshot currentUserSnapshot;
    private String currentUserGender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welCurrentUserName = findViewById(R.id.welCurrentUserName);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        blurOverlay = findViewById(R.id.blurOverlay);
        recyclerView = findViewById(R.id.recyclerView);
        getPremium = findViewById(R.id.getPremium);

        firestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPremium.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
        normalizeGenderInFirestore();

        loadCurrentUserProfile(); // Fetch username + premium

        // Bottom nav clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_notif) {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                return true;
            } else if (itemId == R.id.nav_liked || itemId == R.id.nav_chat) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                return true;
            } else if (itemId == R.id.nav_myprof) {
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                return true;
            }
            return false;
        });
    }
    private void normalizeGenderInFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("UsersData")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot) {
                        String gender = doc.getString("gender");
                        if (gender != null) {
                            String lowercaseGender = gender.toLowerCase();

                            // Update only if different
                            if (!gender.equals(lowercaseGender)) {
                                firestore.collection("UsersData")
                                        .document(doc.getId())
                                        .update("gender", lowercaseGender)
                                        .addOnSuccessListener(aVoid ->
                                                Log.d("GenderFix", "Updated gender for " + doc.getId()))
                                        .addOnFailureListener(e ->
                                                Log.e("GenderFix", "Failed to update " + doc.getId(), e));
                            }
                        }
                    }
//                    Toast.makeText(this, "Gender normalization complete", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("GenderFix", "Error fetching users", e);
                    Toast.makeText(this, "Error normalizing gender data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadCurrentUserProfile() {
        String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection("UsersData")
                .document(currentUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUserSnapshot = documentSnapshot;
                        String name = documentSnapshot.getString("username");
                        premium = documentSnapshot.getBoolean("isPremium");
                        currentUserGender = documentSnapshot.getString("gender");

                        if (name != null) {
                            welCurrentUserName.setText("Welcome, " + name);
                        }

                        isCurrentUserPremium = premium != null && premium;
                        getPremium.setVisibility(isCurrentUserPremium ? View.GONE : View.VISIBLE);
                    }

                    applyBlurEffect();
                    loadUsersFromFirestore(); // Load others after gender is known
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    Toast.makeText(this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    applyBlurEffect();
                });
    }

    private void loadUsersFromFirestore() {
        if (currentUserSnapshot == null || currentUserGender == null) {
            Toast.makeText(this, "Current user gender not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String targetGender = currentUserGender.equalsIgnoreCase("male") ? "female" : "male";
        targetGender = targetGender.toLowerCase();

        firestore.collection("UsersData")
                .whereEqualTo("gender",targetGender.toLowerCase())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    userList.clear();

                    for (DocumentSnapshot doc : querySnapshot) {
                        String otherUid = doc.getId();
                        if (otherUid.equals(currentUid)) continue;

                        String name = doc.getString("username");
                        String profileImageUrl = doc.getString("profileImageUrl");
                        Boolean isPremium = doc.getBoolean("isPremium");

                        // Age
                        int age = 0;
                        Object ageObj = doc.get("age");
                        if (ageObj instanceof Long) age = ((Long) ageObj).intValue();
                        else if (ageObj instanceof String) {
                            try { age = Integer.parseInt((String) ageObj); }
                            catch (NumberFormatException e) { Log.e(TAG, "Invalid age format for user: " + otherUid); }
                        }

                        if (name == null || profileImageUrl == null) continue;

                        // Compute compatibility
                        int compatibility = computeCompatibility(currentUserSnapshot, doc);

                        // Store compatibility in Firestore
                        firestore.collection("UsersData")
                                .document(currentUid)
                                .collection("CompatibilityScores")
                                .document(otherUid)
                                .set(new CompatibilityScore(compatibility))
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Stored score for: " + otherUid))
                                .addOnFailureListener(e -> Log.e(TAG, "Error storing score", e));

                        Boolean isLiked = doc.getBoolean("isLiked");
                        if (isLiked == null) isLiked = false;

                        // Show in UI
                        User user = new User(name, age, compatibility, profileImageUrl, isPremium != null && isPremium, doc.getId(),isLiked);
                        userList.add(user);
                    }

                    runOnUiThread(() -> {
                        userAdapter = new UserAdapter(HomeActivity.this, userList, isCurrentUserPremium, user -> {
                            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                            intent.putExtra("username", user.getName());
                            intent.putExtra("age", user.getAge());
                            intent.putExtra("profileImageUrl", user.getProfileImageUrl());
                            intent.putExtra("compatibility", user.getCompatibility());
                            intent.putExtra("uid", user.getUid());
                            intent.putExtra("isLiked", user.isLiked());
                            startActivity(intent);
                        });

                        recyclerView.setAdapter(userAdapter);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading users", e);
                    Toast.makeText(this, "Failed to load user list", Toast.LENGTH_SHORT).show();
                });
    }

    private int computeCompatibility(DocumentSnapshot currentUser, DocumentSnapshot otherUser) {
        int score = 0;
        int total = 0;

        // Compare list fields
        String[] listFields = {
                "travel", "creative_passions", "movies", "music",
                "marriage", "language", "food", "family_structure",
                "familyType", "WeddingType"
        };

        for (String field : listFields) {
            List<String> currentList = parseList(currentUser.get(field));
            List<String> otherList = parseList(otherUser.get(field));

            if (!currentList.isEmpty() && !otherList.isEmpty()) {
                total++;
                int commonCount = 0;
                for (String item : currentList) {
                    if (otherList.contains(item)) commonCount++;
                }
                score += (int) (((double) commonCount / currentList.size()) * 10);
            }
        }

        // Compare string fields Removed gender from here
        String[] stringFields = {
                "social_media", "religion", "degree", "Season",
                "community", "education"
        };

        for (String field : stringFields) {
            String a = currentUser.getString(field);
            String b = otherUser.getString(field);
            if (a != null && b != null) {
                total++;
                if (a.equalsIgnoreCase(b)) score += 10;
            }
        }

        // Special handling for gender
        String genderA = currentUser.getString("gender");
        String genderB = otherUser.getString("gender");

        if (genderA != null && genderB != null) {
            total++;
            if (!genderA.equalsIgnoreCase(genderB)) {
                score += 15; // Different gender → more compatible
            } else {
                score += 0;  // Same gender → no extra points
            }
        }


        if (total == 0) return 0;

        int finalScore = (int) ((score / (double) total) * 10); // Scale to 100
        return Math.min(finalScore, 100);
    }

    // Helper method to convert Firestore field to List<String>
    private List<String> parseList(Object rawField) {
        if (rawField instanceof List) {
            return (List<String>) rawField;
        } else if (rawField instanceof String) {
            String[] parts = ((String) rawField).split(",\\s*");
            return Arrays.asList(parts);
        } else {
            return new ArrayList<>();
        }
    }

    private void applyBlurEffect() {
        if (premium == null || !premium) {
            Drawable blurDrawable = ContextCompat.getDrawable(this, R.drawable.glass_blur_background);
            blurOverlay.setBackground(blurDrawable);
            blurOverlay.setAlpha(0.9f);
            blurOverlay.setVisibility(View.VISIBLE);
        } else {
            blurOverlay.setVisibility(View.GONE);
            blurOverlay.setBackground(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurrentUserProfile(); // Refresh user status and UI
    }


    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}
