//package com.example.planpair;
//
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
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.planpair.Adapter.PhotoAdapter;
//import com.example.planpair.Model.Vendor;
//import com.example.planpair.Utils.MyWishlistManager;
//
//import java.util.ArrayList;
//
//public class PhotographerDetailActivity extends AppCompatActivity {
//
//    private ImageView detailImage;
//    private TextView detailName, detailCity, detailRating, detailPrice,
//            detailServices, detailDelivery, detailAdvance, detailTravel,
//            detailExperience, detailFeedback;
//
//    private Button callButton, whatsappButton, shareButton, bookNowButton;
//    private ImageButton backButton, wishlistButton;
//    private RecyclerView galleryRecyclerView;
//
//    // Example phone number (replace with actual)
//    private final String phoneNumber = "+919876543210";
//
//    private static final String PREFS_NAME = "photographer_likes";
//    private static final String CATEGORY = "photographer";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photographer_detail);
//
//        // Initialize views
//        detailImage = findViewById(R.id.detailImage);
//        detailName = findViewById(R.id.detailName);
//        detailCity = findViewById(R.id.detailCity);
//        detailRating = findViewById(R.id.detailRating);
//        detailPrice = findViewById(R.id.detailPrice);
//        detailServices = findViewById(R.id.detailServices);
//        detailDelivery = findViewById(R.id.detailDelivery);
//        detailAdvance = findViewById(R.id.detailAdvance);
//        detailTravel = findViewById(R.id.detailTravel);
//        detailExperience = findViewById(R.id.detailExperience);
//        detailFeedback = findViewById(R.id.detailFeedback);
//
//        callButton = findViewById(R.id.callButton);
//        whatsappButton = findViewById(R.id.whatsappButton);
//        shareButton = findViewById(R.id.shareButton);
//        bookNowButton = findViewById(R.id.bookNowButton);
//        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
//        backButton = findViewById(R.id.backButton);
//        wishlistButton = findViewById(R.id.likeButton);
//
//        Intent intent = getIntent();
//        detailImage.setImageResource(intent.getIntExtra("imageUrl", R.drawable.photographer));
//        detailName.setText(intent.getStringExtra("name"));
//        detailCity.setText(intent.getStringExtra("city"));
//
//        double ratingValue = intent.getDoubleExtra("rating", 0.0);
//        int reviews = intent.getIntExtra("reviews", 0);
//        detailRating.setText("★ " + ratingValue + " (" + reviews + " reviews)");
//
//        int pPrice = intent.getIntExtra("pPrice", 0);
//        int pvPrice = intent.getIntExtra("pvPrice", 0);
//        detailPrice.setText("₹" + pPrice + " per day (₹" + pvPrice + " with video)");
//
//        detailServices.setText("Service: " + intent.getStringExtra("service"));
//        detailDelivery.setText("Delivery Time: " + intent.getStringExtra("deliveryTime"));
//        detailAdvance.setText("Advance Payment: " + intent.getStringExtra("advancePayment"));
//        detailTravel.setText("Travel Policy: " + intent.getStringExtra("travelPolicy"));
//        detailExperience.setText("Experience: " + intent.getStringExtra("experience"));
//        detailFeedback.setText("Client Feedback: " + intent.getStringExtra("feedback"));
//
//        // Wishlist logic
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        String tempPhotographerId = intent.getStringExtra("photographerId");
//        if (tempPhotographerId == null) tempPhotographerId = "default_photographer";
//        final String photographerId = tempPhotographerId;
//
//        final String key = CATEGORY + "_" + photographerId;
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
//
//                // Create Vendor object to add to wishlist manager
//                Vendor vendor = new Vendor(
//                        CATEGORY,
//                        photographerId,
//                        detailName.getText().toString(),
//                        detailCity.getText().toString(),
//                        ratingValue,
//                        reviews,
//                        pPrice,
//                        intent.getIntExtra("imageUrl", R.drawable.photographer),
//                        intent.getStringExtra("service"),
//                        "" // aboutText or description (if any)
//                );
////                MyWishlistManager.getInstance().addVendor(vendor);
//
//                Toast.makeText(this, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show();
//            } else {
//                updateWishlistIcon(false);
//                editor.remove(key);
////                MyWishlistManager.getInstance().removeVendor(key);
//
//                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
//            }
//            editor.apply();
//        });
//
//        // Setup gallery RecyclerView
//        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("photographer_photos");
//        if (photoList != null && !photoList.isEmpty()) {
//            PhotoAdapter adapter = new PhotoAdapter(this, photoList);
//            galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//            galleryRecyclerView.setAdapter(adapter);
//        }
//
//        // Set button click listeners
//        setupButtonListeners();
//    }
//
//    private void updateWishlistIcon(boolean isLiked) {
//        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
//        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
//    }
//
//    private void setupButtonListeners() {
//        callButton.setOnClickListener(v -> {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel:" + phoneNumber));
//            startActivity(callIntent);
//        });
//
//        whatsappButton.setOnClickListener(v -> {
//            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
//            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
//            whatsappIntent.setData(Uri.parse(url));
//            startActivity(whatsappIntent);
//        });
//
//        shareButton.setOnClickListener(v -> {
//            String shareText = "Check out this photographer on Plan & Pair:\n\n"
//                    + "Name: " + detailName.getText() + "\n"
//                    + "Location: " + detailCity.getText() + "\n"
//                    + "Rating: " + detailRating.getText() + "\n"
//                    + "Starting Price: " + detailPrice.getText() + "\n\n"
//                    + "Plan your dream wedding easily with Plan & Pair!";
//
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedding Photographer Recommendation");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//            startActivity(Intent.createChooser(shareIntent, "Share via"));
//        });
//
//        bookNowButton.setOnClickListener(v -> {
//            Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show();
//            // You can start a booking activity or open a booking dialog here
//        });
//
//        backButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, PhotographerActivity.class));
//            finish();
//        });
//    }
//}
package com.example.planpair;

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

