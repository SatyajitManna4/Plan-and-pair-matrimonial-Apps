package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.ChatListAdapter;
import com.example.planpair.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatListActivity extends AppCompatActivity {
    private EditText searchEditText;
    private TextView noResultText;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    private List<User> matchedUsers = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;
    private Set<String> fetchedUserIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        searchEditText = findViewById(R.id.searchBar);
        backButton = findViewById(R.id.back_btn);
        noResultText = findViewById(R.id.noResultText);
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatListAdapter(this, matchedUsers, user -> {
            try {
                Log.d("ChatListActivity", "Starting chat with: " + user.getUid());
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("otherUserId", user.getUid());
                intent.putExtra("otherUsername", user.getName());
//                intent.putExtra("profileImage", user.getProfileImageUrl());
//                intent.putExtra("isOnline", user.isOnline());
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ChatListActivity", "Error starting ChatActivity", e);
                Toast.makeText(this, "Failed to start chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(ChatListActivity.this,HomeActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        loadMatchedUsers();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_chat); // Default selected

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.nav_chat) {
                return true;
            }
            else if (itemId == R.id.nav_liked ) {
                startActivity(new Intent(ChatListActivity.this, LikedActivity.class));
                return true;}
            else if (itemId == R.id.nav_home ) {
                startActivity(new Intent(ChatListActivity.this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_myprof) {
                startActivity(new Intent(ChatListActivity.this, MyProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_planner) {
                startActivity(new Intent(ChatListActivity.this, PlannerActivity.class));
                return true;
            }
            return false;
        });
    }

    private void filterUsers(String query) {
        List<User> filteredList = new ArrayList<>();

        for (User user : matchedUsers) {
            if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }

        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            noResultText.setVisibility(View.VISIBLE);
        } else {
            noResultText.setVisibility(View.GONE);
        }
    }

    private void loadMatchedUsers() {
        db.collection("UsersData")
                .document(currentUserId)
                .collection("Matches")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Toast.makeText(this, "No matches found", Toast.LENGTH_SHORT).show();
                        Log.d("ChatListActivity", "No Matches subcollection documents found");
                        return;
                    }

                    Log.d("ChatListActivity", "Matches found: " + querySnapshot.size());
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String matchedUserId = document.getId();
                        Log.d("ChatListActivity", "Matched user id: " + matchedUserId);
                        if (!fetchedUserIds.contains(matchedUserId)) {
                            fetchedUserIds.add(matchedUserId);
                            fetchUserDetails(matchedUserId);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load matches", Toast.LENGTH_SHORT).show();
                    Log.e("ChatListActivity", "Error loading matches", e);
                });
    }

    private void fetchUserDetails(String userId) {
        db.collection("UsersData").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("username");
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        Boolean isPremium = documentSnapshot.getBoolean("isPremium");

                        int age = 0;
                        Object ageObj = documentSnapshot.get("age");
                        if (ageObj instanceof Long) {
                            age = ((Long) ageObj).intValue();
                        } else if (ageObj instanceof String) {
                            try {
                                age = Integer.parseInt((String) ageObj);
                            } catch (NumberFormatException e) {
                                Log.e("ChatListActivity", "Invalid age format for user: " + userId);
                            }
                        }

                        User user = new User(name, age, 0, profileImageUrl, isPremium != null && isPremium, userId, false);
                        user.setUid(userId);

                        // Get lastMessage from Matches subcollection
                        db.collection("UsersData")
                                .document(currentUserId)
                                .collection("Matches")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(matchDoc -> {
                                    if (matchDoc.exists()) {
                                        String lastMsg = matchDoc.getString("lastMessage");
                                        Long lastTime = matchDoc.getLong("timestamp");

                                        user.setLastMessage(lastMsg != null ? lastMsg : "No message yet");
                                        user.setLastMessageTime(lastTime != null ? lastTime : 0);
                                    } else {
                                        user.setLastMessage("No message yet");
                                        user.setLastMessageTime(0);
                                    }

                                    //Count unread messages
                                    db.collection("Chats")
                                            .whereEqualTo("senderId", userId)
                                            .whereEqualTo("receiverId", currentUserId)
                                            .whereEqualTo("isSeen", false)
                                            .get()
                                            .addOnSuccessListener(unreadSnap -> {
                                                user.setUnreadCount(unreadSnap.size());
                                                matchedUsers.add(user);
                                                sortUsersByRecentMessage();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("ChatListActivity", "Failed to fetch unread count", e);
                                                matchedUsers.add(user);
                                                sortUsersByRecentMessage();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("ChatListActivity", "Failed to get last message", e);
                                    matchedUsers.add(user);
                                    sortUsersByRecentMessage();
                                });
                    }
                }).addOnFailureListener(e -> Log.e("ChatListActivity", "User fetch failed", e));
    }


    private void sortUsersByRecentMessage() {
        matchedUsers.sort((u1, u2) -> Long.compare(u2.getLastMessageTime(), u1.getLastMessageTime()));
        adapter.notifyDataSetChanged();
    }

}
