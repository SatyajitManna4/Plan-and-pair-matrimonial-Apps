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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private ImageView likeButton;
    private TextView currUserName, userReligion, userAge, chatText, communityText, birthDetailsText,
            familyBgText, professionIncomeText, educationText, relocateText,
            marriageTimelineText, languageText;

    private CompatibilityScoreView compatibilityView;
    private LinearLayout chatSection;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        profileImageView = findViewById(R.id.profileImageView);
        likeButton = findViewById(R.id.likeButton);
        ImageView chatIcon = findViewById(R.id.chatIcon);

        currUserName=findViewById(R.id.currUserName);
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

        // Get Intent Data
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String name = intent.getStringExtra("username");
        int age = intent.getIntExtra("age", 0);
        String profileImageUrl = intent.getStringExtra("profileImageUrl");
        int compatibilityScore = intent.getIntExtra("compatibility", 0);

        // Set Name, Age, Religion, ChatText
        currUserName.setText(""+name);
        userAge.setText("Age : " + age);
        chatText.setText("Chat with\n" + name);

        // Load Profile Image
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.drawable.profile_icon)
                .error(R.drawable.profile_icon)
                .into(profileImageView);

        // Set compatibility score
        compatibilityView.setCompatibilityScore(compatibilityScore);

        // Fetch full details from Firestore
        if (uid != null && !uid.isEmpty()) {
            db.collection("UsersData").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String religion = documentSnapshot.getString("religion");
                            String community = documentSnapshot.getString("community");
                            String birthDetails = documentSnapshot.getString("dob");
                            String education = documentSnapshot.getString("highestQual");

                            List<String> familyList = (List<String>) documentSnapshot.get("family_structure");
                            String familyBg = (familyList != null && !familyList.isEmpty()) ? TextUtils.join(", ", familyList) : "N/A";

                            List<String> designationList = (List<String>) documentSnapshot.get("designation");
                            String designation = (designationList != null && !designationList.isEmpty())
                                    ? TextUtils.join(", ", designationList)
                                    : "N/A";

                            List<String> incomeList = (List<String>) documentSnapshot.get("income");
                            String income = (incomeList != null && !incomeList.isEmpty())
                                    ? TextUtils.join(", ", incomeList)
                                    : "N/A";

                            String professionIncome = designation + " & " + income;


                            List<String> relocateList = (List<String>) documentSnapshot.get("travel");
                            String relocate = (relocateList != null && !relocateList.isEmpty()) ? TextUtils.join(", ", relocateList) : "N/A";

                            List<String> marriageTimelineList = (List<String>) documentSnapshot.get("marriage");
                            String marriageTimeline = (marriageTimelineList != null && !marriageTimelineList.isEmpty()) ? TextUtils.join(", ", marriageTimelineList) : "N/A";

                            List<String> languageList = (List<String>) documentSnapshot.get("language");
                            String language = (languageList != null && !languageList.isEmpty()) ? TextUtils.join(", ", languageList) : "N/A";



                            userReligion.setText("Religion : " + (religion != null ? religion : "N/A"));
                            communityText.setText("Community : " + (community != null ? community : "N/A"));
                            birthDetailsText.setText("Birth Details : " + (birthDetails != null ? birthDetails : "N/A"));
                            educationText.setText("Education : " + (education != null ? education : "N/A"));

                            familyBgText.setText("Family Background : " + familyBg);
                            professionIncomeText.setText("Profession and Income : " + professionIncome);
                            familyBgText.setText("Family Background : " + familyBg);
                            relocateText.setText("Willingness to relocate : " + relocate);
                            marriageTimelineText.setText("Marriage timeline : " + marriageTimeline);
                            languageText.setText("Languages spoken : " + language);

                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        // Like button toggle
        likeButton.setOnClickListener(v ->
                likeButton.setImageResource(R.drawable.baseline_favorite_24));

        // Handle Chat Section Click
        chatSection.setOnClickListener(v -> {
            // Launch chat activity here if needed
            Toast.makeText(this, "Chat feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }
}
