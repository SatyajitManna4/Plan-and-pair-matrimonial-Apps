/*
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView, editProfileimg;
    private EditText gender, age, religion, community, familyBackground, workDetails, relocateDetails, eduDetails, timeline, languages;
    private Button doneButton;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        // Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
//        editProfileimg = findViewById(R.id.editProfileimg);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        religion = findViewById(R.id.religion);
        community = findViewById(R.id.community);
        familyBackground = findViewById(R.id.familyBackground);
        workDetails = findViewById(R.id.workDetails);
        eduDetails = findViewById(R.id.eduDetails);
        timeline = findViewById(R.id.timeline);
        languages = findViewById(R.id.languages);
        relocateDetails = findViewById(R.id.relocateDetails);
        doneButton = findViewById(R.id.doneButton);

        // Handle image selection from gallery
//        editProfileimg.setOnClickListener(v -> openGallery());

        // Handle Done button click
        doneButton.setOnClickListener(v -> saveUserData());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserData() {
        String userGender = gender.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userReligion = religion.getText().toString().trim();
        String userCommunity = community.getText().toString().trim();
        String userFamilyBackground = familyBackground.getText().toString().trim();
        String userWorkDetails = workDetails.getText().toString().trim();
        String userEduDetails = eduDetails.getText().toString().trim();
        String userTimeline = timeline.getText().toString().trim();
        String userLanguages = languages.getText().toString().trim();

        // Validate required fields
        if (userGender.isEmpty() || userAge.isEmpty() || userReligion.isEmpty() || userCommunity.isEmpty() || userFamilyBackground.isEmpty()
                || userWorkDetails.isEmpty() || userEduDetails.isEmpty() || userTimeline.isEmpty() || userLanguages.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate age
        int ageValue;
        try {
            ageValue = Integer.parseInt(userAge);
            if (userGender.equals("male") && (ageValue < 21 || ageValue > 100)) {
                Toast.makeText(this, "Age must be between 21 and 100", Toast.LENGTH_SHORT).show();
                return;
            } else if (userGender.equals("female") && (ageValue < 18 || ageValue > 100)) {
                Toast.makeText(this, "Age must be between 18 and 100", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data logic (e.g., SharedPreferences, Firebase, Database)
        Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

        // Navigate to HomeActivity
        Intent intent = new Intent(BioActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}*/
