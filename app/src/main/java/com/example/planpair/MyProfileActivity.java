/*
package com.example.planpair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public
class MyProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Initialize TextViews and Buttons
        TextView interestTxtView = findViewById(R.id.interestTxtView);
        TextView bioTxtView = findViewById(R.id.bioTxtView);
        Button logoutButton = findViewById(R.id.logout_button);
        TextView weddingGoalTxtView = findViewById(R.id.weddingGoalTxtView);

        // //Bio Activity button functionality
        bioTxtView.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, BioActivity.class);
            startActivity(intent);
        });

        //Interest button functionality
        interestTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, InterestActivity.class);
                startActivity(intent);
            }
        });
        //wedding button functionality
        weddingGoalTxtView.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, WeddingGoalActivity.class);
            startActivity(intent);
        });

        // Logout button functionality
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent iHome = new Intent(MyProfileActivity.this, LoginActivity.class);
            startActivity(iHome);
            finish();
        });

    }
}
*/

//updated code for MyProfileActivity

package com.example.planpair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView nameText,ageText, religionText, deleteAccount, logout, editPersonalDetails, editInterestDetails, editWeddingDetails;
    private FirebaseAuth mAuth;
    private String userId;
    private ProgressDialog progressDialog;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
            return;
        }

        userId = currentUser.getUid();

        // Bind views
        profileImage = findViewById(R.id.profileImage);
        nameText = findViewById(R.id.nameText);
        ageText=findViewById(R.id.ageText);
        religionText = findViewById(R.id.religionText);


        editPersonalDetails = findViewById(R.id.editPersonalDetails);
        editInterestDetails = findViewById(R.id.editInterestDetails);
        editWeddingDetails = findViewById(R.id.editWeddingDetails);

        deleteAccount = findViewById(R.id.delAcc);
        logout = findViewById(R.id.logout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_myprof); // Default selected

        // Bottom nav clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(MyProfileActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_liked) {
                startActivity(new Intent(MyProfileActivity.this, LikedActivity .class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(MyProfileActivity.this, ChatListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (itemId == R.id.nav_planner) {
                startActivity(new Intent(MyProfileActivity.this, PlannerActivity.class));
                return true;
            }
            else if (itemId == R.id.nav_myprof) {
                return true; // already here
            }
            return false;
        });

        loadProfileImage();
        loadUserData();

        editPersonalDetails.setOnClickListener(v -> startActivity(new Intent(this, BioActivity.class)));
        editInterestDetails.setOnClickListener(v -> startActivity(new Intent(this, InterestActivity.class)));
        editWeddingDetails.setOnClickListener(v -> startActivity(new Intent(this, WeddingGoalActivity.class)));

        logout.setOnClickListener(v -> logoutUser());
        deleteAccount.setOnClickListener(v -> confirmDelete());
    }

    private void loadProfileImage() {
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("profile_images/" + userId + ".jpg");

        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    if (!MyProfileActivity.this.isFinishing() && !MyProfileActivity.this.isDestroyed()) {
                        Glide.with(MyProfileActivity.this)
                                .load(uri)
                                .placeholder(R.drawable.default_profile)
                                .into(profileImage);
                    } else {
                        Log.w("GlideError", "Activity destroyed or finishing - skipped image load");
                    }
                })
                .addOnFailureListener(e -> Log.w("LoadImage", "Image not found", e));
    }



    private void loadUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("UsersData").document(userId);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("username");
                String age = documentSnapshot.getString("age");
                String religion = documentSnapshot.getString("religion");

                nameText.setText(name != null ? "Name:"+name : "username");
                ageText.setText(age != null ? "Age:"+age : "age");
                religionText.setText(religion != null ? "Religion:"+religion : "religion");
            } else {
                Toast.makeText(this, "User data does not exist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteAccount(String email, String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        // Show progress dialog while deleting
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting your account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Step 0: Re-authenticate
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnSuccessListener(unused -> {
                    String userId = user.getUid();

                    // References to storage and Firestore paths
                    StorageReference imageRef = FirebaseStorage.getInstance()
                            .getReference("profile_images/" + userId + ".jpg");
                    DocumentReference userDocRef = FirebaseFirestore.getInstance()
                            .collection("UsersData").document(userId);

                    // Step 1: Delete profile image
                    imageRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                // Step 2: Delete Firestore document
                                userDocRef.delete()
                                        .addOnSuccessListener(aVoid2 -> {
                                            // Step 3: Delete Firebase Auth user
                                            user.delete()
                                                    .addOnSuccessListener(aVoid3 -> {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(MyProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                        redirectToLogin();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(MyProfileActivity.this, "Failed to delete user account: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    });
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(MyProfileActivity.this, "Failed to delete user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Even if image deletion fails, continue deleting user doc and auth user
                                userDocRef.delete()
                                        .addOnSuccessListener(aVoid2 -> {
                                            user.delete()
                                                    .addOnSuccessListener(aVoid3 -> {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(MyProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                        redirectToLogin();
                                                    })
                                                    .addOnFailureListener(e2 -> {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(MyProfileActivity.this, "Failed to delete user account: " + e2.getMessage(), Toast.LENGTH_LONG).show();
                                                    });
                                        })
                                        .addOnFailureListener(e2 -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(MyProfileActivity.this, "Failed to delete user data: " + e2.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyProfileActivity.this, "Re-authentication failed. Please check your email and password.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        builder.setView(layout);

        builder.setMessage("Enter your email and password to confirm account deletion.\nThis action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    String email = emailInput.getText().toString().trim();
                    String password = passwordInput.getText().toString().trim();

                    if (!email.isEmpty() && !password.isEmpty()) {
                        deleteAccount(email, password);
                    } else {
                        Toast.makeText(MyProfileActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logoutUser() {
        mAuth.signOut();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}