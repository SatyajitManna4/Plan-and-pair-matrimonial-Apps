/*
package com.example.planpair;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class RelationshipFamilyActivity extends AppCompatActivity {

    private ArrayList<CheckBox> relationshipCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> familyStructureCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> familyInvolveCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> childPreferencesCheckboxes= new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_family);

        sharedPreferences = getSharedPreferences("FamilyPrefs", MODE_PRIVATE);

        relationshipCheckboxes.add(findViewById(R.id.check_relation_traditional));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_modern));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_balanced));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_companionate));

        familyStructureCheckboxes.add(findViewById(R.id.check_family_nuclear));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_joint));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_extended));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_livein));

        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_high));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_moderate));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_minimal));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_independentchoice));

        childPreferencesCheckboxes.add(findViewById(R.id.check_child_want));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_infuture));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_nopreference));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_dontwant));

        // Load saved selections
        loadPreferences();

        // Set listeners for instant save
        setCheckboxListeners(relationshipCheckboxes);
        setCheckboxListeners(familyStructureCheckboxes);
        setCheckboxListeners(familyInvolveCheckboxes);
        setCheckboxListeners(childPreferencesCheckboxes);
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
        loadCheckboxState(relationshipCheckboxes);
        loadCheckboxState(familyStructureCheckboxes);
        loadCheckboxState(familyInvolveCheckboxes);
        loadCheckboxState(childPreferencesCheckboxes);
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
        selectedInterests.append("Relationship preference: ").append(getCheckedItems(relationshipCheckboxes)).append("\n");
        selectedInterests.append("Family Structure: ").append(getCheckedItems(familyStructureCheckboxes)).append("\n");
        selectedInterests.append("Family Involvement: ").append(getCheckedItems(familyInvolveCheckboxes)).append("\n");
        selectedInterests.append("Child preference: ").append(getCheckedItems(childPreferencesCheckboxes));

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

public class RelationshipFamilyActivity extends AppCompatActivity {

    private ArrayList<CheckBox> relationshipCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> familyStructureCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> familyInvolveCheckboxes = new ArrayList<>();
    private ArrayList<CheckBox> childPreferencesCheckboxes= new ArrayList<>();

    private FirebaseFirestore firestore;
    private String userId;
    private boolean isLoadingFromFirestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_family);

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        relationshipCheckboxes.add(findViewById(R.id.check_relation_traditional));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_modern));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_balanced));
        relationshipCheckboxes.add(findViewById(R.id.check_relation_companionate));

        familyStructureCheckboxes.add(findViewById(R.id.check_family_nuclear));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_joint));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_extended));
        familyStructureCheckboxes.add(findViewById(R.id.check_family_livein));

        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_high));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_moderate));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_minimal));
        familyInvolveCheckboxes.add(findViewById(R.id.check_family_involve_independentchoice));

        childPreferencesCheckboxes.add(findViewById(R.id.check_child_want));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_infuture));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_nopreference));
        childPreferencesCheckboxes.add(findViewById(R.id.check_child_dontwant));

        loadPreferencesFromFirestore();

        setCheckboxListeners(relationshipCheckboxes);
        setCheckboxListeners(familyStructureCheckboxes);
        setCheckboxListeners(familyInvolveCheckboxes);
        setCheckboxListeners(childPreferencesCheckboxes);
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
        Map<String, Object> familyPrefs = new HashMap<>();
        familyPrefs.put("relationship", getCheckedItemsList(relationshipCheckboxes));
        familyPrefs.put("family_structure", getCheckedItemsList(familyStructureCheckboxes));
        familyPrefs.put("family_involve", getCheckedItemsList(familyInvolveCheckboxes));
        familyPrefs.put("child_preferences", getCheckedItemsList(childPreferencesCheckboxes));
        //  Completion flag
        familyPrefs.put("relationshipFamilyCompleted", true);

        firestore.collection("UsersData").document(userId)
                .set(familyPrefs, SetOptions.merge())
                .addOnSuccessListener(unused -> Toast.makeText(this, "Preferences Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show());
    }

    private void loadPreferencesFromFirestore() {
        isLoadingFromFirestore = true;

        firestore.collection("UsersData").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        setCheckboxState(relationshipCheckboxes, documentSnapshot.get("relationship"));
                        setCheckboxState(familyStructureCheckboxes, documentSnapshot.get("family_structure"));
                        setCheckboxState(familyInvolveCheckboxes, documentSnapshot.get("family_involve"));
                        setCheckboxState(childPreferencesCheckboxes, documentSnapshot.get("child_preferences"));
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
        selectedInterests.append("Relationship preference: ").append(getCheckedItemsList(relationshipCheckboxes)).append("\n");
        selectedInterests.append("Family Structure: ").append(getCheckedItemsList(familyStructureCheckboxes)).append("\n");
        selectedInterests.append("Family Involvement: ").append(getCheckedItemsList(familyInvolveCheckboxes)).append("\n");
        selectedInterests.append("Child preference: ").append(getCheckedItemsList(childPreferencesCheckboxes));

        Toast.makeText(this, selectedInterests.toString(), Toast.LENGTH_LONG).show();
    }
}