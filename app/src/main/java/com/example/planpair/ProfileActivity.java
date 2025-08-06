package com.example.planpair;

import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.planpair.models.NotificationModel;
import com.example.planpair.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView, likeButtonProfile, chatIcon;
    private TextView currUserName, userReligion, userAge, chatText, communityText, birthDetailsText,
            familyBgText, professionIncomeText, educationText, relocateText,
            marriageTimelineText, languageText, bondShow;
    private CompatibilityScoreView compatibilityView;
    private LinearLayout chatSection;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String uid, name, profileImageUrl;
    private int age, compatibilityScore;
    private boolean isLiked = false;
    private boolean likeStatusChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        initViews();
        getIntentData();
        populateStaticInfo();
        loadProfileImage();
        setListeners();

        if (uid != null && !uid.isEmpty()) {
            fetchAndDisplayUserData(uid);
        }
    }

    private void initViews() {
        profileImageView = findViewById(R.id.profileImageView);
        likeButtonProfile = findViewById(R.id.likeButtonProfile);
        chatIcon = findViewById(R.id.chatIcon);
        currUserName = findViewById(R.id.currUserName);
        userReligion = findViewById(R.id.userReligion);
        userAge = findViewById(R.id.userAge);
        chatText = findViewById(R.id.chatText);
        communityText = findViewById(R.id.communityText);
        birthDetailsText = findViewById(R.id.birthDetailsText);
        familyBgText = findViewById(R.id.familyBgText);
        professionIncomeText = findViewById(R.id.professionIncomeText);
        educationText = findViewById(R.id.educationText);
        relocateText = findViewById(R.id.relocateText);
        marriageTimelineText = findViewById(R.id.marriageTimelineText);
        languageText = findViewById(R.id.languageText);
        chatSection = findViewById(R.id.chatSection);
        compatibilityView = findViewById(R.id.compatibilityView);
        bondShow = findViewById(R.id.bondShow);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        name = intent.getStringExtra("username");
        age = intent.getIntExtra("age", 0);
        profileImageUrl = intent.getStringExtra("profileImageUrl");
        compatibilityScore = intent.getIntExtra("compatibility", 0);
        isLiked = intent.getBooleanExtra("isLiked", false);
    }

    private void populateStaticInfo() {
        currUserName.setText(name);
        userAge.setText("Age : " + age);
        chatText.setText("Chat with\n" + name);
        compatibilityView.setCompatibilityScore(compatibilityScore);

        bondShow.setText(
                compatibilityScore >= 50 ?
                        "Your bond with " + name + " is this strong!" :
                        "Your bond with " + name + " is weak."
        );
    }

    private void loadProfileImage() {
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.profile_icon)
                .into(profileImageView);
    }

    private void setListeners() {
        updateLikeUI();

        likeButtonProfile.setOnClickListener(v -> {
            if (!isLiked) {
                likeUser();
            } else {
                unlikeUser();
            }
        });

        chatSection.setOnClickListener(v -> {
            checkMutualMatch(uid, isMatch -> {
                if (isMatch) {
                    Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                    intent.putExtra("otherUserId", uid);
                    intent.putExtra("otherUsername", name);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Chat is only available after a mutual match.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateLikeUI() {
        likeButtonProfile.setImageResource(
                isLiked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24
        );
        chatSection.setVisibility(isLiked ? View.VISIBLE : View.GONE);
    }

    private void likeUser() {
        String currentUserId = auth.getCurrentUser().getUid();
        isLiked = true;
        likeStatusChanged = true;
        updateLikeUI();

        db.collection("UsersData").document(currentUserId)
                .collection("Likes").document(uid).set(new HashMap<>());

        db.collection("UsersData").document(uid)
                .collection("LikedBy").document(currentUserId).set(new HashMap<>());

        sendLikeNotification();
        checkMutualMatch(uid, isMatch -> {
            if (isMatch) {
                checkAndCreateChatListEntry(uid, name, profileImageUrl);
            }
        });

        Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
    }

    private void unlikeUser() {
        String currentUserId = auth.getCurrentUser().getUid();
        isLiked = false;
        likeStatusChanged = true;
        updateLikeUI();

        db.collection("UsersData").document(currentUserId)
                .collection("Likes").document(uid).delete();

        db.collection("UsersData").document(uid)
                .collection("LikedBy").document(currentUserId).delete();

        removeLikeNotification();

        Toast.makeText(this, "Unliked", Toast.LENGTH_SHORT).show();
    }

    private void checkMutualMatch(String otherUserId, MatchCallback callback) {
        String currentUserId = auth.getCurrentUser().getUid();
        db.collection("UsersData").document(otherUserId)
                .collection("Likes").document(currentUserId)
                .get()
                .addOnSuccessListener(doc -> callback.onMatchChecked(doc.exists()))
                .addOnFailureListener(e -> callback.onMatchChecked(false));
    }

    interface MatchCallback {
        void onMatchChecked(boolean isMatch);
    }

    private void checkAndCreateChatListEntry(String uid, String name, String profileImageUrl) {
        String currentUserId = auth.getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        User otherUser = new User(uid, name, profileImageUrl, "", timestamp, false, 0);
        User currentUser = new User(currentUserId, auth.getCurrentUser().getDisplayName(), "", "", timestamp, true, 0);

        db.collection("ChatList").document(currentUserId)
                .collection("MatchedUsers").document(uid).set(otherUser);

        db.collection("ChatList").document(uid)
                .collection("MatchedUsers").document(currentUserId).set(currentUser);

        Map<String, Object> matchData = new HashMap<>();
        matchData.put("matched", true);
        matchData.put("timestamp", timestamp);

        db.collection("UsersData").document(currentUserId)
                .collection("Matches").document(uid).set(matchData);

        db.collection("UsersData").document(uid)
                .collection("Matches").document(currentUserId).set(matchData);
    }

    private void sendLikeNotification() {
        String currentUid = auth.getCurrentUser().getUid();

        db.collection("UsersData").document(currentUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String senderName = snapshot.getString("username");
                        String senderImageUrl = snapshot.getString("profileImageUrl");

                        db.collection("UsersData").document(uid)
                                .collection("Likes").document(currentUid)
                                .get()
                                .addOnSuccessListener(doc -> {
                                    boolean mutual = doc.exists();
                                    String message = mutual ?
                                            "It's a Match! You and " + senderName + " liked each other!" :
                                            senderName + " liked your profile!";

                                    NotificationModel notification = new NotificationModel(
                                            currentUid, senderName, senderImageUrl, message, System.currentTimeMillis());

                                    db.collection("UsersData").document(uid)
                                            .collection("Notifications").document(currentUid).set(notification);
                                });
                    }
                });
    }

    private void removeLikeNotification() {
        String currentUid = auth.getCurrentUser().getUid();
        db.collection("UsersData").document(uid)
                .collection("Notifications").document(currentUid).delete();
    }

    private void fetchAndDisplayUserData(String uid) {
        db.collection("UsersData").document(uid)
                .get()
                .addOnSuccessListener(this::populateUserDetails)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void populateUserDetails(DocumentSnapshot documentSnapshot) {
        if (!documentSnapshot.exists()) {
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            return;
        }

        userReligion.setText("Religion : " + getOrNA(documentSnapshot.getString("religion")));
        communityText.setText("Community : " + getOrNA(documentSnapshot.getString("community")));
        birthDetailsText.setText("Birth Details : " + getOrNA(documentSnapshot.getString("dob")));
        educationText.setText("Education : " + getOrNA(documentSnapshot.getString("highestQual")));

        familyBgText.setText("Family Background : " + getFormattedList((List<String>) documentSnapshot.get("family_structure")));
        professionIncomeText.setText("Profession and Income : " +
                getFormattedList((List<String>) documentSnapshot.get("designation")) + " & " +
                getFormattedList((List<String>) documentSnapshot.get("income")));
        relocateText.setText("Willingness to relocate : " + getFormattedList((List<String>) documentSnapshot.get("travel")));
        marriageTimelineText.setText("Marriage timeline : " + getFormattedList((List<String>) documentSnapshot.get("marriage")));
        languageText.setText("Languages spoken : " + getFormattedList((List<String>) documentSnapshot.get("language")));
    }

    private String getFormattedList(List<String> list) {
        return (list != null && !list.isEmpty()) ? TextUtils.join(", ", list) : "N/A";
    }

    private String getOrNA(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "N/A";
    }

    @SuppressWarnings("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (likeStatusChanged) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("uid", uid);
            resultIntent.putExtra("isLiked", isLiked);
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
