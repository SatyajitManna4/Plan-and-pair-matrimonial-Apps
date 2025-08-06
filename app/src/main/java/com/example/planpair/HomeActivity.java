/*

package com.example.planpair;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private ImageView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welCurrentUserName = findViewById(R.id.welCurrentUserName);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        blurOverlay = findViewById(R.id.blurOverlay);
        recyclerView = findViewById(R.id.recyclerView);
        getPremium = findViewById(R.id.getPremium);
        notification=findViewById(R.id.notification);

        firestore = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPremium.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
        normalizeGenderInFirestore();

//        loadCurrentUserProfile(); // Fetch username + premium (off as twice account fetch 1>onCreate 2> super.onResume() method)

        // Bottom nav clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_home) {
                return true;
            }
            else if (itemId == R.id.nav_liked ) {
                startActivity(new Intent(HomeActivity.this, LikedActivity.class));
                return true;
            }else if (itemId == R.id.nav_chat ) {
                startActivity(new Intent(HomeActivity.this, ChatListActivity.class));
                return true;
            } else if (itemId == R.id.nav_myprof) {
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_planner) {
                if (isCurrentUserPremium) {
                    startActivity(new Intent(HomeActivity.this, PlannerActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Take premium", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
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

        firestore.collection("UsersData")
                .whereEqualTo("gender", targetGender.toLowerCase())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    userList.clear(); // Clear existing

                    List<DocumentSnapshot> allUsers = new ArrayList<>();

                    for (DocumentSnapshot doc : querySnapshot) {
                        String otherUid = doc.getId();
                        if (!otherUid.equals(currentUid)) {
                            allUsers.add(doc);
                        }
                    }

                    if (allUsers.isEmpty()) {
                        userAdapter.notifyDataSetChanged(); // Notify change even if empty
                        return;
                    }

                    firestore.collection("UsersData").document(currentUid)
                            .collection("Likes")
                            .get()
                            .addOnSuccessListener(likesSnapshot -> {
                                List<String> likedUserIds = new ArrayList<>();
                                for (DocumentSnapshot likeDoc : likesSnapshot) {
                                    likedUserIds.add(likeDoc.getId());
                                }

                                for (DocumentSnapshot doc : allUsers) {
                                    try {
                                        String otherUid = doc.getId();
                                        String name = doc.getString("username");
                                        String profileImageUrl = doc.getString("profileImageUrl");
                                        Boolean isPremium = doc.getBoolean("isPremium");

                                        int age = 0;
                                        Object ageObj = doc.get("age");
                                        if (ageObj instanceof Long) {
                                            age = ((Long) ageObj).intValue();
                                        } else if (ageObj instanceof String) {
                                            try {
                                                age = Integer.parseInt((String) ageObj);
                                            } catch (NumberFormatException e) {
                                                Log.e(TAG, "Invalid age format for user: " + otherUid);
                                            }
                                        }

                                        if (name == null || profileImageUrl == null) continue;

                                        int compatibility = computeCompatibility(currentUserSnapshot, doc);

                                        firestore.collection("UsersData")
                                                .document(currentUid)
                                                .collection("CompatibilityScores")
                                                .document(otherUid)
                                                .set(new CompatibilityScore(compatibility));

                                        boolean isLiked = likedUserIds.contains(otherUid);

                                        User user = new User(name, age, compatibility, profileImageUrl,
                                                isPremium != null && isPremium, otherUid, isLiked);
                                        userList.add(user);
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error processing user: ", e);
                                    }
                                }

                                // Sort and refresh UI
                                runOnUiThread(() -> {
                                    userList.sort((u1, u2) -> Integer.compare(u2.getCompatibility(), u1.getCompatibility()));
                                    if (userAdapter == null) {
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
                                    } else {
                                        userAdapter.notifyDataSetChanged(); // Notify updated data
                                    }
                                });
                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to fetch likes", e));

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
*/



//s code but not working

