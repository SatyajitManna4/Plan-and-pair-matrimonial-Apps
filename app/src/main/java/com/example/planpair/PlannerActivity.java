
package com.example.planpair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlannerActivity  extends AppCompatActivity {

    EditText yourNameEditText, yourPartnerNameEditText;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner); // Ensure XML file is named activity_main.xml

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        // Bind views
        yourNameEditText = findViewById(R.id.yourName);
        yourPartnerNameEditText = findViewById(R.id.yourPartnerName);
        doneButton= findViewById(R.id.continueButton);

        // Button click listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yourName = yourNameEditText.getText().toString().trim();
                String partnerName = yourPartnerNameEditText.getText().toString().trim();

                if (yourName.isEmpty() || partnerName.isEmpty()) {
                    Toast.makeText(PlannerActivity .this, "Please fill both names", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Save data using SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("weddingPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("yourName", yourName);
                    editor.putString("partnerName", partnerName);
                    editor.apply(); // use commit() if you want synchronous save

                    String message = "Hello " + yourName + " & " + partnerName + "!";
                    Toast.makeText(PlannerActivity .this, message + "\nSaved successfully!", Toast.LENGTH_LONG).show();

                    // Optional: navigate to another activity here
                    Intent intent = new Intent(PlannerActivity .this, SecondActivity.class);
                    intent.putExtra("NAME1", yourName);
                    intent.putExtra("NAME2", partnerName);
                    startActivity(intent);
                }
            }
        });
    }
}