/*
// everything is ok but image is optional.
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText gender, age, community, familyBackground, workDetails, relocateDetails, eduDetails, timeline, languages;
    private Button doneButton;
    private Uri imageUri;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        // Change Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize Views
        profileImageView = findViewById(R.id.profileImageView);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        familyBackground = findViewById(R.id.familyBackground);
        workDetails = findViewById(R.id.workDetails);
        eduDetails = findViewById(R.id.eduDetails);
        timeline = findViewById(R.id.timeline);
        languages = findViewById(R.id.languages);
        relocateDetails = findViewById(R.id.relocateDetails);
        doneButton = findViewById(R.id.doneButton);

        // Fetch Data if Exists
        fetchUserData();

        // Handle Save Button Click
        doneButton.setOnClickListener(v -> saveUserData());
    }

    // Fetch User Data from Firebase FireStore
    private void fetchUserData() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("UsersData").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
//                      Toast.makeText(BioActivity.this,"data is present why??",Toast.LENGTH_SHORT).show();
                        gender.setText(documentSnapshot.getString("gender"));
                        age.setText(documentSnapshot.getString("age"));
                        community.setText(documentSnapshot.getString("community"));
                        familyBackground.setText(documentSnapshot.getString("familyBackground"));
                        workDetails.setText(documentSnapshot.getString("workDetails"));
                        eduDetails.setText(documentSnapshot.getString("eduDetails"));
                        timeline.setText(documentSnapshot.getString("timeline"));
                        languages.setText(documentSnapshot.getString("languages"));
                        relocateDetails.setText(documentSnapshot.getString("relocateDetails"));
                        // Change button text to "Update"
                        doneButton.setText("Done");
                    }


                })
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
    }

    // Save or Update User Data in Firestore
    private void saveUserData() {
        String userId = auth.getCurrentUser().getUid();

        // Get input values
        String userGender = gender.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userCommunity = community.getText().toString().trim();
        String userFamilyBackground = familyBackground.getText().toString().trim();
        String userWorkDetails = workDetails.getText().toString().trim();
        String userEduDetails = eduDetails.getText().toString().trim();
        String userTimeline = timeline.getText().toString().trim();
        String userLanguages = languages.getText().toString().trim();
        String userRelocateDetails = relocateDetails.getText().toString().trim();

        // Validate required fields
        if (userGender.isEmpty() || userAge.isEmpty() ||  userCommunity.isEmpty() ||
                userFamilyBackground.isEmpty() || userWorkDetails.isEmpty() || userEduDetails.isEmpty() ||
                userTimeline.isEmpty() || userLanguages.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate age
        int ageValue;
        try {
            ageValue = Integer.parseInt(userAge);
            if (userGender.equalsIgnoreCase("male") && (ageValue < 21 || ageValue > 100)) {
                Toast.makeText(this, "Male age must be between 21 and 100", Toast.LENGTH_SHORT).show();
                return;
            } else if (userGender.equalsIgnoreCase("female") && (ageValue < 18 || ageValue > 100)) {
                Toast.makeText(this, "Female age must be between 18 and 100", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("gender", userGender);
        userData.put("age", userAge);
        userData.put("community", userCommunity);
        userData.put("familyBackground", userFamilyBackground);
        userData.put("workDetails", userWorkDetails);
        userData.put("eduDetails", userEduDetails);
        userData.put("timeline", userTimeline);
        userData.put("languages", userLanguages);
        userData.put("relocateDetails", userRelocateDetails);
        userData.put("profileCompleted", true);

        // Save to Firestore
        db.collection("UsersData").document(userId).set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BioActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BioActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show());
    }

    // Optional: Image Selection from Gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

*/
//everything is ok but image is optional.
/*
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText gender, age, community, familyBackground, workDetails, relocateDetails, eduDetails, timeline, languages;
    private Button doneButton;
    private Uri imageUri;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        // Change Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("profile_images");

        // Initialize Views
        profileImageView = findViewById(R.id.profileImageView);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        familyBackground = findViewById(R.id.familyBackground);
        workDetails = findViewById(R.id.workDetails);
        eduDetails = findViewById(R.id.eduDetails);
        timeline = findViewById(R.id.timeline);
        languages = findViewById(R.id.languages);
        relocateDetails = findViewById(R.id.relocateDetails);
        doneButton = findViewById(R.id.doneButton);

        // Fetch Data if Exists
        fetchUserData();

        // Open Gallery to Select Image
        profileImageView.setOnClickListener(v -> openGallery());

        // Handle Save Button Click
        doneButton.setOnClickListener(v -> saveUserData());
    }

    // Fetch User Data from Firebase Firestore
    private void fetchUserData() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("UsersData").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        gender.setText(documentSnapshot.getString("gender"));
                        age.setText(documentSnapshot.getString("age"));
                        community.setText(documentSnapshot.getString("community"));
                        familyBackground.setText(documentSnapshot.getString("familyBackground"));
                        workDetails.setText(documentSnapshot.getString("workDetails"));
                        eduDetails.setText(documentSnapshot.getString("eduDetails"));
                        timeline.setText(documentSnapshot.getString("timeline"));
                        languages.setText(documentSnapshot.getString("languages"));
                        relocateDetails.setText(documentSnapshot.getString("relocateDetails"));

                        // Fetch Profile Image
                        String imageUrl = documentSnapshot.getString("profileImageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(BioActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.default_profile) // Placeholder image
                                    .error(R.drawable.default_profile) // Error image
                                    .into(profileImageView);
                        }
                        doneButton.setText("Done");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
    }

    // Save or Update User Data in Firestore
    private void saveUserData() {
        String userId = auth.getCurrentUser().getUid();

        // Get input values
        String userGender = gender.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userCommunity = community.getText().toString().trim();
        String userFamilyBackground = familyBackground.getText().toString().trim();
        String userWorkDetails = workDetails.getText().toString().trim();
        String userEduDetails = eduDetails.getText().toString().trim();
        String userTimeline = timeline.getText().toString().trim();
        String userLanguages = languages.getText().toString().trim();
        String userRelocateDetails = relocateDetails.getText().toString().trim();

        // Validate required fields
        if (userGender.isEmpty() || userAge.isEmpty() || userCommunity.isEmpty() ||
                userFamilyBackground.isEmpty() || userWorkDetails.isEmpty() || userEduDetails.isEmpty() ||
                userTimeline.isEmpty() || userLanguages.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate age
        int ageValue;
        try {
            ageValue = Integer.parseInt(userAge);
            if (userGender.equalsIgnoreCase("male") && (ageValue < 21 || ageValue > 100)) {
                Toast.makeText(this, "Male age must be between 21 and 100", Toast.LENGTH_SHORT).show();
                return;
            } else if (userGender.equalsIgnoreCase("female") && (ageValue < 18 || ageValue > 100)) {
                Toast.makeText(this, "Female age must be between 18 and 100", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload Image to Firebase Storage
        StorageReference imageRef = storageRef.child(userId + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Create user data map
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("gender", userGender);
                    userData.put("age", userAge);
                    userData.put("community", userCommunity);
                    userData.put("familyBackground", userFamilyBackground);
                    userData.put("workDetails", userWorkDetails);
                    userData.put("eduDetails", userEduDetails);
                    userData.put("timeline", userTimeline);
                    userData.put("languages", userLanguages);
                    userData.put("relocateDetails", userRelocateDetails);
                    userData.put("profileImageUrl", imageUrl);
                    userData.put("profileCompleted", true);

                    // Save to Firestore
                    db.collection("UsersData").document(userId).set(userData, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(BioActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(BioActivity.this, HomeActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(BioActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    // Open Gallery to Select Image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
*/
/*
// Add firebase code
package com.example.planpair;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private EditText gender, age, community, familyBackground, workDetails, relocateDetails, eduDetails, timeline, languages;
    private Button doneButton;
    private Uri imageUri;
    private String existingImageUrl = null; // Store existing image URL

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        // Change Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("profile_images");

        // Initialize Views
        profileImageView = findViewById(R.id.profileImageView);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        familyBackground = findViewById(R.id.familyBackground);
        workDetails = findViewById(R.id.workDetails);
        eduDetails = findViewById(R.id.eduDetails);
        timeline = findViewById(R.id.timeline);
        languages = findViewById(R.id.languages);
        relocateDetails = findViewById(R.id.relocateDetails);
        doneButton = findViewById(R.id.doneButton);

        // Fetch Data if Exists
        fetchUserData();

        // Open Gallery to Select Image
        profileImageView.setOnClickListener(v -> openGallery());

        // Handle Save Button Click
        doneButton.setOnClickListener(v -> saveUserData());
    }

    // Fetch User Data from Firebase Firestore
    private void fetchUserData() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("UsersData").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        gender.setText(documentSnapshot.getString("gender"));
                        age.setText(documentSnapshot.getString("age"));
                        community.setText(documentSnapshot.getString("community"));
                        familyBackground.setText(documentSnapshot.getString("familyBackground"));
                        workDetails.setText(documentSnapshot.getString("workDetails"));
                        eduDetails.setText(documentSnapshot.getString("eduDetails"));
                        timeline.setText(documentSnapshot.getString("timeline"));
                        languages.setText(documentSnapshot.getString("languages"));
                        relocateDetails.setText(documentSnapshot.getString("relocateDetails"));

                        // Fetch Profile Image
                        existingImageUrl = documentSnapshot.getString("profileImageUrl"); // Store in global variable
                        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                            Glide.with(BioActivity.this)
                                    .load(existingImageUrl)
                                    .placeholder(R.drawable.default_profile) // Placeholder image
                                    .error(R.drawable.default_profile) // Error image
                                    .into(profileImageView);
                        }
                        doneButton.setText("Done");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
    }

    // Save or Update User Data in Firestore
    private void saveUserData() {
        String userId = auth.getCurrentUser().getUid();

        // Get input values
        String userGender = gender.getText().toString().trim();
        String userAge = age.getText().toString().trim();
        String userCommunity = community.getText().toString().trim();
        String userFamilyBackground = familyBackground.getText().toString().trim();
        String userWorkDetails = workDetails.getText().toString().trim();
        String userEduDetails = eduDetails.getText().toString().trim();
        String userTimeline = timeline.getText().toString().trim();
        String userLanguages = languages.getText().toString().trim();
        String userRelocateDetails = relocateDetails.getText().toString().trim();

        // Validate required fields
        if (userGender.isEmpty() || userAge.isEmpty() || userCommunity.isEmpty() ||
                userFamilyBackground.isEmpty() || userWorkDetails.isEmpty() || userEduDetails.isEmpty() ||
                userTimeline.isEmpty() || userLanguages.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate age
        int ageValue;
        try {
            ageValue = Integer.parseInt(userAge);
            if (userGender.equalsIgnoreCase("male") && (ageValue < 21 || ageValue > 100)) {
                Toast.makeText(this, "Male age must be between 21 and 100", Toast.LENGTH_SHORT).show();
                return;
            } else if (userGender.equalsIgnoreCase("female") && (ageValue < 18 || ageValue > 100)) {
                Toast.makeText(this, "Female age must be between 18 and 100", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if it's the first time filling in the profile
        if (existingImageUrl == null && imageUri == null) {
            Toast.makeText(this, "Please enter the profile Image. ", Toast.LENGTH_SHORT).show();
            return;
        }

        // If no new image is selected, use the existing image URL
        if (imageUri == null) {
            saveUserDataToFirestore(userId, userGender, userAge, userCommunity, userFamilyBackground,
                    userWorkDetails, userEduDetails, userTimeline, userLanguages, userRelocateDetails, existingImageUrl);
        } else {
            uploadImageAndSave(userId, userGender, userAge, userCommunity, userFamilyBackground,
                    userWorkDetails, userEduDetails, userTimeline, userLanguages, userRelocateDetails);
        }
    }


    // Upload Image to Firebase Storage
    private void uploadImageAndSave(String userId, String gender, String age, String community,
                                    String familyBackground, String workDetails, String eduDetails,
                                    String timeline, String languages, String relocateDetails) {

        StorageReference imageRef = storageRef.child(userId + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveUserDataToFirestore(userId, gender, age, community, familyBackground,
                            workDetails, eduDetails, timeline, languages, relocateDetails, imageUrl);
                }))
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    // Save Data to Firestore
    private void saveUserDataToFirestore(String userId, String gender, String age, String community,
                                         String familyBackground, String workDetails, String eduDetails,
                                         String timeline, String languages, String relocateDetails, String imageUrl) {

        Map<String, Object> userData = new HashMap<>();
        userData.put("gender", gender);
        userData.put("age", age);
        userData.put("community", community);
        userData.put("familyBackground", familyBackground);
        userData.put("workDetails", workDetails);
        userData.put("eduDetails", eduDetails);
        userData.put("timeline", timeline);
        userData.put("languages", languages);
        userData.put("relocateDetails", relocateDetails);
        userData.put("profileImageUrl", imageUrl);
        userData.put("profileCompleted", true);

        db.collection("UsersData").document(userId).set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BioActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BioActivity.this, InterestActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(BioActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show());
    }

    // Open Gallery to Select Image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri); // Directly set the selected image
        }
    }
}
*/

