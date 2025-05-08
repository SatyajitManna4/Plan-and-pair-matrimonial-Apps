/*
package com.example.planpair;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class Hobby_lifestyle extends AppCompatActivity {

    private ArrayList<CheckBox> musicCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> moviesCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> booksCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> foodCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> travelCheckboxes = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_lifestyle);

        sharedPreferences = getSharedPreferences("HobbyPrefs", MODE_PRIVATE);

        // Initialize Checkboxes for Music Preferences
        musicCheckboxes.add(findViewById(R.id.check_music_classical));
        musicCheckboxes.add(findViewById(R.id.check_music_folk));
        musicCheckboxes.add(findViewById(R.id.check_music_pop));
        musicCheckboxes.add(findViewById(R.id.check_music_bim));
        musicCheckboxes.add(findViewById(R.id.check_music_hiphop));
        musicCheckboxes.add(findViewById(R.id.check_music_dance));

        // Initialize Checkboxes for Movies Preferences
        moviesCheckboxes.add(findViewById(R.id.check_movies_horror));
        moviesCheckboxes.add(findViewById(R.id.check_movies_detective));
        moviesCheckboxes.add(findViewById(R.id.check_movies_comedy));
        moviesCheckboxes.add(findViewById(R.id.check_movies_action));
        moviesCheckboxes.add(findViewById(R.id.check_movies_realityshow));
        moviesCheckboxes.add(findViewById(R.id.check_movies_animation));

        // Initialize Checkboxes for Books Preferences
        booksCheckboxes.add(findViewById(R.id.check_books_fiction));
        booksCheckboxes.add(findViewById(R.id.check_books_science));
        booksCheckboxes.add(findViewById(R.id.check_books_fantasy));
        booksCheckboxes.add(findViewById(R.id.check_books_selfhelp));
        booksCheckboxes.add(findViewById(R.id.check_books_mystery));
        booksCheckboxes.add(findViewById(R.id.check_books_biography));

        // Initialize Checkboxes for Food Preferences
        foodCheckboxes.add(findViewById(R.id.check_food_veg));
        foodCheckboxes.add(findViewById(R.id.check_food_nonveg));
        foodCheckboxes.add(findViewById(R.id.check_food_pescatarian));
        foodCheckboxes.add(findViewById(R.id.check_food_sweet));
        foodCheckboxes.add(findViewById(R.id.check_food_homefood));
        foodCheckboxes.add(findViewById(R.id.check_food_fastfood));

        // Initialize Checkboxes for Travel Preferences
        travelCheckboxes.add(findViewById(R.id.check_travel_hill));
        travelCheckboxes.add(findViewById(R.id.check_travel_beach));
        travelCheckboxes.add(findViewById(R.id.check_travel_desert));
        travelCheckboxes.add(findViewById(R.id.check_travel_forest));
        travelCheckboxes.add(findViewById(R.id.check_travel_historical_place));
        travelCheckboxes.add(findViewById(R.id.check_travel_island));

        // Load saved selections
        loadPreferences();

        // Set listeners for instant save
        setCheckboxListeners(musicCheckboxes);
        setCheckboxListeners(moviesCheckboxes);
        setCheckboxListeners(booksCheckboxes);
        setCheckboxListeners(foodCheckboxes);
        setCheckboxListeners(travelCheckboxes);
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
        loadCheckboxState(musicCheckboxes);
        loadCheckboxState(moviesCheckboxes);
        loadCheckboxState(booksCheckboxes);
        loadCheckboxState(foodCheckboxes);
        loadCheckboxState(travelCheckboxes);
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
        selectedInterests.append("Music: ").append(getCheckedItems(musicCheckboxes)).append("\n");
        selectedInterests.append("Movies: ").append(getCheckedItems(moviesCheckboxes)).append("\n");
        selectedInterests.append("Books: ").append(getCheckedItems(booksCheckboxes)).append("\n");
        selectedInterests.append("Food: ").append(getCheckedItems(foodCheckboxes)).append("\n");
        selectedInterests.append("Travel: ").append(getCheckedItems(travelCheckboxes));

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
// Add firebase code instead of sharePreference
package com.example.planpair;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.*;

public class Hobby_lifestyle extends AppCompatActivity {

    private ArrayList<CheckBox> musicCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> moviesCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> booksCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> foodCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> travelCheckboxes = new ArrayList<>();

    private FirebaseFirestore firestore;
    private String userId;
    private boolean isLoadingFromFirestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_lifestyle);

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Checkboxes
        musicCheckboxes.add(findViewById(R.id.check_music_classical));
        musicCheckboxes.add(findViewById(R.id.check_music_folk));
        musicCheckboxes.add(findViewById(R.id.check_music_pop));
        musicCheckboxes.add(findViewById(R.id.check_music_bim));
        musicCheckboxes.add(findViewById(R.id.check_music_hiphop));
        musicCheckboxes.add(findViewById(R.id.check_music_dance));

        moviesCheckboxes.add(findViewById(R.id.check_movies_horror));
        moviesCheckboxes.add(findViewById(R.id.check_movies_detective));
        moviesCheckboxes.add(findViewById(R.id.check_movies_comedy));
        moviesCheckboxes.add(findViewById(R.id.check_movies_action));
        moviesCheckboxes.add(findViewById(R.id.check_movies_realityshow));
        moviesCheckboxes.add(findViewById(R.id.check_movies_animation));

        booksCheckboxes.add(findViewById(R.id.check_books_fiction));
        booksCheckboxes.add(findViewById(R.id.check_books_science));
        booksCheckboxes.add(findViewById(R.id.check_books_fantasy));
        booksCheckboxes.add(findViewById(R.id.check_books_selfhelp));
        booksCheckboxes.add(findViewById(R.id.check_books_mystery));
        booksCheckboxes.add(findViewById(R.id.check_books_biography));

        foodCheckboxes.add(findViewById(R.id.check_food_veg));
        foodCheckboxes.add(findViewById(R.id.check_food_nonveg));
        foodCheckboxes.add(findViewById(R.id.check_food_pescatarian));
        foodCheckboxes.add(findViewById(R.id.check_food_sweet));
        foodCheckboxes.add(findViewById(R.id.check_food_homefood));
        foodCheckboxes.add(findViewById(R.id.check_food_fastfood));

        travelCheckboxes.add(findViewById(R.id.check_travel_hill));
        travelCheckboxes.add(findViewById(R.id.check_travel_beach));
        travelCheckboxes.add(findViewById(R.id.check_travel_desert));
        travelCheckboxes.add(findViewById(R.id.check_travel_forest));
        travelCheckboxes.add(findViewById(R.id.check_travel_historical_place));
        travelCheckboxes.add(findViewById(R.id.check_travel_island));

        // Load user hobbies from Firestore
        loadPreferencesFromFirestore();

        // Set auto-save listeners
        setCheckboxListeners(musicCheckboxes);
        setCheckboxListeners(moviesCheckboxes);
        setCheckboxListeners(booksCheckboxes);
        setCheckboxListeners(foodCheckboxes);
        setCheckboxListeners(travelCheckboxes);
    }

    private void setCheckboxListeners(ArrayList<CheckBox> checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isLoadingFromFirestore) {
                    saveAllInterestsToFirestore();
                }
            });
        }
    }

    private void saveAllInterestsToFirestore() {
        Map<String, Object> hobbies = new HashMap<>();
        hobbies.put("music", getCheckedItemsList(musicCheckboxes));
        hobbies.put("movies", getCheckedItemsList(moviesCheckboxes));
        hobbies.put("books", getCheckedItemsList(booksCheckboxes));
        hobbies.put("food", getCheckedItemsList(foodCheckboxes));
        hobbies.put("travel", getCheckedItemsList(travelCheckboxes));

        //Mark this section as completed
        hobbies.put("hobbyLifestyleCompleted", true);

        firestore.collection("UsersData").document(userId)
                .set(hobbies, SetOptions.merge())
                .addOnSuccessListener(unused -> Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show());
    }


    private void loadPreferencesFromFirestore() {
        isLoadingFromFirestore = true; // Start blocking listeners

        firestore.collection("UsersData").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        setCheckboxState(musicCheckboxes, documentSnapshot.get("music"));
                        setCheckboxState(moviesCheckboxes, documentSnapshot.get("movies"));
                        setCheckboxState(booksCheckboxes, documentSnapshot.get("books"));
                        setCheckboxState(foodCheckboxes, documentSnapshot.get("food"));
                        setCheckboxState(travelCheckboxes, documentSnapshot.get("travel"));
                    }
                    isLoadingFromFirestore = false; // Done loading
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
        selectedInterests.append("Music: ").append(getCheckedItemsList(musicCheckboxes)).append("\n");
        selectedInterests.append("Movies: ").append(getCheckedItemsList(moviesCheckboxes)).append("\n");
        selectedInterests.append("Books: ").append(getCheckedItemsList(booksCheckboxes)).append("\n");
        selectedInterests.append("Food: ").append(getCheckedItemsList(foodCheckboxes)).append("\n");
        selectedInterests.append("Travel: ").append(getCheckedItemsList(travelCheckboxes));
        Toast.makeText(this, selectedInterests.toString(), Toast.LENGTH_LONG).show();
    }
}