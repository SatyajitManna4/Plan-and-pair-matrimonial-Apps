/*
package com.example.planpair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planpair.adapters.UserAdapter;
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackPressed = false;
    private RecyclerView recyclerView;
    private FrameLayout blurOverlay;
    private Button getPremium;
    private SharedPreferences prefs;
    private boolean isPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        blurOverlay = findViewById(R.id.blurOverlay);
        getPremium = findViewById(R.id.getPremium);

        // Check if user is premium
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        isPremium = prefs.getBoolean("isPremium", false);

        // Apply blur effect if not premium
        applyBlurEffect();

        // Handle "Get Premium" button click
        getPremium.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
            startActivity(intent);
        });

        // Load user data
        loadUsers();

        // Handle Bottom Navigation Clicks
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

    private void loadUsers() {
        List<User> userList = Arrays.asList(
                new User("Aman", 32, 85, R.drawable.sample_img1),
                new User("Amit", 30, 83, R.drawable.sample_img2),
                new User("Rohan", 36, 76, R.drawable.sample_img3),
                new User("Aman", 28, 94, R.drawable.sample_img4),
                new User("Rohit", 35, 98, R.drawable.sample_img5),
                new User("Sayak", 28, 90, R.drawable.sample_img6),
                new User("Sidharth", 29, 92, R.drawable.sample_img7)
        );

        // Pass `isPremium` to adapter
        UserAdapter adapter = new UserAdapter(this, userList, isPremium, user -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("userName", user.getName());
            intent.putExtra("userAge", user.getAge());
            intent.putExtra("compatibility", user.getCompatibility());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void applyBlurEffect() {
        if (!isPremium) {
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
        isPremium = prefs.getBoolean("isPremium", false);
        applyBlurEffect();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        // First back press
        this.doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        // Reset after 2 seconds
        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}


*/

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
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

    private void loadCurrentUserProfile() {
        String currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firestore.collection("UsersData")
                .document(currentUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("username");
                        premium = documentSnapshot.getBoolean("isPremium");

                        if (name != null) {
                            welCurrentUserName.setText("Welcome, "+name);
                        }

                        isCurrentUserPremium = premium != null && premium;
                        // Hide or show Get Premium button
                        getPremium.setVisibility(isCurrentUserPremium ? View.GONE : View.VISIBLE);
                    }

                    applyBlurEffect(); // Move inside after premium is fetched
                    loadUsersFromFirestore();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    Toast.makeText(this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
                    applyBlurEffect(); // Still apply default blur
                    loadUsersFromFirestore(); // still try loading users
                });
    }

    private void loadUsersFromFirestore() {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (currentUid == null) {
            Log.e(TAG, "Current user UID is null.");
            return;
        }

        firestore.collection("UsersData")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    userList.clear();

                    for (DocumentSnapshot doc : querySnapshot) {
                        if (doc.getId().equals(currentUid)) continue;

                        String name = doc.getString("username");
                        Object ageObj = doc.get("age");
                        Object compatibilityObj = doc.get("compatibility");
                        String profileImageUrl = doc.getString("profileImageUrl");
                        Boolean isPremium = doc.getBoolean("isPremium");

                        int age = 0;
                        if (ageObj instanceof Long) {
                            age = ((Long) ageObj).intValue();
                        } else if (ageObj instanceof String) {
                            try {
                                age = Integer.parseInt((String) ageObj);
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Invalid age format for user: " + doc.getId());
                            }
                        }

                        int compatibility = 0;
                        if (compatibilityObj instanceof Long) {
                            compatibility = ((Long) compatibilityObj).intValue();
                        }

                        if (name == null || profileImageUrl == null) {
                            Log.e(TAG, "Invalid user data: " + doc.getId());
                            continue;
                        }

                        User user = new User(name, age, compatibility, profileImageUrl, isPremium != null && isPremium);
                        userList.add(user);
                    }

                    runOnUiThread(() -> {
                        userAdapter = new UserAdapter(HomeActivity.this,userList,isCurrentUserPremium,user -> {
                            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                            intent.putExtra("username", user.getName());
                            intent.putExtra("age", user.getAge());
                            intent.putExtra("compatibility", user.getCompatibility());
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
