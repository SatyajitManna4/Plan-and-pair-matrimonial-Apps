/*
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class InterestActivity extends AppCompatActivity {
    private CardView card1, card2, card3, card4, card5;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        saveButton=findViewById(R.id.btnSave);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, Hobby_lifestyle.class);
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, RelationshipFamilyActivity.class);
                startActivity(intent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, PersonalInterestsActivity.class);
                startActivity(intent);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, CulturalIdentityActivity.class);
                startActivity(intent);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, SocialPreferencesActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterestActivity.this, WeddingGoalActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}*/
//add all card require

package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InterestActivity extends AppCompatActivity {
    private CardView card1, card2, card3, card4, card5;
    private Button saveButton;

    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        saveButton = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        card1.setOnClickListener(v -> startActivity(new Intent(this, Hobby_lifestyle.class)));
        card2.setOnClickListener(v -> startActivity(new Intent(this, RelationshipFamilyActivity.class)));
        card3.setOnClickListener(v -> startActivity(new Intent(this, PersonalInterestsActivity.class)));
        card4.setOnClickListener(v -> startActivity(new Intent(this, CulturalIdentityActivity.class)));
        card5.setOnClickListener(v -> startActivity(new Intent(this, SocialPreferencesActivity.class)));

        saveButton.setOnClickListener(v -> checkAllSectionsCompleted());
    }

    private void checkAllSectionsCompleted() {
        db.collection("UsersData").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean hobby = documentSnapshot.getBoolean("hobbyLifestyleCompleted");
                        Boolean relationship = documentSnapshot.getBoolean("relationshipFamilyCompleted");
                        Boolean personal = documentSnapshot.getBoolean("personalInterestsCompleted");
                        Boolean cultural = documentSnapshot.getBoolean("culturalIdentityCompleted");
                        Boolean social = documentSnapshot.getBoolean("socialPreferencesCompleted");

                        if (Boolean.TRUE.equals(hobby) &&
                                Boolean.TRUE.equals(relationship) &&
                                Boolean.TRUE.equals(personal) &&
                                Boolean.TRUE.equals(cultural) &&
                                Boolean.TRUE.equals(social)) {
                            // All done, now update isInterestCompleted
                            db.collection("UsersData").document(uid)
                                    .update("isInterestCompleted", true)
                                    .addOnSuccessListener(aVoid -> {
                                        // Proceed to WeddingGoalActivity
                                        startActivity(new Intent(InterestActivity.this, WeddingGoalActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error updating interest completion: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                    );
                        } else {
                            Toast.makeText(this, "Please complete all 5 sections before proceeding", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "User data not found. Please try again.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error checking data: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