package com.example.planpair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int PROFILE_REQUEST = 1001;

    private FrameLayout blurOverlay;
    private TextView welCurrentUserName;
    private RecyclerView recyclerView;
    private Button getPremium;
    private ImageView notification;

    private FirebaseFirestore firestore;
    private boolean isCurrentUserPremium = false;
    private boolean doubleBackPressed = false;

    private DocumentSnapshot currentUserSnapshot;
    private String currentUserGender;
    private final List<User> userList = new ArrayList<>();
    private final Set<String> addedUserIds = new HashSet<>();

    private UserAdapter userAdapter;

    private final ActivityResultLauncher<Intent> paymentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    checkPremiumStatus();
                }
            });

    private final ActivityResultLauncher<Intent> profileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String uid = result.getData().getStringExtra("uid");
                    boolean isLiked = result.getData().getBooleanExtra("isLiked", false);

                    for (int i = 0; i < userList.size(); i++) {
                        User user = userList.get(i);
                        if (user.getUid().equals(uid)) {
                            user.setLiked(isLiked);
                            userAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firestore = FirebaseFirestore.getInstance();

        welCurrentUserName = findViewById(R.id.welCurrentUserName);
        blurOverlay = findViewById(R.id.blurOverlay);
        recyclerView = findViewById(R.id.recyclerView);
        getPremium = findViewById(R.id.getPremium);
        notification = findViewById(R.id.notification);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPremium.setOnClickListener(v ->
                paymentLauncher.launch(new Intent(this, PaymentActivity.class)));

        notification.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Bottom nav clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_home) {
                return true;
            }
            else if (itemId == R.id.nav_liked ) {
                startActivity(new Intent(HomeActivity.this, LikedActivity.class));
                return true;
            }else if (itemId == R.id.nav_chat ) {
                startActivity(new Intent(HomeActivity.this, ChatListActivity.class));
                return true;
            } else if (itemId == R.id.nav_myprof) {
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_planner) {
                if (isCurrentUserPremium) {
                    startActivity(new Intent(HomeActivity.this, PlannerActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Take premium", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
        loadCurrentUser();
    }

    private void loadCurrentUser() {
        String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection("UsersData").document(currentUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;

                    currentUserSnapshot = snapshot;
                    currentUserGender = snapshot.getString("gender");
                    isCurrentUserPremium = Boolean.TRUE.equals(snapshot.getBoolean("isPremium"));

                    welCurrentUserName.setText("Welcome, " + snapshot.getString("username"));

                    getPremium.setVisibility(isCurrentUserPremium ? View.GONE : View.VISIBLE);
                    applyBlurEffect();
                    loadOtherUsers(currentUid);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load current user", e);
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    applyBlurEffect();
                });
    }

    private void loadOtherUsers(String currentUid) {
        if (currentUserGender == null) return;

        String targetGender = currentUserGender.equalsIgnoreCase("male") ? "female" : "male";

        firestore.collection("UsersData")
                .whereEqualTo("gender", targetGender.toLowerCase())
                .get()
                .addOnSuccessListener(snapshot -> processUsers(snapshot, currentUid))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading users", e);
                    Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                });
    }

    private void processUsers(QuerySnapshot snapshot, String currentUid) {
        userList.clear();
        addedUserIds.clear();

        if (snapshot == null || snapshot.isEmpty()) return;

        int totalUsers = snapshot.size();
        AtomicInteger processed = new AtomicInteger(0);

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            String uid = doc.getId();
            if (uid.equals(currentUid)) {
                processed.incrementAndGet();
                continue;
            }

            String name = doc.getString("username");
            String profileImageUrl = doc.getString("profileImageUrl");
            boolean isPremium = Boolean.TRUE.equals(doc.getBoolean("isPremium"));

            if (name == null || profileImageUrl == null) {
                processed.incrementAndGet();
                continue;
            }

            int age = 0;
            Object ageObj = doc.get("age");
            if (ageObj instanceof Long) age = ((Long) ageObj).intValue();
            else if (ageObj instanceof String) {
                try {
                    age = Integer.parseInt((String) ageObj);
                } catch (Exception ignored) {}
            }

            int compatibility = computeCompatibility(currentUserSnapshot, doc);

            firestore.collection("UsersData").document(currentUid)
                    .collection("CompatibilityScores").document(uid)
                    .set(new CompatibilityScore(compatibility));

            final int finalAge = age;

            firestore.collection("UsersData").document(currentUid)
                    .collection("Likes").document(uid)
                    .get()
                    .addOnSuccessListener(likeDoc -> {
                        boolean isLiked = likeDoc.exists();
                        if (!addedUserIds.contains(uid)) {
                            User user = new User(name, finalAge, compatibility, profileImageUrl, isPremium, uid, isLiked);
                            userList.add(user);
                            addedUserIds.add(uid);
                        }
                        if (processed.incrementAndGet() >= totalUsers - 1) updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error checking like status", e);
                        if (processed.incrementAndGet() >= totalUsers - 1) updateUI();
                    });
        }
    }

    private int computeCompatibility(DocumentSnapshot userA, DocumentSnapshot userB) {
        int score = 0, total = 0;

        String[] listFields = {"travel", "creative_passions", "movies", "music", "marriage", "language", "food", "family_structure", "familyType", "WeddingType"};

        for (String field : listFields) {
            List<String> a = parseList(userA.get(field));
            List<String> b = parseList(userB.get(field));
            if (!a.isEmpty() && !b.isEmpty()) {
                total++;
                long match = a.stream().filter(b::contains).count();
                score += (int) ((match / (double) a.size()) * 10);
            }
        }

        String[] stringFields = {"social_media", "religion", "degree", "Season", "community", "education"};

        for (String field : stringFields) {
            String a = userA.getString(field);
            String b = userB.getString(field);
            if (a != null && b != null) {
                total++;
                if (a.equalsIgnoreCase(b)) score += 10;
            }
        }

        String genderA = userA.getString("gender");
        String genderB = userB.getString("gender");
        if (genderA != null && genderB != null && !genderA.equalsIgnoreCase(genderB)) {
            total++;
            score += 15;
        }

        return total == 0 ? 0 : Math.min((int) ((score / (double) (total * 10)) * 100), 100);
    }

    private List<String> parseList(Object obj) {
        if (obj instanceof List) return (List<String>) obj;
        if (obj instanceof String) return Arrays.asList(((String) obj).split(",\\s*"));
        return new ArrayList<>();
    }

    private void updateUI() {
        runOnUiThread(() -> {
            userList.sort((a, b) -> Integer.compare(b.getCompatibility(), a.getCompatibility()));
            userAdapter = new UserAdapter(this, userList, isCurrentUserPremium, user -> {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("uid", user.getUid());
                intent.putExtra("username", user.getName());
                intent.putExtra("age", user.getAge());
                intent.putExtra("profileImageUrl", user.getProfileImageUrl());
                intent.putExtra("compatibility", user.getCompatibility());
                intent.putExtra("isLiked", user.isLiked());
                profileLauncher.launch(intent);
            });
            recyclerView.setAdapter(userAdapter);
        });
    }

    private void applyBlurEffect() {
        if (!isCurrentUserPremium) {
            blurOverlay.setVisibility(View.VISIBLE);
            blurOverlay.setBackground(ContextCompat.getDrawable(this, R.drawable.glass_blur_background));
            blurOverlay.setAlpha(0.9f);
        } else {
            blurOverlay.setVisibility(View.GONE);
            blurOverlay.setBackground(null);
        }
    }

    private void checkPremiumStatus() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("UsersData").document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    boolean isNowPremium = Boolean.TRUE.equals(snapshot.getBoolean("isPremium"));
                    if (isNowPremium && !isCurrentUserPremium) {
                        isCurrentUserPremium = true;
                        applyBlurEffect();
                        getPremium.setVisibility(View.GONE);
                        updateUI();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
        } else {
            this.doubleBackPressed = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
        }
    }
}

