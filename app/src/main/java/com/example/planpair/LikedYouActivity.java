package com.example.planpair;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class LikedYouActivity extends AppCompatActivity {

    private static final String TAG = "LikedYouActivity";

    private TextView welcomeText;
    private RecyclerView recyclerViewYou;
    private FirebaseFirestore db;
    private String currentUid;
    private LikedUserAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_you);

        welcomeText = findViewById(R.id.welcomeText);
        recyclerViewYou = findViewById(R.id.recyclerViewYou);
        recyclerViewYou.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        adapter = new LikedUserAdapter(userList);
        recyclerViewYou.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadLikedYouUsers();
    }

    private void loadLikedYouUsers() {
        db.collection("UsersData")
                .document(currentUid)
                .collection("LikedBy")
                .get()
                .addOnSuccessListener(query -> {
                    if (query.isEmpty()) {
                        welcomeText.setText("No one has liked you yet.");
                        return;
                    }

                    int total = query.size();
                    final int[] loadedCount = {0};
                    List<User> tempUsers = new ArrayList<>();

                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String likerUid = doc.getId();

                        db.collection("UsersData").document(likerUid)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    if (userDoc.exists()) {
                                        String name = userDoc.getString("username");
                                        String profileImageUrl = userDoc.getString("profileImageUrl");

                                        // Parse age
                                        int age = 0;
                                        Object ageObj = userDoc.get("age");
                                        if (ageObj instanceof Long) {
                                            age = ((Long) ageObj).intValue();
                                        } else if (ageObj instanceof String) {
                                            try {
                                                age = Integer.parseInt((String) ageObj);
                                            } catch (NumberFormatException e) {
                                                age = 0;
                                            }
                                        }

                                        // Get compatibility score from current user's view
                                        int finalAge = age;
                                        db.collection("UsersData").document(currentUid)
                                                .collection("CompatibilityScores")
                                                .document(likerUid)
                                                .get()
                                                .addOnSuccessListener(scoreDoc -> {
                                                    int compatibility = 0;
                                                    if (scoreDoc.exists()) {
                                                        Object scoreObj = scoreDoc.get("score");
                                                        if (scoreObj instanceof Long) {
                                                            compatibility = ((Long) scoreObj).intValue();
                                                        }
                                                    }

                                                    // Create User object
                                                    User user = new User(name, finalAge, compatibility, profileImageUrl);
                                                    user.setUid(likerUid);
                                                    tempUsers.add(user);

                                                    loadedCount[0]++;
                                                    if (loadedCount[0] == total) {
                                                        userList.clear();
                                                        userList.addAll(tempUsers);
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                });
                                    } else {
                                        loadedCount[0]++;
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading likedBy users", e));
    }
}
