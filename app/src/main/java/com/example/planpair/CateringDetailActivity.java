//package com.example.planpair;
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.planpair.Adapter.CatererPhotosAdapter;
//import com.example.planpair.Model.Vendor;
//import com.example.planpair.Utils.MyWishlistManager;
//
//import java.util.ArrayList;
//
//public class CateringDetailActivity extends AppCompatActivity {
//
//    private ImageView imageView;
//    private TextView nameView, locationView, ratingView, priceView, servicesView;
//    private ImageButton backButton, wishlistButton;
//    private Button contactButton, aboutButton, whatsappButton, shareButton;
//    private RecyclerView cateringPhotosRecyclerView;
//    private String aboutText = "";
//
//    private static final String PHONE_NUMBER = "+919876543210";
//    private static final String PREFS_NAME = "catering_likes"; // universal
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_catering_detail);
//        initViews();
//
//        Intent intent = getIntent();
//
//        // Extract values and set UI
//        int imageRes = intent.getIntExtra("imageUrl", R.drawable.catering);
//        String vendorName = intent.getStringExtra("name");
//        String vendorLocation = intent.getStringExtra("location");
//        double rating = intent.getDoubleExtra("rating", 0.0);
//        int reviewCount = intent.getIntExtra("reviewCount", 0);
//        int startingPrice = intent.getIntExtra("cateringPrice", 0);
//        String services = intent.getStringExtra("services");
//        aboutText = intent.getStringExtra("about");
//
//        if (aboutText == null || aboutText.trim().isEmpty()) {
//            aboutText = "No additional information available.";
//        }
//
//        imageView.setImageResource(imageRes);
//        nameView.setText(vendorName);
//        locationView.setText(vendorLocation);
//        ratingView.setText("★ " + rating + " (" + reviewCount + " reviews)");
//        priceView.setText("Starting Price \n ₹" + startingPrice + " per Plate");
//        servicesView.setText("Services: " + services);
//
//        // --- Universal Wishlist Logic ---
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        String vendorId = intent.getStringExtra("catererId");
//        if (vendorId == null) vendorId = "default_caterer";
//        String category = "caterer";
//        String key = category + "_" + vendorId;
//
//        Vendor vendor = new Vendor(
//                category,         // "caterer"
//                vendorId,
//                vendorName,
//                vendorLocation,
//                rating,
//                reviewCount,
//                startingPrice,
//                imageRes,         // int
//                services,         // String
//                aboutText         // String
//        );
//
//
//        boolean isLiked = prefs.getBoolean(key, false);
//        updateWishlistIcon(isLiked);
//
//        wishlistButton.setOnClickListener(v -> {
//            boolean liked = prefs.getBoolean(key, false);
//            SharedPreferences.Editor editor = prefs.edit();
//
//            if (!liked) {
//                updateWishlistIcon(true);
//                editor.putBoolean(key, true);
////                MyWishlistManager.getInstance().addVendor(vendor);
//                Toast.makeText(this, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show();
//            } else {
//                updateWishlistIcon(false);
//                editor.remove(key);
////                MyWishlistManager.getInstance().removeVendor(key);
//                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
//            }
//
//            editor.apply();
//        });
//
//        // Load photos
//        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("Catering_photos");
//        if (photoList != null && !photoList.isEmpty()) {
//            CatererPhotosAdapter adapter = new CatererPhotosAdapter(this, photoList);
//            cateringPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//            cateringPhotosRecyclerView.setAdapter(adapter);
//        }
//
//        setupButtonListeners();
//    }
//
//    private void initViews() {
//        imageView = findViewById(R.id.imageViewCatering);
//        nameView = findViewById(R.id.textServiceName);
//        locationView = findViewById(R.id.textViewLocation);
//        ratingView = findViewById(R.id.textViewRating);
//        priceView = findViewById(R.id.textViewStartingPrice);
//        servicesView = findViewById(R.id.detailServices);
//        backButton = findViewById(R.id.backButton);
//        wishlistButton = findViewById(R.id.likeButton);
//        contactButton = findViewById(R.id.contactButton);
//        aboutButton = findViewById(R.id.aboutButton);
//        whatsappButton = findViewById(R.id.whatsappButton);
//        shareButton = findViewById(R.id.shareButton);
//        cateringPhotosRecyclerView = findViewById(R.id.cateringPhotosRecyclerView);
//    }
//
//    private void updateWishlistIcon(boolean isLiked) {
//        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
//        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
//    }
//
//    private void setupButtonListeners() {
//        contactButton.setOnClickListener(v -> {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
//            startActivity(callIntent);
//        });
//
//        whatsappButton.setOnClickListener(v -> {
//            String url = "https://api.whatsapp.com/send?phone=" + PHONE_NUMBER;
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//            startActivity(intent);
//        });
//
//        aboutButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(this)
//                    .setTitle("About the Caterer")
//                    .setMessage(aboutText)
//                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
//                    .show();
//        });
//
//        shareButton.setOnClickListener(v -> {
//            String shareText = "Check out this caterer on Plan & Pair:\n\n"
//                    + "Name: " + nameView.getText() + "\n"
//                    + "Location: " + locationView.getText() + "\n"
//                    + "Rating: " + ratingView.getText() + "\n"
//                    + "Starting Price: " + priceView.getText() + "\n\n"
//                    + "Plan your dream wedding easily with Plan & Pair!";
//
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedding Caterer Recommendation");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//            startActivity(Intent.createChooser(shareIntent, "Share via"));
//        });
//
//        backButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, CateringActivity.class));
//            finish();
//        });
//    }
//}
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