/*
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Uri selectedImageUri;

    private EditText gender, age, community, dobbirth, timebirth, placebirth, otherbirth, otherdesig, otherindus, otherlang;
    private EditText father, mother, fatherocc, motherocc, highestQual, degree;
    private Button doneButton;
    private ImageView editProfileImg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);


        profileImageView = findViewById(R.id.profileImageView);
        editProfileImg = findViewById(R.id.editProfileimg);

        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        dobbirth = findViewById(R.id.dobbirth);
        timebirth = findViewById(R.id.timebirth);
        placebirth = findViewById(R.id.placebirth);
        otherbirth = findViewById(R.id.otherbirth);
        father = findViewById(R.id.father);
        mother = findViewById(R.id.mother);
        fatherocc = findViewById(R.id.fatherocc);
        motherocc = findViewById(R.id.motherocc);
        highestQual = findViewById(R.id.highestQual);
        degree = findViewById(R.id.degree);
        doneButton = findViewById(R.id.doneButton);

        editProfileImg.setOnClickListener(v -> openImageChooser());

        doneButton.setOnClickListener(v -> {
            if (validateInputs()) {
                Toast.makeText(this, "All data valid! Proceeding...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BioActivity.this, InterestActivity.class));
            }
        });

        // SINGLE SELECTION LOGIC FOR EACH GROUP
        setupSingleSelect(R.id.familyTypeJoint, R.id.familyTypeNuclear, R.id.familyTypeExtended);
        setupSingleSelect(R.id.it, R.id.engineering, R.id.medical, R.id.government, R.id.business, R.id.law,
                R.id.arts, R.id.finance, R.id.education, R.id.aviation, R.id.hospitality, R.id.freelancer,
                R.id.marketing, R.id.realestate, R.id.studentIndustry);
        setupSingleSelect(R.id.engineer, R.id.DA, R.id.doc, R.id.nurse, R.id.CA, R.id.ias,
                R.id.ips, R.id.lawyer, R.id.teacher, R.id.writer, R.id.pilot, R.id.journalist,
                R.id.student, R.id.unemployed, R.id.Looking);
        setupSingleSelect(R.id.below1l, R.id.onelto3l, R.id.threelto5l, R.id.fivelto10l, R.id.tenlto25l, R.id.twentyfive);
        setupSingleSelect(R.id.immediate, R.id.sixM, R.id.oneY, R.id.oneYplus);
        setupSingleSelect(R.id.hindi, R.id.bengali, R.id.marathi, R.id.tamil, R.id.gujrati, R.id.urdu,
                R.id.kannada, R.id.odia, R.id.malayalam, R.id.assamese, R.id.eng);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    private boolean validateInputs() {
        String g = gender.getText().toString().trim().toLowerCase();
        String a = age.getText().toString().trim();
        String dob = dobbirth.getText().toString().trim();
        String tob = timebirth.getText().toString().trim();

        if (g.isEmpty() || a.isEmpty() || community.getText().toString().trim().isEmpty()
                || dob.isEmpty() || tob.isEmpty()
                || placebirth.getText().toString().trim().isEmpty()
                || highestQual.getText().toString().trim().isEmpty()
                || degree.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all compulsory fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        int ageVal;
        try {
            ageVal = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (g.equals("female") && ageVal < 18) {
            Toast.makeText(this, "Minimum age for females is 18", Toast.LENGTH_SHORT).show();
            return false;
        } else if (g.equals("male") && ageVal < 21) {
            Toast.makeText(this, "Minimum age for males is 21", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidDate(dob, "dd/MM/yyyy")) {
            Toast.makeText(this, "Invalid Date of Birth. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidDate(tob, "HH:mm:ss")) {
            Toast.makeText(this, "Invalid Time of Birth. Use HH:mm:ss", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.familyTypeJoint, R.id.familyTypeNuclear, R.id.familyTypeExtended)) {
            Toast.makeText(this, "Please select a Family Type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.it, R.id.engineering, R.id.medical, R.id.government, R.id.business, R.id.law,
                R.id.arts, R.id.finance, R.id.education, R.id.aviation, R.id.hospitality, R.id.freelancer,
                R.id.marketing, R.id.realestate, R.id.studentIndustry)) {
            Toast.makeText(this, "Please select an Industry", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.engineer, R.id.DA, R.id.doc, R.id.nurse, R.id.CA, R.id.ias,
                R.id.ips, R.id.lawyer, R.id.teacher, R.id.writer, R.id.pilot, R.id.journalist,
                R.id.student, R.id.unemployed, R.id.Looking)) {
            Toast.makeText(this, "Please select a Designation", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.below1l, R.id.onelto3l, R.id.threelto5l, R.id.fivelto10l, R.id.tenlto25l, R.id.twentyfive)) {
            Toast.makeText(this, "Please select an Income range", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.immediate, R.id.sixM, R.id.oneY, R.id.oneYplus)) {
            Toast.makeText(this, "Please select a Marriage Timeline", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAnyChecked(R.id.hindi, R.id.bengali, R.id.marathi, R.id.tamil, R.id.gujrati, R.id.urdu,
                R.id.kannada, R.id.odia, R.id.malayalam, R.id.assamese, R.id.eng)) {
            Toast.makeText(this, "Please select at least one Language", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isAnyChecked(int... ids) {
        for (int id : ids) {
            CheckBox cb = findViewById(id);
            if (cb != null && cb.isChecked()) return true;
        }
        return false;
    }

    private boolean isValidDate(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // This ensures only one checkbox in the group is selected
    private void setupSingleSelect(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            CheckBox current = findViewById(ids[i]);
            int finalI = i;
            current.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (int j = 0; j < ids.length; j++) {
                        if (j != finalI) {
                            CheckBox other = findViewById(ids[j]);
                            if (other != null) other.setChecked(false);
                        }
                    }
                }
            });
        }
    }
}
*/

