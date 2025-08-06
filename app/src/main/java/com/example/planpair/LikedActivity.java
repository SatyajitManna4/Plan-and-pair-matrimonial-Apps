package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.LikedUserAdapter;
import com.example.planpair.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LikedActivity extends AppCompatActivity {

    private static final String TAG = "LikedActivity";
    private RecyclerView recyclerView;
    private LikedUserAdapter likedUserAdapter;
    private List<User> likedUsers;
    private FirebaseFirestore db;
    private String currentUid;
    private Button likedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        recyclerView = findViewById(R.id.recyclerView);
        likedButton = findViewById(R.id.likedButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        likedUsers = new ArrayList<>();
        likedUserAdapter = new LikedUserAdapter(likedUsers);
        recyclerView.setAdapter(likedUserAdapter);

        db = FirebaseFirestore.getInstance();
        currentUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        loadLikedUsers();
        likedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LikedYouActivity.class));
        });
    }

    private void loadLikedUsers() {
        db.collection("UsersData")
                .document(currentUid)
                .collection("Likes")
                .get()
                .addOnSuccessListener(likesSnapshot -> {
                    if (likesSnapshot.isEmpty()) {
                        Toast.makeText(this, "You haven't liked anyone yet.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<User> tempUserList = new ArrayList<>();
                    int totalLikes = likesSnapshot.size();
                    final int[] loadedCount = {0};

                    for (DocumentSnapshot likeDoc : likesSnapshot.getDocuments()) {
                        String likedUserId = likeDoc.getId();

                        db.collection("UsersData")
                                .document(likedUserId)
                                .get()
                                .addOnSuccessListener(userSnapshot -> {
                                    if (userSnapshot.exists()) {
                                        String name = userSnapshot.getString("username");
                                        String profileImageUrl = userSnapshot.getString("profileImageUrl");
                                        Boolean isPremium = userSnapshot.getBoolean("isPremium");

                                        int age = 0;
                                        Object ageObj = userSnapshot.get("age");
                                        if (ageObj instanceof Long)
                                            age = ((Long) ageObj).intValue();
                                        else if (ageObj instanceof String) {
                                            try {
                                                age = Integer.parseInt((String) ageObj);
                                            } catch (NumberFormatException e) {
                                                Log.e(TAG, "Invalid age format for " + likedUserId);
                                            }
                                        }

                                        int finalAge = age;
                                        db.collection("UsersData")
                                                .document(currentUid)
                                                .collection("CompatibilityScores")
                                                .document(likedUserId)
                                                .get()
                                                .addOnSuccessListener(scoreDoc -> {
                                                    int compatibility = 0;
                                                    if (scoreDoc.exists()) {
                                                        Object scoreObj = scoreDoc.get("score");
                                                        if (scoreObj instanceof Long) {
                                                            compatibility = ((Long) scoreObj).intValue();
                                                        }
                                                    }

                                                    User user = new User(name, finalAge, compatibility,
                                                            profileImageUrl);

                                                    tempUserList.add(user);

                                                    loadedCount[0]++;
                                                    if (loadedCount[0] == totalLikes) {
                                                        // All users loaded, update main list and notify adapter once on UI thread
                                                        runOnUiThread(() -> {
                                                            likedUsers.clear();
                                                            likedUsers.addAll(tempUserList);
                                                            likedUserAdapter.notifyDataSetChanged();
                                                        });
                                                    }
                                                }).addOnFailureListener(e -> {
                                                    Log.e(TAG, "Failed to fetch compatibility for " + likedUserId, e);
                                                    loadedCount[0]++;
                                                });
                                    } else {
                                        loadedCount[0]++;
                                    }
                                }).addOnFailureListener(e -> {
                                    Log.e(TAG, "Failed to fetch user: " + likedUserId, e);
                                    loadedCount[0]++;
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch liked users", e);
                    Toast.makeText(this, "Error loading liked users", Toast.LENGTH_SHORT).show();
                });
    }
}
