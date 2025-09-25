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

//    private int currentUserAge = 0;

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
                .whereEqualTo("isBioCompleted", true) // ✅ keep your filter
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error listening for users", e);
                        Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshot != null) {
                        processUsers(snapshot, currentUid); // 🔄 Will auto-refresh on any change
                    }
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
            // Sort: compatibility first (you can add age sorting if you want)
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
                userAdapter.notifyDataSetChanged();
            }

            /* if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }*/
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
