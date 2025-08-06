package com.example.planpair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class VendorActivity extends AppCompatActivity {

    private ImageButton backButton ;
    private TextView coupleNameText, weddingDateText,vendors;
    private CardView wcard1, wcard2, wcard3, wcard4, wcard5;
    private ImageButton todolist,wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);// Make sure this matches your layout file name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        backButton= findViewById(R.id.backButton);
        coupleNameText = findViewById(R.id.coupleNameText);
        weddingDateText = findViewById(R.id.weddingDateText);
        vendors= findViewById(R.id.vendors);

        wcard1 = findViewById(R.id.wcard1);
        wcard2 = findViewById(R.id.wcard2);
        wcard3 = findViewById(R.id.wcard3);
        wcard4 = findViewById(R.id.wcard4);
        wcard5 = findViewById(R.id.wcard5);
        todolist = findViewById(R.id.todoIcon);
        wishlist = findViewById(R.id.wishlistIcon);

        // Get names and wedding date from Intent
        String yourName = getIntent().getStringExtra("NAME1");
        String partnerName = getIntent().getStringExtra("NAME2");
        String weddingDate = getIntent().getStringExtra("WEDDING_DATE");

        // Set the names
        if (yourName != null && partnerName != null) {
            coupleNameText.setText(yourName + " ❤️ " + partnerName);
        }

        // Set the wedding date
        if (weddingDate != null) {
            weddingDateText.setText(weddingDate);
        } else {
            // Optionally fetch from SharedPreferences if not passed in intent
            SharedPreferences sharedPreferences = getSharedPreferences("weddingPrefs", MODE_PRIVATE);
            String savedDate = sharedPreferences.getString("weddingDate", "Not Set");
            weddingDateText.setText("Wedding Date: " + savedDate);
        }
        if(yourName !=null){
            vendors.setText("Hey "+yourName+ "\nWhat're you looking for ?");
        }

        wcard1.setOnClickListener(view -> {
            Intent intent=new Intent(VendorActivity.this,VenueActivity.class);
            startActivity(intent);
        });
        wcard2.setOnClickListener(view -> {
            Intent intent=new Intent(VendorActivity.this,PhotographerActivity.class);
            startActivity(intent);
        });
        wcard3.setOnClickListener(view -> {
            Intent intent=new Intent(VendorActivity.this,MakeupActivity.class);
            startActivity(intent);
        });
        wcard4.setOnClickListener(view -> {
            Intent intent=new Intent(VendorActivity.this,DecorActivity.class);
            startActivity(intent);
        });
        wcard5.setOnClickListener(view -> {
            Intent intent=new Intent(VendorActivity.this,CateringActivity.class);
            startActivity(intent);
        });
        wishlist.setOnClickListener(v -> {
            // Navigate to WishlistActivity or open a dialog
            Intent intent = new Intent(VendorActivity.this, WishlistActivity.class);
            startActivity(intent);
        });

        todolist.setOnClickListener(v -> {
            // Navigate to ToDoListActivity
            Intent intent = new Intent(VendorActivity.this, ToDoListActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

    }
}
