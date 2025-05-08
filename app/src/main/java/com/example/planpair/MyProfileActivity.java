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