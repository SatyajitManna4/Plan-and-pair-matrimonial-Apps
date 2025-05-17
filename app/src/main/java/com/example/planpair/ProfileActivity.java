/*
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private boolean isLiked = false; // Track like status
    private ImageView likeButton;
    private LinearLayout chatSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        CompatibilityScoreView compatibilityView = findViewById(R.id.compatibilityView);
        likeButton = findViewById(R.id.likeButton); // Heart icon as like button
        chatSection = findViewById(R.id.chatSection); // Chat section layout

        if (compatibilityView != null) {
            compatibilityView.setCompatibilityScore(85);
            compatibilityView.invalidate();
            compatibilityView.requestLayout();
        }

        // Set initial chat section visibility
        chatSection.setVisibility(View.GONE);

        // Like button functionality
        likeButton.setOnClickListener(v -> {
            isLiked = !isLiked; // Toggle like status
            updateLikeButton();
        });

        // Chat button click listener
        findViewById(R.id.chatSection).setOnClickListener(v -> {
            if (isLiked) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
            }
        });
    }

    private void updateLikeButton() {
        if (isLiked) {
            likeButton.setImageResource(R.drawable.baseline_favorite_24); // Filled heart
            chatSection.setVisibility(View.VISIBLE); // Show chat section
        } else {
            likeButton.setImageResource(R.drawable.baseline_favorite_border_24); // Outline heart
            chatSection.setVisibility(View.GONE); // Hide chat section
        }
    }
}
*/

//Compatibility score calling from compatibilityCalculator.java file

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView, likeButtonProfile;
    private TextView currUserName, userReligion, userAge, chatText, communityText, birthDetailsText,
            familyBgText, professionIncomeText, educationText, relocateText,
            marriageTimelineText, languageText;
    private CompatibilityScoreView compatibilityView;
    private LinearLayout chatSection;
    private FirebaseFirestore db;
    private String uid, name, profileImageUrl;
    private int age, compatibilityScore;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();

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
        ImageView chatIcon = findViewById(R.id.chatIcon);

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
    }

    private void loadProfileImage() {
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.profile_icon)
                .into(profileImageView);
    }

    private void setListeners() {
        if (isLiked) {
            likeButtonProfile.setImageResource(R.drawable.baseline_favorite_24);
            chatSection.setVisibility(View.VISIBLE);
        } else {
            likeButtonProfile.setImageResource(R.drawable.baseline_favorite_border_24); // or any unliked icon
            chatSection.setVisibility(View.GONE);
        }

        likeButtonProfile.setOnClickListener(v -> {
            isLiked = !isLiked;

            // Update icon
            likeButtonProfile.setImageResource(
                    isLiked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24
            );

            // Show/hide chat section
            chatSection.setVisibility(isLiked ? View.VISIBLE : View.GONE);

            // Update Firestore
            db.collection("UsersData")
                    .document(uid)
                    .update("isLiked", isLiked)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this, isLiked ? "Liked" : "Unliked", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to update like status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });


        chatSection.setOnClickListener(v -> {
            if (isLiked) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
            }
        });
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

        String religion = documentSnapshot.getString("religion");
        String community = documentSnapshot.getString("community");
        String birthDetails = documentSnapshot.getString("dob");
        String education = documentSnapshot.getString("highestQual");

        List<String> familyList = (List<String>) documentSnapshot.get("family_structure");
        String familyBg = getFormattedList(familyList);

        List<String> designationList = (List<String>) documentSnapshot.get("designation");
        String designation = getFormattedList(designationList);

        List<String> incomeList = (List<String>) documentSnapshot.get("income");
        String income = getFormattedList(incomeList);

        String professionIncome = designation + " & " + income;

        List<String> relocateList = (List<String>) documentSnapshot.get("travel");
        String relocate = getFormattedList(relocateList);

        List<String> marriageTimelineList = (List<String>) documentSnapshot.get("marriage");
        String marriageTimeline = getFormattedList(marriageTimelineList);

        List<String> languageList = (List<String>) documentSnapshot.get("language");
        String language = getFormattedList(languageList);

        // Set to views
        userReligion.setText("Religion : " + (religion != null ? religion : "N/A"));
        communityText.setText("Community : " + (community != null ? community : "N/A"));
        birthDetailsText.setText("Birth Details : " + (birthDetails != null ? birthDetails : "N/A"));
        educationText.setText("Education : " + (education != null ? education : "N/A"));
        familyBgText.setText("Family Background : " + familyBg);
        professionIncomeText.setText("Profession and Income : " + professionIncome);
        relocateText.setText("Willingness to relocate : " + relocate);
        marriageTimelineText.setText("Marriage timeline : " + marriageTimeline);
        languageText.setText("Languages spoken : " + language);
    }

    private String getFormattedList(List<String> list) {
        return (list != null && !list.isEmpty()) ? TextUtils.join(", ", list) : "N/A";
    }
}
