/*
package com.example.planpair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CulturalIdentityActivity extends AppCompatActivity {

    private RadioGroup religionGroup;
    private AutoCompleteTextView sectDropdown;
    private ChipGroup culturalChipGroup, festivalChipGroup;
    private EditText otherCulturalBackground;
    private Slider traditionalModernSlider;
    private SwitchMaterial intercasteSwitch;
    private MaterialButton btnSave;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPreferences";
    private static final String KEY_CULTURAL = "selected_cultural_chips";
    private static final String KEY_FESTIVAL = "selected_festival_chips";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_identity);

        // Initialize SharedPreferences before using it
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize Views
        religionGroup = findViewById(R.id.religionGroup);
        sectDropdown = findViewById(R.id.sectDropdown);
        culturalChipGroup = findViewById(R.id.culturalChipGroup);
        festivalChipGroup = findViewById(R.id.festivalChipGroup);
        otherCulturalBackground = findViewById(R.id.otherCulturalBackground);
        traditionalModernSlider = findViewById(R.id.traditionalModernSlider);
        intercasteSwitch = findViewById(R.id.intercasteSwitch);
        btnSave = findViewById(R.id.btnSave);

        // Load and Setup Components
        setupSectDropdown();
        setupCulturalBackgroundChips();
        setupFestivalChips();
        setupSlider();
        loadAllPreferences();
        setupChipListeners();
        setupSaveButton();
    }

    // Setup Sect Dropdown
    private void setupSectDropdown() {
        String[] sects = {"Shia", "Sunni", "Catholic", "Protestant", "Vaishnavism", "Shaivism"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sects);
        sectDropdown.setAdapter(adapter);
        sectDropdown.setOnClickListener(v -> sectDropdown.showDropDown());
    }

    // Handle Cultural Background Chip Selection
    private void setupCulturalBackgroundChips() {
        culturalChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            boolean isOthersChecked = false;
            for (int id : checkedIds) {
                Chip chip = findViewById(id);
                if (chip != null && "Others".equalsIgnoreCase(chip.getText().toString())) {
                    isOthersChecked = true;
                    break;
                }
            }
            otherCulturalBackground.setVisibility(isOthersChecked ? View.VISIBLE : View.GONE);
        });
    }

    // Handle Festival Chip Selection
    private void setupFestivalChips() {
        festivalChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            List<String> selectedFestivals = new ArrayList<>();
            for (int id : checkedIds) {
                Chip chip = findViewById(id);
                if (chip != null) {
                    selectedFestivals.add(chip.getText().toString());
                }
            }
            Toast.makeText(this, "Selected Festivals: " + selectedFestivals, Toast.LENGTH_SHORT).show();
        });
    }

    // Handle Slider for Traditional vs Modern
    private void setupSlider() {
        traditionalModernSlider.addOnChangeListener((slider, value, fromUser) -> {
            String sliderValue = value < 5 ? "Traditional" : value > 5 ? "Modern" : "Balanced";
            Toast.makeText(this, "Belief Preference: " + sliderValue, Toast.LENGTH_SHORT).show();
        });
    }

    // Save Chip Selections
    private void saveChipSelection(ChipGroup chipGroup, String key) {
        Set<String> selectedChips = new HashSet<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedChips.add(chip.getText().toString());
            }
        }
        sharedPreferences.edit().putString(key, String.join(",", selectedChips)).apply();
    }
    private void loadAllPreferences() {
        String savedReligion = sharedPreferences.getString("Religion", "");
        if (!savedReligion.isEmpty()) {
            for (int i = 0; i < religionGroup.getChildCount(); i++) {
                View view = religionGroup.getChildAt(i);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getText().toString().equals(savedReligion)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }

        sectDropdown.setText(sharedPreferences.getString("Sect", ""), false);
        intercasteSwitch.setChecked(sharedPreferences.getBoolean("IntercasteMarriage", false));
        traditionalModernSlider.setValue(sharedPreferences.getFloat("SliderValue", 5));
        otherCulturalBackground.setText(sharedPreferences.getString("OtherCulturalBackground", ""));

        loadChipSelection(culturalChipGroup, KEY_CULTURAL);
        loadChipSelection(festivalChipGroup, KEY_FESTIVAL);
    }

    // Load Chip Selections
    private void loadChipSelections() {
        loadChipSelection(culturalChipGroup, KEY_CULTURAL);
        loadChipSelection(festivalChipGroup, KEY_FESTIVAL);
    }

    private void loadChipSelection(ChipGroup chipGroup, String key) {
        String savedData = sharedPreferences.getString(key, "");
        if (!savedData.isEmpty()) {
            List<String> selectedChips = Arrays.asList(savedData.split(","));
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (selectedChips.contains(chip.getText().toString())) {
                    chip.setChecked(true);
                }
            }
        }
    }


    // Setup Click Listeners for Chips
    private void setupChipListeners() {
        setupChipClickListeners(culturalChipGroup, KEY_CULTURAL);
        setupChipClickListeners(festivalChipGroup, KEY_FESTIVAL);
    }

    private void setupChipClickListeners(ChipGroup chipGroup, String key) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnClickListener(v -> saveChipSelection(chipGroup, key));
        }
    }

    // Save Button Click Listener
    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            int selectedReligionId = religionGroup.getCheckedRadioButtonId();
            String selectedReligion = selectedReligionId != -1 ? ((RadioButton) findViewById(selectedReligionId)).getText().toString() : "Not Selected";

            String selectedSect = sectDropdown.getText().toString().isEmpty() ? "Not Selected" : sectDropdown.getText().toString();

            List<String> selectedCultures = new ArrayList<>();
            for (int i = 0; i < culturalChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) culturalChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedCultures.add(chip.getText().toString());
                }
            }
            if (selectedCultures.contains("Others")) {
                selectedCultures.add("Others: " + otherCulturalBackground.getText().toString());
            }

            boolean isIntercasteAccepted = intercasteSwitch.isChecked();
            float sliderValue = traditionalModernSlider.getValue();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Religion", selectedReligion);
            editor.putString("Sect", selectedSect);
            editor.putFloat("SliderValue", sliderValue);
            editor.putString("CulturalBackgrounds", String.join(",", selectedCultures));
            editor.putBoolean("IntercasteMarriage", isIntercasteAccepted);
            editor.apply();

            Toast.makeText(this, "Preferences saved successfully!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(CulturalIdentityActivity.this, InterestActivity.class));
            finish();
        });
    }
}*/
//Add firebase code instead of sharePreference code
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CulturalIdentityActivity extends AppCompatActivity {

    private RadioGroup religionGroup;
    private AutoCompleteTextView sectDropdown;
    private ChipGroup culturalChipGroup, festivalChipGroup;
    private EditText otherCulturalBackground;
    private Slider traditionalModernSlider;
    private SwitchMaterial intercasteSwitch;
    private MaterialButton btnSave;

    private FirebaseFirestore firestore;
    private String uid;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_identity);

        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        religionGroup = findViewById(R.id.religionGroup);
        sectDropdown = findViewById(R.id.sectDropdown);
        culturalChipGroup = findViewById(R.id.culturalChipGroup);
        festivalChipGroup = findViewById(R.id.festivalChipGroup);
        otherCulturalBackground = findViewById(R.id.otherCulturalBackground);
        traditionalModernSlider = findViewById(R.id.traditionalModernSlider);
        intercasteSwitch = findViewById(R.id.intercasteSwitch);
        btnSave = findViewById(R.id.btnSave);

        setupSectDropdown();
        setupCulturalBackgroundChips();
        setupFestivalChips();
        setupSlider();
        setupChipListeners();
        loadFromFirestore();
        setupSaveButton();
    }

    private void setupSectDropdown() {
        String[] sects = {"Shia", "Sunni", "Catholic", "Protestant", "Vaishnavism", "Shaivism"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sects);
        sectDropdown.setAdapter(adapter);
        sectDropdown.setOnClickListener(v -> sectDropdown.showDropDown());
    }

    private void setupCulturalBackgroundChips() {
        culturalChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            boolean isOthersChecked = false;
            for (int id : checkedIds) {
                Chip chip = findViewById(id);
                if (chip != null && "Others".equalsIgnoreCase(chip.getText().toString())) {
                    isOthersChecked = true;
                    break;
                }
            }
            otherCulturalBackground.setVisibility(isOthersChecked ? View.VISIBLE : View.GONE);
        });
    }

    private void setupFestivalChips() {
        // Optional: add listener for chip selections
    }

    private void setupSlider() {
        traditionalModernSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (!isLoading)
                Toast.makeText(this, "Belief Preference: " + getBeliefLabel(value), Toast.LENGTH_SHORT).show();
        });
    }

    private String getBeliefLabel(float value) {
        if (value < 5) return "Traditional";
        else if (value > 5) return "Modern";
        else return "Balanced";
    }

    private void setupChipListeners() {
        // Optional: add chip click listeners for real-time Firestore updates
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            int selectedReligionId = religionGroup.getCheckedRadioButtonId();
            String religion = selectedReligionId != -1 ?
                    ((RadioButton) findViewById(selectedReligionId)).getText().toString() : "";

            String sect = sectDropdown.getText().toString().trim();
            boolean intercasteAllowed = intercasteSwitch.isChecked();
            float sliderValue = traditionalModernSlider.getValue();

            List<String> culturalList = new ArrayList<>();
            for (int i = 0; i < culturalChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) culturalChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    culturalList.add(chip.getText().toString());
                }
            }

            if (culturalList.contains("Others")) {
                culturalList.add("Others: " + otherCulturalBackground.getText().toString().trim());
            }

            List<String> festivalList = new ArrayList<>();
            for (int i = 0; i < festivalChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) festivalChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    festivalList.add(chip.getText().toString());
                }
            }

            // ✅ Save to Firestore with completion flag
            Map<String, Object> data = new HashMap<>();
            data.put("religion", religion);
            data.put("sect", sect);
            data.put("intercaste_marriage", intercasteAllowed);
            data.put("traditional_modern_scale", sliderValue);
            data.put("cultural_backgrounds", culturalList);
            data.put("festivals", festivalList);
            data.put("culturalIdentityCompleted", true);  // ✅ Completion flag

            firestore.collection("UsersData").document(uid)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CulturalIdentityActivity.this, InterestActivity.class));
                        finish();
                    });
        });
    }

    private void loadFromFirestore() {
        isLoading = true;
        firestore.collection("UsersData").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String religion = document.getString("religion");
                        if (religion != null) {
                            for (int i = 0; i < religionGroup.getChildCount(); i++) {
                                View view = religionGroup.getChildAt(i);
                                if (view instanceof RadioButton) {
                                    RadioButton radioButton = (RadioButton) view;
                                    if (radioButton.getText().toString().equalsIgnoreCase(religion)) {
                                        radioButton.setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }

                        sectDropdown.setText(document.getString("sect"), false);
                        intercasteSwitch.setChecked(Boolean.TRUE.equals(document.getBoolean("intercaste_marriage")));
                        Double slider = document.getDouble("traditional_modern_scale");
                        if (slider != null) traditionalModernSlider.setValue(slider.floatValue());

                        List<String> cultures = (List<String>) document.get("cultural_backgrounds");
                        if (cultures != null) {
                            for (int i = 0; i < culturalChipGroup.getChildCount(); i++) {
                                Chip chip = (Chip) culturalChipGroup.getChildAt(i);
                                if (cultures.contains(chip.getText().toString()) ||
                                        cultures.contains("Others: " + chip.getText().toString())) {
                                    chip.setChecked(true);
                                }
                            }
                        }

                        List<String> festivals = (List<String>) document.get("festivals");
                        if (festivals != null) {
                            for (int i = 0; i < festivalChipGroup.getChildCount(); i++) {
                                Chip chip = (Chip) festivalChipGroup.getChildAt(i);
                                if (festivals.contains(chip.getText().toString())) {
                                    chip.setChecked(true);
                                }
                            }
                        }
                    }
                    isLoading = false;
                });
    }
}