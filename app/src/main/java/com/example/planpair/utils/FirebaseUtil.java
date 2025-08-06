package com.example.planpair.utils;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.planpair.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Existing method for Storage reference
    public static StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    // Get current logged-in user ID
    public static String currentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }

    // Fetch matched users for the current user
    public static void fetchMatchedUsers(MatchUsersCallback callback) {
        String currentUserId = currentUserId();
        if (currentUserId == null) {
            callback.onFailure("User not logged in");
            return;
        }

        db.collection("UsersData")
                .document(currentUserId)
                .collection("Matches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task<DocumentSnapshot>> userDetailTasks = new ArrayList<>();

                        for (DocumentSnapshot matchDoc : task.getResult()) {
                            String matchedUserId = matchDoc.getId();

                            // Fetch full user details
                            Task<DocumentSnapshot> userTask = db.collection("UsersData")
                                    .document(matchedUserId)
                                    .get();

                            userDetailTasks.add(userTask);
                        }

                        // Wait for all user detail tasks to complete
                        com.google.android.gms.tasks.Tasks.whenAllSuccess(userDetailTasks)
                                .addOnCompleteListener(userDetailsTask -> {
                                    if (userDetailsTask.isSuccessful()) {
                                        List<Object> results = userDetailsTask.getResult();
                                        List<User> matchedUsers = new ArrayList<>();

                                        if (results != null) {
                                            for (Object obj : results) {
                                                DocumentSnapshot doc = (DocumentSnapshot) obj;
                                                if (doc.exists()) {
                                                    User user = doc.toObject(User.class);
                                                    if (user != null) {
                                                        user.setUid(doc.getId());
                                                        matchedUsers.add(user);
                                                    }
                                                }
                                            }
                                            callback.onSuccess(matchedUsers);
                                        } else {
                                            callback.onFailure("No matched users found");
                                        }
                                    } else {
                                        callback.onFailure("Failed to fetch matched users");
                                    }
                                });
                    } else {
                        callback.onFailure("Failed to get matches");
                    }
                });
    }

    // Callback interface for fetching matched users
    public interface MatchUsersCallback {
        void onSuccess(List<User> matchedUsers);
        void onFailure(String errorMessage);
    }

}