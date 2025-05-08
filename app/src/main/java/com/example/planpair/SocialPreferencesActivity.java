/*
package com.example.planpair;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SocialPreferencesActivity extends AppCompatActivity {

    private RadioGroup radioGroupSocialMedia, radioGroupPersonality;
    private SeekBar seekBarPublicSpeaking;
    private CheckBox checkPhoneCall, checkChatting, checkVideoCall, checkInPerson;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_preferences);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Initialize Views
        radioGroupSocialMedia = findViewById(R.id.radioGroup_socialMedia);
        radioGroupPersonality = findViewById(R.id.radioGroup_personality);
        seekBarPublicSpeaking = findViewById(R.id.seekBar_publicSpeaking);
        checkPhoneCall = findViewById(R.id.check_phone_call);
        checkChatting = findViewById(R.id.check_chatting);
        checkVideoCall = findViewById(R.id.check_video_call);
        checkInPerson = findViewById(R.id.check_inperson);

        // Load saved preferences
        loadPreferences();

        // Social Media Engagement Level Selection
        radioGroupSocialMedia.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            savePreference("social_media", selectedButton.getText().toString());
            Toast.makeText(this, "Selected: " + selectedButton.getText(), Toast.LENGTH_SHORT).show();
        });

        // Personality Type Selection
        radioGroupPersonality.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            savePreference("personality", selectedButton.getText().toString());
            Toast.makeText(this, "Personality: " + selectedButton.getText(), Toast.LENGTH_SHORT).show();
        });

        // Public Speaking Comfort Level Selection
        seekBarPublicSpeaking.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                savePreference("public_speaking", String.valueOf(progress));
                Toast.makeText(SocialPreferencesActivity.this, "Public Speaking Level: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SocialPreferencesActivity.this, "Adjusting Public Speaking Level...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SocialPreferencesActivity.this, "Final Level Set: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
            }
        });
        setupCheckboxListener(checkPhoneCall, "phone_call");
        setupCheckboxListener(checkChatting, "chatting");
        setupCheckboxListener(checkVideoCall, "video_call");
        setupCheckboxListener(checkInPerson, "in_person");
    }

    private void setupCheckboxListener(CheckBox checkBox, String key) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference(key, isChecked ? "true" : "false"));
    }

    // Save preference to SharedPreferences
    private void savePreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Load saved preferences
    private void loadPreferences() {
        String socialMedia = sharedPreferences.getString("social_media", "");
        String personality = sharedPreferences.getString("personality", "");
        int publicSpeaking = Integer.parseInt(sharedPreferences.getString("public_speaking", "3"));

        // Set saved values
        if (!socialMedia.isEmpty()) {
            for (int i = 0; i < radioGroupSocialMedia.getChildCount(); i++) {
                RadioButton button = (RadioButton) radioGroupSocialMedia.getChildAt(i);
                if (button.getText().toString().equals(socialMedia)) {
                    button.setChecked(true);
                    break;
                }
            }
        }

        if (!personality.isEmpty()) {
            for (int i = 0; i < radioGroupPersonality.getChildCount(); i++) {
                RadioButton button = (RadioButton) radioGroupPersonality.getChildAt(i);
                if (button.getText().toString().equals(personality)) {
                    button.setChecked(true);
                    break;
                }
            }
        }

        seekBarPublicSpeaking.setProgress(publicSpeaking);

        checkPhoneCall.setChecked(sharedPreferences.getString("phone_call", "false").equals("true"));
        checkChatting.setChecked(sharedPreferences.getString("chatting", "false").equals("true"));
        checkVideoCall.setChecked(sharedPreferences.getString("video_call", "false").equals("true"));
        checkInPerson.setChecked(sharedPreferences.getString("in_person", "false").equals("true"));
    }
}*/
//Add firebase code instead of sharePreference code
package com.example.planpair;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SocialPreferencesActivity extends AppCompatActivity {

    private RadioGroup radioGroupSocialMedia, radioGroupPersonality;
    private SeekBar seekBarPublicSpeaking;
    private CheckBox checkPhoneCall, checkChatting, checkVideoCall, checkInPerson;

    private FirebaseFirestore firestore;
    private String uid;
    private boolean isLoadingFromFirestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_preferences);

        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize Views
        radioGroupSocialMedia = findViewById(R.id.radioGroup_socialMedia);
        radioGroupPersonality = findViewById(R.id.radioGroup_personality);
        seekBarPublicSpeaking = findViewById(R.id.seekBar_publicSpeaking);
        checkPhoneCall = findViewById(R.id.check_phone_call);
        checkChatting = findViewById(R.id.check_chatting);
        checkVideoCall = findViewById(R.id.check_video_call);
        checkInPerson = findViewById(R.id.check_inperson);

        loadPreferencesFromFirestore();

        radioGroupSocialMedia.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isLoadingFromFirestore) {
                RadioButton selected = findViewById(checkedId);
                saveToFirestore("social_media", selected.getText().toString());
                Toast.makeText(this, "Selected: " + selected.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        radioGroupPersonality.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isLoadingFromFirestore) {
                RadioButton selected = findViewById(checkedId);
                saveToFirestore("personality", selected.getText().toString());
                Toast.makeText(this, "Personality: " + selected.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        seekBarPublicSpeaking.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!isLoadingFromFirestore) {
                    saveToFirestore("public_speaking", progress);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                if (!isLoadingFromFirestore) {
                    Toast.makeText(SocialPreferencesActivity.this, "Public Speaking Level Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupCheckboxListener(checkPhoneCall, "phone_call");
        setupCheckboxListener(checkChatting, "chatting");
        setupCheckboxListener(checkVideoCall, "video_call");
        setupCheckboxListener(checkInPerson, "in_person");
    }

    private void setupCheckboxListener(CheckBox checkBox, String key) {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isLoadingFromFirestore) {
                saveToFirestore(key, isChecked);
                Toast.makeText(this, key.replace("_", " ") + ": " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToFirestore(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);
        data.put("socialPreferencesCompleted", true); // Completion flag
        firestore.collection("UsersData").document(uid).set(data, SetOptions.merge());
    }


    private void loadPreferencesFromFirestore() {
        isLoadingFromFirestore = true;
        firestore.collection("UsersData").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        setRadioGroupSelection(radioGroupSocialMedia, documentSnapshot.getString("social_media"));
                        setRadioGroupSelection(radioGroupPersonality, documentSnapshot.getString("personality"));

                        Long speakingLevel = documentSnapshot.getLong("public_speaking");
                        if (speakingLevel != null) seekBarPublicSpeaking.setProgress(speakingLevel.intValue());

                        checkPhoneCall.setChecked(Boolean.TRUE.equals(documentSnapshot.getBoolean("phone_call")));
                        checkChatting.setChecked(Boolean.TRUE.equals(documentSnapshot.getBoolean("chatting")));
                        checkVideoCall.setChecked(Boolean.TRUE.equals(documentSnapshot.getBoolean("video_call")));
                        checkInPerson.setChecked(Boolean.TRUE.equals(documentSnapshot.getBoolean("in_person")));
                    }
                    isLoadingFromFirestore = false;
                });
    }

    private void setRadioGroupSelection(RadioGroup group, String value) {
        if (value == null) return;
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if (button.getText().toString().equals(value)) {
                button.setChecked(true);
                break;
            }
        }
    }
}