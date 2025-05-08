
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



//satya
/*

package com.example.planpair;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.UserAdapter;
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackPressed = false;
    private RecyclerView recyclerView;
    private FrameLayout blurOverlay;
    private Button getPremium;
    private boolean isPremium = false;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        recyclerView = findViewById(R.id.recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        blurOverlay = findViewById(R.id.blurOverlay);
        getPremium = findViewById(R.id.getPremium);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            fetchPremiumStatusAndLoadUsers();
        }

        getPremium.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PaymentActivity.class));
        });

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

    private void fetchPremiumStatusAndLoadUsers() {
        db.collection("UsersData")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean premium = documentSnapshot.getBoolean("isPremium");
                        isPremium = premium != null && premium;
                        applyBlurEffect();
                        loadUsers(); // Load users only after premium status is known
                    } else {
                        isPremium = false;
                        applyBlurEffect();
                        loadUsers();
                    }
                })
                .addOnFailureListener(e -> {
                    isPremium = false;
                    applyBlurEffect();
                    loadUsers();
                    Toast.makeText(this, "Failed to fetch premium status", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUsers() {
        CollectionReference usersRef = db.collection("UsersData");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("username");
                    Long ageLong = document.getLong("age");
                    Long compatibilityLong = document.getLong("compatibility");
                    String imageUrl = document.getString("profileImageUrl");

                    if (name != null && ageLong != null && compatibilityLong != null && imageUrl != null) {
                        int age = ageLong.intValue();
                        int compatibility = compatibilityLong.intValue();
                        userList.add(new User(name, age, compatibility, imageUrl));
                    }
                }

                UserAdapter adapter = new UserAdapter(this, userList, isPremium, user -> {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("username", user.getName());
                    intent.putExtra("age", user.getAge());
                    intent.putExtra("compatibility", user.getCompatibility());
                    intent.putExtra("profileImageUrl", user.getProfileImageUrl());
                    startActivity(intent);
                });

                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
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
        if (currentUser != null) {
            fetchPremiumStatusAndLoadUsers(); // Refresh status and users
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed();
            return;
        }

        doubleBackPressed = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackPressed = false, 2000);
    }
}
*/