/*
// Add firebase code with new bio activity
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView, editProfileImg;
    private Uri selectedImageUri;

    private EditText gender, age, community, dobbirth, timebirth, placebirth, otherbirth, otherdesig, otherindus, otherlang;
    private EditText father, mother, fatherocc, motherocc, highestQual, degree;
    private Button doneButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        profileImageView = findViewById(R.id.profileImageView);
        editProfileImg = findViewById(R.id.editProfileimg);

        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        dobbirth = findViewById(R.id.dobbirth);
        timebirth = findViewById(R.id.timebirth);
        placebirth = findViewById(R.id.placebirth);
        otherbirth = findViewById(R.id.otherbirth);
        father = findViewById(R.id.father);
        mother = findViewById(R.id.mother);
        fatherocc = findViewById(R.id.fatherocc);
        motherocc = findViewById(R.id.motherocc);
        highestQual = findViewById(R.id.highestQual);
        degree = findViewById(R.id.degree);
        doneButton = findViewById(R.id.doneButton);

        editProfileImg.setOnClickListener(v -> openImageChooser());

        doneButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveToFirestore();
            }
        });

        setupSingleSelect(R.id.familyTypeJoint, R.id.familyTypeNuclear, R.id.familyTypeExtended);
        // Add other single select groups here...

        // Fetch data from Firestore
        fetchUserData();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveToFirestore() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("gender", gender.getText().toString().trim());
        userMap.put("age", age.getText().toString().trim());
        userMap.put("community", community.getText().toString().trim());
        userMap.put("dobbirth", dobbirth.getText().toString().trim());
        userMap.put("timebirth", timebirth.getText().toString().trim());
        userMap.put("placebirth", placebirth.getText().toString().trim());
        userMap.put("otherbirth", otherbirth.getText().toString().trim());
        userMap.put("father", father.getText().toString().trim());
        userMap.put("mother", mother.getText().toString().trim());
        userMap.put("fatherocc", fatherocc.getText().toString().trim());
        userMap.put("motherocc", motherocc.getText().toString().trim());
        userMap.put("highestQual", highestQual.getText().toString().trim());
        userMap.put("degree", degree.getText().toString().trim());

        if (selectedImageUri != null) {
            userMap.put("profileImageUri", selectedImageUri.toString());
        }

        db.collection("UsersData").document(uid)
                .set(userMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BioActivity.this, InterestActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchUserData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("UserData").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        gender.setText(documentSnapshot.getString("gender"));
                        age.setText(documentSnapshot.getString("age"));
                        community.setText(documentSnapshot.getString("community"));
                        dobbirth.setText(documentSnapshot.getString("dobbirth"));
                        timebirth.setText(documentSnapshot.getString("timebirth"));
                        placebirth.setText(documentSnapshot.getString("placebirth"));
                        otherbirth.setText(documentSnapshot.getString("otherbirth"));
                        father.setText(documentSnapshot.getString("father"));
                        mother.setText(documentSnapshot.getString("mother"));
                        fatherocc.setText(documentSnapshot.getString("fatherocc"));
                        motherocc.setText(documentSnapshot.getString("motherocc"));
                        highestQual.setText(documentSnapshot.getString("highestQual"));
                        degree.setText(documentSnapshot.getString("degree"));

                        // Fetch Profile Image
                        String imageUrl = documentSnapshot.getString("profileImageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(BioActivity.this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.default_profile) // Placeholder image
                                    .error(R.drawable.default_profile) // Error image
                                    .into(profileImageView);
                        }
                        doneButton.setText("Update");
                    }
                    doneButton.setText("Done");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BioActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateInputs() {
        // Validation logic same as before...
        String g = gender.getText().toString().trim().toLowerCase();
        String a = age.getText().toString().trim();
        String dob = dobbirth.getText().toString().trim();
        String tob = timebirth.getText().toString().trim();

        if (g.isEmpty() || a.isEmpty() || community.getText().toString().trim().isEmpty()
                || dob.isEmpty() || tob.isEmpty()
                || placebirth.getText().toString().trim().isEmpty()
                || highestQual.getText().toString().trim().isEmpty()
                || degree.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all compulsory fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        int ageVal;
        try {
            ageVal = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (g.equals("female") && ageVal < 18) {
            Toast.makeText(this, "Minimum age for females is 18", Toast.LENGTH_SHORT).show();
            return false;
        } else if (g.equals("male") && ageVal < 21) {
            Toast.makeText(this, "Minimum age for males is 21", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidDate(dob, "dd/MM/yyyy")) {
            Toast.makeText(this, "Invalid Date of Birth. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidDate(tob, "HH:mm:ss")) {
            Toast.makeText(this, "Invalid Time of Birth. Use HH:mm:ss", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidDate(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void setupSingleSelect(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            CheckBox current = findViewById(ids[i]);
            int finalI = i;
            current.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (int j = 0; j < ids.length; j++) {
                        if (j != finalI) {
                            CheckBox other = findViewById(ids[j]);
                            if (other != null) other.setChecked(false);
                        }
                    }
                }
            });
        }
    }
}
*/