import com.example.planpair.adapters.PhotoAdapter;
import com.example.planpair.models.Photographer;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PhotographerDetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailName, detailCity, detailRating, detailPrice,
            detailServices, detailDelivery, detailAdvance, detailTravel,
            detailExperience, detailFeedback;

    private Button callButton, whatsappButton, shareButton, bookNowButton;
    private ImageButton backButton, wishlistButton;
    private RecyclerView galleryRecyclerView;

    private static final String PREF_NAME = "wishlist_prefs";
    private static final String CATEGORY = "photographer";

    private Photographer currentPhotographer;
    private Gson gson = new Gson();
    private String phoneNumber = "+919876543210"; // default fallback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_detail);

        initViews();

        // ✅ Build Photographer from intent
        Intent intent = getIntent();
        currentPhotographer = buildPhotographerFromIntent(intent);

        if (currentPhotographer == null || currentPhotographer.getId() == null) {
            Toast.makeText(this, "Error loading photographer data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Populate data
        detailImage.setImageResource(currentPhotographer.getImageUrl());
        detailName.setText(currentPhotographer.getName());
        detailCity.setText(currentPhotographer.getCity());
        detailRating.setText("★ " + currentPhotographer.getRating() + " (" + currentPhotographer.getReviewCount() + " reviews)");
        detailPrice.setText("₹" + currentPhotographer.getpPrice() + " per day (₹" + currentPhotographer.getPvPrice() + " with video)");
        detailServices.setText("Service: " + currentPhotographer.getService());
        detailDelivery.setText("Delivery Time: " + currentPhotographer.getDeliveryTime());
        detailAdvance.setText("Advance Payment: " + currentPhotographer.getAdvancePayment());
        detailTravel.setText("Travel Policy: " + currentPhotographer.getTravelPolicy());
        detailExperience.setText("Experience: " + currentPhotographer.getExperience());
        detailFeedback.setText("Client Feedback: " + currentPhotographer.getClientFeedback());

        phoneNumber = intent.getStringExtra("phone");

        // ✅ Wishlist
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = CATEGORY + "_" + currentPhotographer.getId();
        boolean isLiked = prefs.contains(key);
        updateWishlistIcon(isLiked);

        wishlistButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (!prefs.contains(key)) {
                String json = gson.toJson(currentPhotographer);
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

        // ✅ Gallery
        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("photographer_photos");
        if (photoList != null && !photoList.isEmpty()) {
            PhotoAdapter adapter = new PhotoAdapter(this, photoList);
            galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            galleryRecyclerView.setAdapter(adapter);
        }

        setupButtonListeners();
    }

    private void initViews() {
        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailCity = findViewById(R.id.detailCity);
        detailRating = findViewById(R.id.detailRating);
        detailPrice = findViewById(R.id.detailPrice);
        detailServices = findViewById(R.id.detailServices);
        detailDelivery = findViewById(R.id.detailDelivery);
        detailAdvance = findViewById(R.id.detailAdvance);
        detailTravel = findViewById(R.id.detailTravel);
        detailExperience = findViewById(R.id.detailExperience);
        detailFeedback = findViewById(R.id.detailFeedback);

        callButton = findViewById(R.id.callButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        shareButton = findViewById(R.id.shareButton);
        bookNowButton = findViewById(R.id.bookNowButton);
        backButton = findViewById(R.id.backButton);
        wishlistButton = findViewById(R.id.likeButton);
        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
    }

    private Photographer buildPhotographerFromIntent(Intent intent) {
        String id = intent.getStringExtra("photographerId");
        String name = intent.getStringExtra("name");
        String city = intent.getStringExtra("city");
        int imageRes = intent.getIntExtra("imageUrl", R.drawable.photographer);
        double rating = intent.getDoubleExtra("rating", 0.0);
        int reviews = intent.getIntExtra("reviews", 0);
        int pPrice = intent.getIntExtra("pPrice", 0);
        int pvPrice = intent.getIntExtra("pvPrice", 0);
        String service = intent.getStringExtra("service");
        String deliveryTime = intent.getStringExtra("deliveryTime");
        String advancePayment = intent.getStringExtra("advancePayment");
        String travelPolicy = intent.getStringExtra("travelPolicy");
        String experience = intent.getStringExtra("experience");
        String feedback = intent.getStringExtra("feedback");
        ArrayList<Integer> photos = intent.getIntegerArrayListExtra("photographer_photos");

        return new Photographer(id, name, city, imageRes, service, rating, reviews, pPrice, pvPrice, deliveryTime, advancePayment, travelPolicy, experience, feedback, photos);
    }

    private void updateWishlistIcon(boolean isLiked) {
        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
    }

    private void setupButtonListeners() {
        callButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        whatsappButton.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setData(Uri.parse(url));
            startActivity(whatsappIntent);
        });

        shareButton.setOnClickListener(v -> {
            String shareText = "Check out this photographer on Plan & Pair:\n\n"
                    + "Name: " + detailName.getText() + "\n"
                    + "Location: " + detailCity.getText() + "\n"
                    + "Rating: " + detailRating.getText() + "\n"
                    + "Price: " + detailPrice.getText() + "\n\n"
                    + "Plan your dream wedding easily with Plan & Pair!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedding Photographer Recommendation");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        bookNowButton.setOnClickListener(v -> {
            Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> {
            finish(); // simply finish, no need to restart PhotographerActivity
        });
    }
}