import com.example.planpair.adapters.CatererPhotosAdapter;
import com.example.planpair.models.Catering;
import com.example.planpair.models.MakeupArtist;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CateringDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameView, locationView, ratingView, priceView, servicesView;
    private ImageButton backButton, wishlistButton;
    private Button contactButton, aboutButton, whatsappButton, shareButton;
    private RecyclerView cateringPhotosRecyclerView;
    private String aboutText = "";

    private static final String PHONE_NUMBER = "+919876543210";
    private static final String PREF_NAME = "wishlist_prefs";
    private static final String CATEGORY = "caterer";

    private Catering currentCatering;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_detail);

        initViews();

        Intent intent = getIntent();
        currentCatering = buildCateringFromIntent(intent);

        if (currentCatering == null || currentCatering.getId() == null) {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Set data
        imageView.setImageResource(currentCatering.getImageUrl());
        nameView.setText(currentCatering.getName());
        locationView.setText(currentCatering.getLocation());
//        double rating = intent.getDoubleExtra("rating", 0.0);
//        int reviewCount = intent.getIntExtra("reviewCount", 0);
        ratingView.setText("★ "+ currentCatering.getRating() + " (" + currentCatering.getReviewCount() + " reviews)");
//        int startingPrice = intent.getIntExtra("cateringPrice", 0);
        priceView.setText("Starting Price \n ₹" + currentCatering.getStartingPrice() + " per Plate");
//        servicesView.setText(intent.getStringExtra("services"));
//        String services = intent.getStringExtra("services");
        servicesView.setText("Services: "+ currentCatering.getServices());
        aboutText = currentCatering.getAbout();
        if (aboutText == null || aboutText.trim().isEmpty()) {
            aboutText = "No additional information available.";
        }



//       Wishlist logic
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = CATEGORY + "_" + currentCatering.getId();
        boolean isLiked = prefs.contains(key);
        updateWishlistIcon(isLiked);

        wishlistButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (!prefs.contains(key)) {
                String json = gson.toJson(currentCatering);
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

        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("Catering_photos");
        if (photoList != null && !photoList.isEmpty()) {
            CatererPhotosAdapter adapter = new CatererPhotosAdapter(this, photoList);
            cateringPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            cateringPhotosRecyclerView.setAdapter(adapter);
        }

        setupButtonListeners();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageViewCatering);
        nameView = findViewById(R.id.textServiceName);
        locationView = findViewById(R.id.textViewLocation);
        ratingView = findViewById(R.id.textViewRating);
        priceView = findViewById(R.id.textViewStartingPrice);
        servicesView = findViewById(R.id.detailServices);
        backButton = findViewById(R.id.backButton);
        wishlistButton = findViewById(R.id.likeButton);
        contactButton = findViewById(R.id.contactButton);
        aboutButton = findViewById(R.id.aboutButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        shareButton = findViewById(R.id.shareButton);
        cateringPhotosRecyclerView = findViewById(R.id.cateringPhotosRecyclerView);
    }
    private Catering buildCateringFromIntent(Intent intent) {
        String id = intent.getStringExtra("catererId");
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        double rating = intent.getDoubleExtra("rating",0.0);
        int reviews = intent.getIntExtra("reviewCount", 0);
        int imageRes = intent.getIntExtra("imageUrl", R.drawable.photographer);
        int price = intent.getIntExtra("cateringPrice", 0);
        String service = intent.getStringExtra("services");
        String about = intent.getStringExtra("about");
        ArrayList<Integer> photos = intent.getIntegerArrayListExtra("Catering_photos");

        return new Catering(id,name,location,rating,reviews,imageRes,price,service,about,photos);
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

//        aboutButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(this)
//                    .setTitle("About the Decorator")
//                    .setMessage("FLORA is a premium home function decorator in Kolkata, offering budget-friendly yet elegant setups for all types of events.")
//                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
//                    .show();
//        });

        aboutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("About the Caterer")
                    .setMessage(aboutText)
                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        shareButton.setOnClickListener(v -> {
            String shareText = "Check out this decorator on Plan & Pair:\n\n"
                    + "Name: " + nameView.getText() + "\n"
                    + "Location: " + locationView.getText() + "\n"
                    + "Rating: " + ratingView.getText() + "\n"
                    + "Starting Price: " + priceView.getText() + "\n\n"
                    + "Plan your dream wedding easily with Plan & Pair!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedding Caterer Recommendation");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CateringActivity.class));
            finish();
        });
    }
}