//new bio page with add firebase and image is mandatory

package com.example.planpair;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BioActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView, editProfileImg;
    private Uri selectedImageUri;

    private EditText gender, age, community, dobbirth, timebirth, placebirth, otherbirth, otherdesig, otherindus, otherlang;
    private EditText father, mother, fatherocc, motherocc, highestQual, degree;
    // IDs must match your XML layout
    private int[] familyTypeCheckboxesId = {R.id.familyTypeJoint, R.id.familyTypeNuclear, R.id.familyTypeExtended};
    private String[] familyTypeValues = {"Joint", "Nuclear", "Extended"};

    private int[] industryCheckboxIds = {
            R.id.it, R.id.engineering, R.id.medical, R.id.government, R.id.business,
            R.id.law, R.id.arts, R.id.finance, R.id.education, R.id.aviation,
            R.id.hospitality, R.id.freelancer, R.id.marketing, R.id.realestate, R.id.studentIndustry
    };
    private String[] industryValues = {
            "IT", "Engineering", "Medical", "Government", "Business",
            "Law", "Arts", "Finance", "Education", "Aviation",
            "Hospitality", "Freelancer", "Marketing", "Real Estate", "Student"
    };

    private int[] designationCheckboxIds = {
            R.id.engineer, R.id.DA, R.id.doc, R.id.nurse, R.id.CA,R.id.ias, R.id.ips, R.id.lawyer, R.id.teacher, R.id.writer ,R.id.pilot,
            R.id.journalist, R.id.student, R.id.unemployed ,R.id.Looking
    };
    private String[] designationValues = {
            "Engineer", "Data Analyst", "Doctor", "Nurse", "CA", "IAS", "IPS", "Lawyer",
            "Teacher", "Writer", "Pilot", "Journalist", "Student",
            "Unemployed", "Looking for Job"
    };

    private int[] incomeCheckboxIds = {
            R.id.below1l, R.id.onelto3l, R.id.threelto5l, R.id.fivelto10l, R.id.tenlto25l,R.id.twentyFivePlus
    };
    private String[] incomeValues = {
            "Below 1L", "1L to 3L", "3L to 5L", "5L to 10L", "10L to 25L", "25L+"
    };

    private int[] marriageTimelineCheckboxIds = {
            R.id.marriageImmediate,R.id.marriageSixM, R.id.marriageWithinOneYear, R.id.marriageAfterOneYear
    };
    private String[] marriageTimelineValues = {
            "Immediate", "Within 6 Months", "Within 1 Year", "After 1 Year"
    };

    private int[] languageCheckboxIds = {
            R.id.hindi, R.id.bengali, R.id.marathi, R.id.tamil, R.id.gujrati, R.id.urdu, R.id.kannada, R.id.odia, R.id.malayalam, R.id.assamese,
            R.id.english
    };
    private String[] languageValues = {
            "Hindi", "Bengali","Marathi", "Tamil","Gujrati","Urdu","Kannada","Odia","Malayalam","Assamese","English"
    };


    private Button doneButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;

    private String currentProfileImageUrl = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.sagegreen));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        profileImageView = findViewById(R.id.profileImageView);
        editProfileImg = findViewById(R.id.editProfileimg);

        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        community = findViewById(R.id.community);
        dobbirth = findViewById(R.id.dobbirth);
        timebirth = findViewById(R.id.timebirth);
        placebirth = findViewById(R.id.placebirth);
        otherbirth = findViewById(R.id.otherbirth);
        otherdesig = findViewById(R.id.otherdesig);
        otherindus = findViewById(R.id.otherindus);
        otherlang = findViewById(R.id.otherlang);
        father = findViewById(R.id.father);
        mother = findViewById(R.id.mother);
        fatherocc = findViewById(R.id.fatherocc);
        motherocc = findViewById(R.id.motherocc);
        highestQual = findViewById(R.id.highestQual);
        degree = findViewById(R.id.degree);
        doneButton = findViewById(R.id.doneButton);

        editProfileImg.setOnClickListener(v -> openImageChooser());
        setupCheckboxGroups();

        doneButton.setOnClickListener(v -> {
            if (validateInputs()) {
                uploadImageAndSaveData();
            }
        });

        loadUserData(); // Load existing data
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);
        }
    }


    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid();
        firestore.collection("UsersData").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        gender.setText(document.getString("gender"));
                        age.setText(document.getString("age"));
                        community.setText(document.getString("community"));
                        dobbirth.setText(document.getString("dob"));
                        timebirth.setText(document.getString("tob"));
                        placebirth.setText(document.getString("placebirth"));
                        otherbirth.setText(document.getString("otherBirthDetalis"));
                        father.setText(document.getString("fatherName"));
                        mother.setText(document.getString("motherName"));
                        fatherocc.setText(document.getString("fatherOcc"));
                        motherocc.setText(document.getString("motherOcc"));
                        otherdesig.setText(document.getString("otherDesignation"));
                        otherindus.setText(document.getString("otherIndustry"));
                        highestQual.setText(document.getString("highestQual"));
                        degree.setText(document.getString("degree"));
                        otherlang.setText(document.getString("otherLanguage"));

                        currentProfileImageUrl = document.getString("profileImageUrl");
                        if (currentProfileImageUrl != null && !currentProfileImageUrl.isEmpty()) {
                            Glide.with(BioActivity.this)
                                    .load(currentProfileImageUrl)
                                    .placeholder(R.drawable.default_profile)
                                    .error(R.drawable.default_profile)
                                    .into(profileImageView);
                        }

                        // Restore checkboxes
                        preselectCheckboxList((List<String>) document.get("familyType"), familyTypeCheckboxesId, familyTypeValues);
                        preselectCheckboxList((List<String>) document.get("industry"), industryCheckboxIds, industryValues);
                        preselectCheckboxList((List<String>) document.get("designation"), designationCheckboxIds, designationValues);
                        preselectCheckboxList((List<String>) document.get("income"), incomeCheckboxIds, incomeValues);
                        preselectCheckboxList((List<String>) document.get("marriage"), marriageTimelineCheckboxIds, marriageTimelineValues);
                        preselectCheckboxList((List<String>) document.get("language"), languageCheckboxIds, languageValues);
                    }
                });
    }

    //Replace your preselectCheckbox and getSelectedCheckboxText methods
    private void preselectCheckboxList(List<String> savedList, int[] ids, String[] values) {
        if (savedList == null) return;
        for (int i = 0; i < ids.length; i++) {
            CheckBox cb = findViewById(ids[i]);
            cb.setChecked(savedList.contains(values[i]));
        }
    }

    private List<String> getSelectedCheckboxList(int[] ids, String[] values) {
        List<String> selectedList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            CheckBox cb = findViewById(ids[i]);
            if (cb.isChecked()) {
                selectedList.add(values[i]);
            }
        }
        return selectedList;
    }


    private boolean validateInputs() {
        String g = gender.getText().toString().trim().toLowerCase();
        String a = age.getText().toString().trim();
        String dob = dobbirth.getText().toString().trim();
        String tob = timebirth.getText().toString().trim();

        if (selectedImageUri == null && (currentProfileImageUrl == null || currentProfileImageUrl.isEmpty())) {
            Toast.makeText(this, "Profile image is mandatory", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (g.isEmpty() || a.isEmpty() || community.getText().toString().trim().isEmpty()
                || dob.isEmpty() || tob.isEmpty()
                || placebirth.getText().toString().trim().isEmpty()
                || highestQual.getText().toString().trim().isEmpty()
                || degree.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all compulsory fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        int ageVal;
        try {
            ageVal = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Age must be a number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((g.equals("female") && ageVal < 18) || (g.equals("male") && ageVal < 21)) {
            Toast.makeText(this, "Minimum age: 18 (F), 21 (M)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidDate(dob, "dd/MM/yyyy")) {
            Toast.makeText(this, "Invalid DOB. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidDate(tob, "HH:mm:ss")) {
            Toast.makeText(this, "Invalid TOB. Use HH:mm:ss", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadImageAndSaveData() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Saving...");
        dialog.show();

        final String uid = mAuth.getCurrentUser().getUid();

        if (selectedImageUri != null) {
            final StorageReference fileRef = storageRef.child(uid + ".jpg");
            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveUserDataToFirestore(uri.toString());
                        dialog.dismiss();
                    }))
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveUserDataToFirestore(currentProfileImageUrl);
            dialog.dismiss();
        }
    }

    private void saveUserDataToFirestore(String imageUrl) {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection("UsersData").document(uid);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("gender", gender.getText().toString().trim());
        userMap.put("age", age.getText().toString().trim());
        userMap.put("community", community.getText().toString().trim());
        // Birth details
        userMap.put("dob", dobbirth.getText().toString().trim());
        userMap.put("tob", timebirth.getText().toString().trim());
        userMap.put("placebirth", placebirth.getText().toString().trim());
        userMap.put("otherBirthDetalis", otherbirth.getText().toString().trim());
        // Family background
        userMap.put("fatherName", father.getText().toString().trim());
        userMap.put("motherName", mother.getText().toString().trim());
        userMap.put("fatherOcc", fatherocc.getText().toString().trim());
        userMap.put("motherOcc", motherocc.getText().toString().trim());
        // Industry & Designation
        userMap.put("otherIndustry", otherindus.getText().toString().trim());
        userMap.put("otherDesignation", otherdesig.getText().toString().trim());
        // Education
        userMap.put("highestQual", highestQual.getText().toString().trim());
        userMap.put("degree", degree.getText().toString().trim());
        // Language
        userMap.put("otherLanguage", otherlang.getText().toString().trim());
        // Profile Image
        userMap.put("profileImageUrl", imageUrl);
        // Checkboxes
        userMap.put("familyType", getSelectedCheckboxList(familyTypeCheckboxesId, familyTypeValues));
        userMap.put("industry", getSelectedCheckboxList(industryCheckboxIds, industryValues));
        userMap.put("designation", getSelectedCheckboxList(designationCheckboxIds, designationValues));
        userMap.put("income", getSelectedCheckboxList(incomeCheckboxIds, incomeValues));
        userMap.put("marriage", getSelectedCheckboxList(marriageTimelineCheckboxIds, marriageTimelineValues));
        userMap.put("language", getSelectedCheckboxList(languageCheckboxIds, languageValues));

        userRef.set(userMap, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    // Set isBioCompleted to true
                    userRef.update("isBioCompleted", true);

                    Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BioActivity.this, InterestActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show());
    }

    private boolean isValidDate(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    private void setupCheckboxGroups() {
        setupSingleSelect(familyTypeCheckboxesId); // one at a time
        setupSingleSelect(industryCheckboxIds);     // one at a time
        setupSingleSelect(designationCheckboxIds);  // one at a time
        setupSingleSelect(incomeCheckboxIds);       // one at a time
        setupSingleSelect(marriageTimelineCheckboxIds); // one at a time
    }

    private void setupSingleSelect(int[] ids) {
        for (int i = 0; i < ids.length; i++) {
            CheckBox current = findViewById(ids[i]);
            int finalI = i;
            current.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    for (int j = 0; j < ids.length; j++) {
                        if (j != finalI) {
                            CheckBox other = findViewById(ids[j]);
                            if (other != null) other.setChecked(false);
                        }
                    }
                }
            });
        }
    }
}