/*
package com.example.planpair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.planpair.adapters.UserAdapter;
import com.example.planpair.models.CompatibilityScore;
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int PROFILE_REQUEST = 1001;

    private FrameLayout blurOverlay;
    private TextView welCurrentUserName;
    private RecyclerView recyclerView;
    private Button getPremium;
    private ImageView notification;

    private FirebaseFirestore firestore;
    private boolean isCurrentUserPremium = false;
    private boolean doubleBackPressed = false;

    private DocumentSnapshot currentUserSnapshot;
    private String currentUserGender;
    private final List<User> userList = new ArrayList<>();
    private final Set<String> addedUserIds = new HashSet<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    private UserAdapter userAdapter;

    private final ActivityResultLauncher<Intent> paymentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    checkPremiumStatus();
                }
            });

    private final ActivityResultLauncher<Intent> profileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String uid = result.getData().getStringExtra("uid");
                    boolean isLiked = result.getData().getBooleanExtra("isLiked", false);

                    for (int i = 0; i < userList.size(); i++) {
                        User user = userList.get(i);
                        if (user.getUid().equals(uid)) {
                            user.setLiked(isLiked);
                            userAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firestore = FirebaseFirestore.getInstance();

        welCurrentUserName = findViewById(R.id.welCurrentUserName);
        blurOverlay = findViewById(R.id.blurOverlay);
        recyclerView = findViewById(R.id.recyclerView);
        getPremium = findViewById(R.id.getPremium);
        notification = findViewById(R.id.notification);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPremium.setOnClickListener(v ->
                paymentLauncher.launch(new Intent(this, PaymentActivity.class)));

        notification.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        // Bottom nav clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_home) {
                return true;
            }
            else if (itemId == R.id.nav_liked ) {
                startActivity(new Intent(HomeActivity.this, LikedActivity.class));
                return true;
            }else if (itemId == R.id.nav_chat ) {
                startActivity(new Intent(HomeActivity.this, ChatListActivity.class));
                return true;
            } else if (itemId == R.id.nav_myprof) {
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_planner) {
                if (isCurrentUserPremium) {
                    startActivity(new Intent(HomeActivity.this, PlannerActivity.class));
                } else {
                    Toast.makeText(HomeActivity.this, "Take premium", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadCurrentUser(); // Reload current user and user list
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Only allow swipe refresh when the first item is fully visible
                swipeRefreshLayout.setEnabled(!recyclerView.canScrollVertically(-1));
            }
        });


    }

    private void loadCurrentUser() {
        String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection("UsersData").document(currentUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;

                    currentUserSnapshot = snapshot;
                    currentUserGender = snapshot.getString("gender");
                    isCurrentUserPremium = Boolean.TRUE.equals(snapshot.getBoolean("isPremium"));

                    welCurrentUserName.setText("Welcome, " + snapshot.getString("username"));

                    getPremium.setVisibility(isCurrentUserPremium ? View.GONE : View.VISIBLE);
                    applyBlurEffect();
                    loadOtherUsers(currentUid);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load current user", e);
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    applyBlurEffect();
                });
    }

    private void loadOtherUsers(String currentUid) {
        if (currentUserGender == null) return;

        String targetGender = currentUserGender.equalsIgnoreCase("male") ? "female" : "male";

        firestore.collection("UsersData")
                .whereEqualTo("gender", targetGender.toLowerCase())
                .whereEqualTo("isBioCompleted", true) // ✅ Add this
                .get()
                .addOnSuccessListener(snapshot -> processUsers(snapshot, currentUid))

                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading users", e);
                    Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                });
    }

    private void processUsers(QuerySnapshot snapshot, String currentUid) {
        userList.clear();
        addedUserIds.clear();

        if (snapshot == null || snapshot.isEmpty()) return;

        int totalUsers = snapshot.size();
        AtomicInteger processed = new AtomicInteger(0);

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            String uid = doc.getId();
            if (uid.equals(currentUid)) {
                processed.incrementAndGet();
                continue;
            }

            String name = doc.getString("username");
            String profileImageUrl = doc.getString("profileImageUrl");
            boolean isPremium = Boolean.TRUE.equals(doc.getBoolean("isPremium"));

            if (name == null || profileImageUrl == null) {
                processed.incrementAndGet();
                continue;
            }

            int age = 0;
            Object ageObj = doc.get("age");
            if (ageObj instanceof Long) age = ((Long) ageObj).intValue();
            else if (ageObj instanceof String) {
                try {
                    age = Integer.parseInt((String) ageObj);
                } catch (Exception ignored) {}
            }

            int compatibility = computeCompatibility(currentUserSnapshot, doc);

            firestore.collection("UsersData").document(currentUid)
                    .collection("CompatibilityScores").document(uid)
                    .set(new CompatibilityScore(compatibility));

            final int finalAge = age;

            firestore.collection("UsersData").document(currentUid)
                    .collection("Likes").document(uid)
                    .get()
                    .addOnSuccessListener(likeDoc -> {
                        boolean isLiked = likeDoc.exists();
                        if (!addedUserIds.contains(uid)) {
                            User user = new User(name, finalAge, compatibility, profileImageUrl, isPremium, uid, isLiked);
                            userList.add(user);
                            addedUserIds.add(uid);
                        }
                        if (processed.incrementAndGet() >= totalUsers - 1) updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error checking like status", e);
                        if (processed.incrementAndGet() >= totalUsers - 1) updateUI();
                    });
        }
    }

    private int computeCompatibility(DocumentSnapshot userA, DocumentSnapshot userB) {
        int score = 0, total = 0;

        String[] listFields = {"travel", "creative_passions", "movies", "music", "marriage", "language", "food", "family_structure", "familyType", "WeddingType"};

        for (String field : listFields) {
            List<String> a = parseList(userA.get(field));
            List<String> b = parseList(userB.get(field));
            if (!a.isEmpty() && !b.isEmpty()) {
                total++;
                long match = a.stream().filter(b::contains).count();
                score += (int) ((match / (double) a.size()) * 10);
            }
        }

        String[] stringFields = {"social_media", "religion", "degree", "Season", "community", "education"};

        for (String field : stringFields) {
            String a = userA.getString(field);
            String b = userB.getString(field);
            if (a != null && b != null) {
                total++;
                if (a.equalsIgnoreCase(b)) score += 10;
            }
        }

        String genderA = userA.getString("gender");
        String genderB = userB.getString("gender");
        if (genderA != null && genderB != null && !genderA.equalsIgnoreCase(genderB)) {
            total++;
            score += 15;
        }

        return total == 0 ? 0 : Math.min((int) ((score / (double) (total * 10)) * 100), 100);
    }

    private List<String> parseList(Object obj) {
        if (obj instanceof List) return (List<String>) obj;
        if (obj instanceof String) return Arrays.asList(((String) obj).split(",\\s*"));
        return new ArrayList<>();
    }

    private void updateUI() {
        runOnUiThread(() -> {
            userList.sort((a, b) -> Integer.compare(b.getCompatibility(), a.getCompatibility()));

            if (userAdapter == null) {
                userAdapter = new UserAdapter(this, userList, isCurrentUserPremium, user -> {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("uid", user.getUid());
                    intent.putExtra("username", user.getName());
                    intent.putExtra("age", user.getAge());
                    intent.putExtra("profileImageUrl", user.getProfileImageUrl());
                    intent.putExtra("compatibility", user.getCompatibility());
                    intent.putExtra("isLiked", user.isLiked());
                    profileLauncher.launch(intent);
                });
                recyclerView.setAdapter(userAdapter);
            } else {
                userAdapter.notifyDataSetChanged(); // Safe UI update
            }

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void applyBlurEffect() {
        if (!isCurrentUserPremium) {
            blurOverlay.setVisibility(View.VISIBLE);
            blurOverlay.setBackground(ContextCompat.getDrawable(this, R.drawable.glass_blur_background));
            blurOverlay.setAlpha(0.9f);
        } else {
            blurOverlay.setVisibility(View.GONE);
            blurOverlay.setBackground(null);
        }
    }

    private void checkPremiumStatus() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("UsersData").document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    boolean isNowPremium = Boolean.TRUE.equals(snapshot.getBoolean("isPremium"));
                    if (isNowPremium && !isCurrentUserPremium) {
                        isCurrentUserPremium = true;
                        applyBlurEffect();
                        getPremium.setVisibility(View.GONE);
                        updateUI();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
        } else {
            this.doubleBackPressed = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
        }
    }
}
*/