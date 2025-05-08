package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WeddingGoalActivity extends AppCompatActivity {
    private RadioGroup weddingTypeGroup, guestCountGroup, cateringGroup, entertainmentGroup;
    private ChipGroup budgetChipGroup;
    private AutoCompleteTextView chooseSeason, chooseVibe, chooseStyle;
    private MaterialButton buttonSave;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wedding_goal);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        weddingTypeGroup = findViewById(R.id.wedding_type);
        guestCountGroup = findViewById(R.id.guest_count);
        cateringGroup = findViewById(R.id.choose_catering);
        entertainmentGroup = findViewById(R.id.choose_entertainment);
        budgetChipGroup = findViewById(R.id.budget_chip_group);
        buttonSave = findViewById(R.id.button_Save);
        chooseSeason = findViewById(R.id.choose_season);
        chooseVibe = findViewById(R.id.choose_vibe);
        chooseStyle = findViewById(R.id.choose_style);

        chooseSeasonDropdown();
        chooseVibeDropdown();
        chooseStyleDropdown();

        buttonSave.setOnClickListener(v -> saveWeddingGoalsToFirestore());

        // Load saved data
        loadWeddingGoalsFromFirestore();
    }

    private void chooseSeasonDropdown() {
        String[] season = {"Winter", "Spring", "Summer", "Autumn"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, season);
        chooseSeason.setAdapter(adapter);
        chooseSeason.setOnClickListener(v -> chooseSeason.showDropDown());
    }

    private void chooseVibeDropdown() {
        String[] vibe = {"Elegant", "Rustic", "Beach", "Royal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, vibe);
        chooseVibe.setAdapter(adapter);
        chooseVibe.setOnClickListener(v -> chooseVibe.showDropDown());
    }

    private void chooseStyleDropdown() {
        String[] style = {"Traditional", "Contemporary", "Fusion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, style);
        chooseStyle.setAdapter(adapter);
        chooseStyle.setOnClickListener(v -> chooseStyle.showDropDown());
    }

    private void saveWeddingGoalsToFirestore() {
        Map<String, Object> data = new HashMap<>();

        // Wedding Type
        int weddingTypeId = weddingTypeGroup.getCheckedRadioButtonId();
        if (weddingTypeId != -1)
            data.put("WeddingType", ((RadioButton) findViewById(weddingTypeId)).getText().toString());

        // Guest Count
        int guestCountId = guestCountGroup.getCheckedRadioButtonId();
        if (guestCountId != -1)
            data.put("GuestCount", ((RadioButton) findViewById(guestCountId)).getText().toString());

        // Catering
        int cateringId = cateringGroup.getCheckedRadioButtonId();
        if (cateringId != -1)
            data.put("Catering", ((RadioButton) findViewById(cateringId)).getText().toString());

        // Entertainment
        int entertainmentId = entertainmentGroup.getCheckedRadioButtonId();
        if (entertainmentId != -1)
            data.put("Entertainment", ((RadioButton) findViewById(entertainmentId)).getText().toString());

        // Season, Vibe, Style
        data.put("Season", chooseSeason.getText().toString().trim());
        data.put("Vibe", chooseVibe.getText().toString().trim());
        data.put("Style", chooseStyle.getText().toString().trim());

        // Budget (Chip)
        int budgetId = budgetChipGroup.getCheckedChipId();
        if (budgetId != -1) {
            Chip selectedChip = findViewById(budgetId);
            data.put("WeddingBudget", selectedChip.getText().toString());
        }

        // Save to Firestore under UsersData → UID
        DocumentReference userRef = db.collection("UsersData").document(userId);
        userRef.set(data, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    // Update isWeddingGoalsCompleted field
                    userRef.update("isWeddingGoalsCompleted", true)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Wedding Goal Preferences saved to Firebase!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WeddingGoalActivity.this, HomeActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error updating wedding goals completion status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadWeddingGoalsFromFirestore() {
        DocumentReference userRef = db.collection("UsersData").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Wedding Type
                String weddingType = documentSnapshot.getString("WeddingType");
                selectRadioButton(weddingTypeGroup, weddingType);

                // Guest Count
                String guestCount = documentSnapshot.getString("GuestCount");
                selectRadioButton(guestCountGroup, guestCount);

                // Catering
                String catering = documentSnapshot.getString("Catering");
                selectRadioButton(cateringGroup, catering);

                // Entertainment
                String entertainment = documentSnapshot.getString("Entertainment");
                selectRadioButton(entertainmentGroup, entertainment);

                // Season, Vibe, Style
                chooseSeason.setText(documentSnapshot.getString("Season"), false);
                chooseVibe.setText(documentSnapshot.getString("Vibe"), false);
                chooseStyle.setText(documentSnapshot.getString("Style"), false);

                // Budget Chip
                String selectedBudget = documentSnapshot.getString("WeddingBudget");
                if (selectedBudget != null) {
                    for (int i = 0; i < budgetChipGroup.getChildCount(); i++) {
                        Chip chip = (Chip) budgetChipGroup.getChildAt(i);
                        if (chip.getText().toString().equals(selectedBudget)) {
                            chip.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load preferences: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void selectRadioButton(RadioGroup group, String value) {
        if (value == null) return;
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(value)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }
    }
}
