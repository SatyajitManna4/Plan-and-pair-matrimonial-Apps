package com.example.planpair;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.DecorPhotosAdapter;
import com.example.planpair.models.Decoration;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DecorationDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameView, locationView, ratingView, priceView, tagView, servicesView;
    private ImageButton backButton, wishlistButton;
    private Button contactButton, aboutButton, whatsappButton, shareButton;
    private RecyclerView decorPhotosRecyclerView;
    private String aboutText = "";


    private static final String PHONE_NUMBER = "+919876543210";
    private static final String PREF_NAME = "wishlist_prefs";
    private static final String CATEGORY = "decoration";

    private Decoration currentDecoration;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_detail);

        String vendorId = getIntent().getStringExtra("decorId");

        initViews();

        Intent intent = getIntent();
        currentDecoration = buildDecorationFromIntent(intent);

        if (currentDecoration == null || currentDecoration.getId() == null) {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set data to views

//        imageView.setImageResource(currentDecoration.getImageUrl());
//        nameView.setText(currentDecoration.getName());
//        locationView.setText(currentDecoration.getLocation());
//        ratingView.setText("★ " + currentDecoration.getRating() + " (" + currentDecoration.getReviewCount() + " reviews)");
//        priceView.setText("Starting Price For Decor \n ₹" + currentDecoration.getStartingPrice() + " Onwards");
//        tagView.setText(currentDecoration.getTag());
//        servicesView.setText("Services: " + currentDecoration.getServices());
//        aboutText = currentDecoration.getAbout();

        imageView.setImageResource(currentDecoration.getImageUrl());
        nameView.setText(currentDecoration.getName());
        locationView.setText(currentDecoration.getLocation());
//        double rating = intent.getDoubleExtra("rating", 0.0);
//        int reviewCount = intent.getIntExtra("reviewCount", 0);
        ratingView.setText("★ "+ currentDecoration.getRating() + " (" + currentDecoration.getReviewCount() + " reviews)");
//        int startingPrice = intent.getIntExtra("startingPrice", 0);
        priceView.setText("Starting Price For Decor \n ₹" + currentDecoration.getStartingPrice() + " Onwards");
        tagView.setText(currentDecoration.getTag());
//        servicesView.setText(intent.getStringExtra("services"));
//        String services = intent.getStringExtra("services");
        servicesView.setText("Services: "+ currentDecoration.getServices());
        aboutText = currentDecoration.getAbout();
        if (aboutText == null || aboutText.trim().isEmpty()) {
            aboutText = "No additional information available.";
        }


        // Wishlist logic
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = CATEGORY + "_" + currentDecoration.getId();
        boolean isLiked = prefs.contains(key);
        updateWishlistIcon(isLiked);

        wishlistButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (!prefs.contains(key)) {
                String json = gson.toJson(currentDecoration);
                editor.putString(key, json);
                editor.apply();
                updateWishlistIcon(true);
                Toast.makeText(this, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show();
            } else {
                editor.remove(key);
                editor.apply();
                updateWishlistIcon(false);
                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
            }
        });

        // Load photo gallery
        ArrayList<Integer> photoList = getIntent().getIntegerArrayListExtra("Decoration_photos");
        if (photoList != null && !photoList.isEmpty()) {
            DecorPhotosAdapter adapter = new DecorPhotosAdapter(this, photoList);
            decorPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            decorPhotosRecyclerView.setAdapter(adapter);
        }

        setupButtonListeners();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageViewDecoration);
        nameView = findViewById(R.id.textViewName);
        locationView = findViewById(R.id.textViewLocation);
        ratingView = findViewById(R.id.textViewRating);
        priceView = findViewById(R.id.textViewStartingPrice);
        tagView = findViewById(R.id.textViewTag);
        servicesView = findViewById(R.id.detailServices);
        backButton = findViewById(R.id.backButton);
        wishlistButton = findViewById(R.id.likeButton);
        contactButton = findViewById(R.id.contactButton);
        aboutButton = findViewById(R.id.aboutButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        shareButton = findViewById(R.id.shareButton);
        decorPhotosRecyclerView = findViewById(R.id.decorPhotosRecyclerView);
    }

    private Decoration buildDecorationFromIntent(Intent intent) {
        String id = intent.getStringExtra("decorId");
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        double rating = intent.getDoubleExtra("rating",0.0);
        int reviews = intent.getIntExtra("reviewCount", 0);
        int imageRes = intent.getIntExtra("imageResId", R.drawable.photographer);
        int price = intent.getIntExtra("startingPrice", 0);

        String service = intent.getStringExtra("services");
        String about = intent.getStringExtra("about");
        ArrayList<Integer> photos = intent.getIntegerArrayListExtra("Decoration_photos");

        return new Decoration(id,name,location,rating,reviews,imageRes,price,"",service,about,photos);
    }

    private void updateWishlistIcon(boolean isLiked) {
        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
    }

    private void setupButtonListeners() {
        contactButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
            startActivity(callIntent);
        });

        whatsappButton.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=" + PHONE_NUMBER;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        aboutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("About the Decorator")
                    .setMessage(aboutText != null && !aboutText.isEmpty() ? aboutText : "No additional information available.")
                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        shareButton.setOnClickListener(v -> {
            String shareText = "Check out this decorator on Plan & Pair:\n\n"
                    + "Name: " + nameView.getText() + "\n"
                    + "Location: " + locationView.getText() + "\n"
                    + "Rating: " + ratingView.getText() + "\n"
                    + "Starting Price: " + priceView.getText() + "\n"
                    + "Specialty: " + tagView.getText() + "\n\n"
                    + "Plan your dream wedding easily with Plan & Pair!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedding Decorator Recommendation");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}
