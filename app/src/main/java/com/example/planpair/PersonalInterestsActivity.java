/*
package com.example.planpair;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PersonalInterestsActivity extends AppCompatActivity {

    private ArrayList<CheckBox> creativeInterestCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> learningCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> socialInvolveCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> adventureCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> gamingCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> passionCheckboxes = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_interests);

        sharedPreferences = getSharedPreferences("PersonalInterestPrefs", MODE_PRIVATE);

        creativeInterestCheckboxes.add(findViewById(R.id.check_art_design));
        creativeInterestCheckboxes.add(findViewById(R.id.check_write_blog));
        creativeInterestCheckboxes.add(findViewById(R.id.check_music));
        creativeInterestCheckboxes.add(findViewById(R.id.check_dance));
        creativeInterestCheckboxes.add(findViewById(R.id.check_photography));
        creativeInterestCheckboxes.add(findViewById(R.id.check_acting));

        learningCheckboxes.add(findViewById(R.id.check_reading));
        learningCheckboxes.add(findViewById(R.id.check_technology));
        learningCheckboxes.add(findViewById(R.id.check_history));
        learningCheckboxes.add(findViewById(R.id.check_philosophy));
        learningCheckboxes.add(findViewById(R.id.check_diy));
        learningCheckboxes.add(findViewById(R.id.check_motivation_Learning));

        socialInvolveCheckboxes.add(findViewById(R.id.check_social_charitywork));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_environment));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_leadership));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_religious));

        adventureCheckboxes.add(findViewById(R.id.check_traveling));
        adventureCheckboxes.add(findViewById(R.id.check_trekking));
        adventureCheckboxes.add(findViewById(R.id.check_skydriving));
        adventureCheckboxes.add(findViewById(R.id.check_road_trips));
        adventureCheckboxes.add(findViewById(R.id.check_food_exploration));
        adventureCheckboxes.add(findViewById(R.id.check_cultural_exploration));

        gamingCheckboxes.add(findViewById(R.id.check_video_game));
        gamingCheckboxes.add(findViewById(R.id.check_esports));
        gamingCheckboxes.add(findViewById(R.id.check_board_games));
        gamingCheckboxes.add(findViewById(R.id.check_podcast));
        gamingCheckboxes.add(findViewById(R.id.check_radio_shows));

        passionCheckboxes.add(findViewById(R.id.check_gardening));
        passionCheckboxes.add(findViewById(R.id.check_cooking));
        passionCheckboxes.add(findViewById(R.id.check_baking));
        passionCheckboxes.add(findViewById(R.id.check_fashion));
        passionCheckboxes.add(findViewById(R.id.check_yoga));

        // Load saved selections
        loadPreferences();

        // Set listeners for instant save
        setCheckboxListeners(creativeInterestCheckboxes);
        setCheckboxListeners(learningCheckboxes);
        setCheckboxListeners(socialInvolveCheckboxes);
        setCheckboxListeners(adventureCheckboxes);
        setCheckboxListeners(gamingCheckboxes);
        setCheckboxListeners(passionCheckboxes);
    }

    private void setCheckboxListeners(ArrayList<CheckBox> checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference(checkBox.getId(), isChecked));
        }
    }

    private void savePreference(int id, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(id), value);
        editor.apply();
    }

    private void loadPreferences() {
        loadCheckboxState(creativeInterestCheckboxes);
        loadCheckboxState(learningCheckboxes);
        loadCheckboxState(socialInvolveCheckboxes);
        loadCheckboxState(adventureCheckboxes);
        loadCheckboxState(gamingCheckboxes);
        loadCheckboxState(passionCheckboxes);
    }

    private void loadCheckboxState(ArrayList<CheckBox> checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            boolean isChecked = sharedPreferences.getBoolean(String.valueOf(checkBox.getId()), false);
            checkBox.setChecked(isChecked);
        }
    }

    // Function to display selected interests
    public void getSelectedInterests(View view) {
        StringBuilder selectedInterests = new StringBuilder();
        selectedInterests.append("Creative Interest: ").append(getCheckedItems(creativeInterestCheckboxes)).append("\n");
        selectedInterests.append("Knowledge & Learning: ").append(getCheckedItems(learningCheckboxes)).append("\n");
        selectedInterests.append("Social Involvement: ").append(getCheckedItems(socialInvolveCheckboxes)).append("\n");
        selectedInterests.append("Adventure & Travel: ").append(getCheckedItems(adventureCheckboxes)).append("\n");
        selectedInterests.append("Gaming Preference: ").append(getCheckedItems(gamingCheckboxes)).append("\n");
        selectedInterests.append("Creative Passion: ").append(getCheckedItems(passionCheckboxes));

        Toast.makeText(this, selectedInterests.toString(), Toast.LENGTH_LONG).show();
    }

    private String getCheckedItems(ArrayList<CheckBox> checkBoxes) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                selectedItems.add(checkBox.getText().toString());
            }
        }
        return selectedItems.isEmpty() ? "None" : String.join(", ", selectedItems);
    }
}*/
// Add firebase code instead of sharePreference code
package com.example.planpair;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalInterestsActivity extends AppCompatActivity {

    private ArrayList<CheckBox> creativeInterestCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> learningCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> socialInvolveCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> adventureCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> gamingCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> passionCheckboxes = new ArrayList<>();

    private FirebaseFirestore firestore;
    private String userId;
    private boolean isLoadingFromFirestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_interests);

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize all checkboxes
        creativeInterestCheckboxes.add(findViewById(R.id.check_art_design));
        creativeInterestCheckboxes.add(findViewById(R.id.check_write_blog));
        creativeInterestCheckboxes.add(findViewById(R.id.check_music));
        creativeInterestCheckboxes.add(findViewById(R.id.check_dance));
        creativeInterestCheckboxes.add(findViewById(R.id.check_photography));
        creativeInterestCheckboxes.add(findViewById(R.id.check_acting));

        learningCheckboxes.add(findViewById(R.id.check_reading));
        learningCheckboxes.add(findViewById(R.id.check_technology));
        learningCheckboxes.add(findViewById(R.id.check_history));
        learningCheckboxes.add(findViewById(R.id.check_philosophy));
        learningCheckboxes.add(findViewById(R.id.check_diy));
        learningCheckboxes.add(findViewById(R.id.check_motivation_Learning));

        socialInvolveCheckboxes.add(findViewById(R.id.check_social_charitywork));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_environment));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_leadership));
        socialInvolveCheckboxes.add(findViewById(R.id.check_social_religious));

        adventureCheckboxes.add(findViewById(R.id.check_traveling));
        adventureCheckboxes.add(findViewById(R.id.check_trekking));
        adventureCheckboxes.add(findViewById(R.id.check_skydriving));
        adventureCheckboxes.add(findViewById(R.id.check_road_trips));
        adventureCheckboxes.add(findViewById(R.id.check_food_exploration));
        adventureCheckboxes.add(findViewById(R.id.check_cultural_exploration));

        gamingCheckboxes.add(findViewById(R.id.check_video_game));
        gamingCheckboxes.add(findViewById(R.id.check_esports));
        gamingCheckboxes.add(findViewById(R.id.check_board_games));
        gamingCheckboxes.add(findViewById(R.id.check_podcast));
        gamingCheckboxes.add(findViewById(R.id.check_radio_shows));

        passionCheckboxes.add(findViewById(R.id.check_gardening));
        passionCheckboxes.add(findViewById(R.id.check_cooking));
        passionCheckboxes.add(findViewById(R.id.check_baking));
        passionCheckboxes.add(findViewById(R.id.check_fashion));
        passionCheckboxes.add(findViewById(R.id.check_yoga));

        loadPreferencesFromFirestore();

        setCheckboxListeners(creativeInterestCheckboxes);
        setCheckboxListeners(learningCheckboxes);
        setCheckboxListeners(socialInvolveCheckboxes);
        setCheckboxListeners(adventureCheckboxes);
        setCheckboxListeners(gamingCheckboxes);
        setCheckboxListeners(passionCheckboxes);
    }

    private void setCheckboxListeners(ArrayList<CheckBox> checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isLoadingFromFirestore) {
                    saveAllPreferencesToFirestore();
                }
            });
        }
    }

    private void saveAllPreferencesToFirestore() {
        Map<String, Object> personalPrefs = new HashMap<>();
        personalPrefs.put("creative_interests", getCheckedItemsList(creativeInterestCheckboxes));
        personalPrefs.put("knowledge_learning", getCheckedItemsList(learningCheckboxes));
        personalPrefs.put("social_involvement", getCheckedItemsList(socialInvolveCheckboxes));
        personalPrefs.put("adventure_travel", getCheckedItemsList(adventureCheckboxes));
        personalPrefs.put("gaming_preference", getCheckedItemsList(gamingCheckboxes));
        personalPrefs.put("creative_passions", getCheckedItemsList(passionCheckboxes));
        personalPrefs.put("personalInterestsCompleted",true);// mark true

        firestore.collection("UsersData").document(userId)
                .set(personalPrefs, SetOptions.merge())
                .addOnSuccessListener(unused -> Toast.makeText(this, "Preferences Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show());
    }

    private void loadPreferencesFromFirestore() {
        isLoadingFromFirestore = true;

        firestore.collection("UsersData").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        setCheckboxState(creativeInterestCheckboxes, documentSnapshot.get("creative_interests"));
                        setCheckboxState(learningCheckboxes, documentSnapshot.get("knowledge_learning"));
                        setCheckboxState(socialInvolveCheckboxes, documentSnapshot.get("social_involvement"));
                        setCheckboxState(adventureCheckboxes, documentSnapshot.get("adventure_travel"));
                        setCheckboxState(gamingCheckboxes, documentSnapshot.get("gaming_preference"));
                        setCheckboxState(passionCheckboxes, documentSnapshot.get("creative_passions"));
                    }
                    isLoadingFromFirestore = false;
                });
    }

    private void setCheckboxState(ArrayList<CheckBox> checkBoxes, Object data) {
        List<String> selectedList = (List<String>) data;
        if (selectedList == null) return;
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(selectedList.contains(checkBox.getText().toString()));
        }
    }

    private ArrayList<String> getCheckedItemsList(ArrayList<CheckBox> checkBoxes) {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                selectedItems.add(checkBox.getText().toString());
            }
        }
        return selectedItems;
    }

    public void getSelectedInterests(View view) {
        StringBuilder selectedInterests = new StringBuilder();
        selectedInterests.append("Creative Interest: ").append(getCheckedItemsList(creativeInterestCheckboxes)).append("\n");
        selectedInterests.append("Knowledge & Learning: ").append(getCheckedItemsList(learningCheckboxes)).append("\n");
        selectedInterests.append("Social Involvement: ").append(getCheckedItemsList(socialInvolveCheckboxes)).append("\n");
        selectedInterests.append("Adventure & Travel: ").append(getCheckedItemsList(adventureCheckboxes)).append("\n");
        selectedInterests.append("Gaming Preference: ").append(getCheckedItemsList(gamingCheckboxes)).append("\n");
        selectedInterests.append("Creative Passion: ").append(getCheckedItemsList(passionCheckboxes));

        Toast.makeText(this, selectedInterests.toString(), Toast.LENGTH_LONG).show();
    }
}