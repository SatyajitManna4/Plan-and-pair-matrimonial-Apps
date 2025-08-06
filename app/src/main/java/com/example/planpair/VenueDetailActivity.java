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
//import com.example.planpair.Adapter.PhotoAdapter;
//import com.example.planpair.Model.Vendor;
//import com.example.planpair.Model.Venue;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//
//public class VenueDetailActivity extends AppCompatActivity {
//
//    private ImageView venueImage;
//    private TextView name, location, price, guest, room, rating, venueType;
//    private Button contactManagerBtn, showLocationBtn;
//    private RecyclerView photoRecyclerView;
//    private ImageButton backButton, wishlistButton;
//
//    private static final String PREF_NAME = "wishlist_prefs";
//    private static final String CATEGORY = "Venue";
//
//    private Venue currentVenue;
//    private Gson gson = new Gson();
//
//    private String phoneNumber = "+919876543210"; // You can customize per vendor if needed
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_venue_detail);
//
//        String vendorId = getIntent().getStringExtra("venueId");
//
//        initViews();
//
//        Intent intent = getIntent();
//        currentVenue = buildVenueFromIntent(intent);
//
//        if (currentVenue == null) {
//            Toast.makeText(this, "Error loading venue data", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Bind data to views
////        venueImage.setImageResource(currentVenue.getImageRes());
////        name.setText(currentVenue.getName());
////        location.setText(currentVenue.getLocation());
////        price.setText("₹" + currentVenue.getPrice());
////        guest.setText(currentVenue.getGuestCount() + " guest");
////        room.setText(currentVenue.getRoomCount() + " room");
////        rating.setText("⭐ " + currentVenue.getRating());
////        venueType.setText(currentVenue.getType());
//        venueImage.setImageResource(intent.getIntExtra("imageResId", R.drawable.venue));
//        name.setText(intent.getStringExtra("name"));
//        location.setText(intent.getStringExtra("location"));
//        int vprice = getIntent().getIntExtra("price", 0);
//        price.setText("₹" + vprice);
//        guest.setText(intent.getIntExtra("guest", 0) + " guest");
//        room.setText(intent.getIntExtra("room", 0) + " room");
//        rating.setText(intent.getStringExtra("rating"));
//        venueType.setText(intent.getStringExtra("venueType"));
//        phoneNumber = intent.getStringExtra("phone");
//
//
//        // Wishlist logic
//        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String key = CATEGORY + "_" + currentVenue.getId();
//        boolean isLiked = prefs.contains(key);
//        updateWishlistIcon(isLiked);
//
//        wishlistButton.setOnClickListener(v -> {
//            SharedPreferences.Editor editor = prefs.edit();
//            if (!prefs.contains(key)) {
//                String json = gson.toJson(currentVenue);
//                editor.putString(key, json);
//                editor.apply();
//                updateWishlistIcon(true);
//                Toast.makeText(this, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show();
//            } else {
//                editor.remove(key);
//                editor.apply();
//                updateWishlistIcon(false);
//                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        backButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, VenueActivity.class));
//            finish();
//        });
//
//        contactManagerBtn.setOnClickListener(v -> {
//            if (phoneNumber != null && !phoneNumber.isEmpty()) {
//                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
//                startActivity(dialIntent);
//            } else {
//                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        showLocationBtn.setOnClickListener(v -> {
//            String address = currentVenue.getLocation();
//            if (address != null && !address.isEmpty()) {
//                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(mapIntent);
//                } else {
//                    Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Setup photo RecyclerView
//        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("venue_photos");
//        if (photoList != null && !photoList.isEmpty()) {
//            PhotoAdapter adapter = new PhotoAdapter(this, photoList);
//            photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//            photoRecyclerView.setAdapter(adapter);
//        }
//    }
//
//    private void initViews() {
//        venueImage = findViewById(R.id.venueImage);
//        name = findViewById(R.id.venueName);
//        location = findViewById(R.id.venueLocation);
//        price = findViewById(R.id.venuePrice);
//        guest = findViewById(R.id.venueGuest);
//        room = findViewById(R.id.venueRoom);
//        rating = findViewById(R.id.rating);
//        venueType = findViewById(R.id.venueType);
//        contactManagerBtn = findViewById(R.id.contactManagerBtn);
//        showLocationBtn = findViewById(R.id.map);
//        photoRecyclerView = findViewById(R.id.photoRecyclerView);
//        backButton = findViewById(R.id.backButton);
//        wishlistButton = findViewById(R.id.likeButton);
//    }
//
//    private Venue buildVenueFromIntent(Intent intent) {
//        Venue venue = new Venue();
//
//        venue.setId(intent.getStringExtra("venueId"));
//        venue.setCategory(CATEGORY);
//        venue.setName(intent.getStringExtra("name"));
//        venue.setLocation(intent.getStringExtra("location"));
//        venue.setRating(intent.getDoubleExtra("rating",0.0));
//        venue.setPrice(intent.getIntExtra("price",0));
//        venue.setType(intent.getStringExtra("venueType"));
//        venue.setGuestCount(intent.getIntExtra("guest", 0));
//        venue.setRoomCount(intent.getIntExtra("room", 0));
//        venue.setImageRes(intent.getIntExtra("imageResId", R.drawable.venue));
//
//        return venue;
//    }
//
//    private void updateWishlistIcon(boolean isLiked) {
//        int colorRes = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
//        wishlistButton.setColorFilter(ContextCompat.getColor(this, colorRes));
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
import com.example.planpair.models.Vendor;
import com.example.planpair.models.Venue;
import com.google.gson.Gson;

import java.util.ArrayList;

public class VenueDetailActivity extends AppCompatActivity {

    private ImageView venueImage;
    private TextView name, location, price, guest, room, rating, venueType;
    private Button contactManagerBtn, showLocationBtn;
    private RecyclerView photoRecyclerView;
    private ImageButton backButton, wishlistButton;

    private static final String PREF_NAME = "wishlist_prefs";
    private static final String CATEGORY = "venue";

    private Venue currentVenue;
    private Gson gson = new Gson();

    private String phoneNumber = "+919876543210"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        initViews();

        Intent intent = getIntent();
        currentVenue = buildVenueFromIntent(intent); // build Venue from intent extras

        if (currentVenue == null || currentVenue.getId() == null) {
            Toast.makeText(this, "Error loading venue data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind data to views
        venueImage.setImageResource(currentVenue.getImageRes());
        name.setText(currentVenue.getName());
        location.setText(currentVenue.getLocation());
        price.setText("₹" + currentVenue.getPrice());
        guest.setText(currentVenue.getGuestCount() + " guest");
        room.setText(currentVenue.getRoomCount() + " room");
        rating.setText("⭐ " + currentVenue.getRating());
        venueType.setText(currentVenue.getType());

        phoneNumber = intent.getStringExtra("phone");

        // Wishlist logic
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = CATEGORY + "_" + currentVenue.getId();
        boolean isLiked = prefs.contains(key);
        updateWishlistIcon(isLiked);

        wishlistButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (!prefs.contains(key)) {
                String json = gson.toJson(currentVenue);
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

        backButton.setOnClickListener(v -> finish());

        contactManagerBtn.setOnClickListener(v -> {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(dialIntent);
            } else {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
            }
        });

        showLocationBtn.setOnClickListener(v -> {
            String address = currentVenue.getLocation();
            if (address != null && !address.isEmpty()) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Address not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup photo grid
        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("venue_photos");
        if (photoList != null && !photoList.isEmpty()) {
            PhotoAdapter adapter = new PhotoAdapter(this, photoList);
            photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            photoRecyclerView.setAdapter(adapter);
        }
    }



    private void initViews() {
        venueImage = findViewById(R.id.venueImage);
        name = findViewById(R.id.venueName);
        location = findViewById(R.id.venueLocation);
        price = findViewById(R.id.venuePrice);
        guest = findViewById(R.id.venueGuest);
        room = findViewById(R.id.venueRoom);
        rating = findViewById(R.id.rating);
        venueType = findViewById(R.id.venueType);
        contactManagerBtn = findViewById(R.id.contactManagerBtn);
        showLocationBtn = findViewById(R.id.map);
        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        backButton = findViewById(R.id.backButton);
        wishlistButton = findViewById(R.id.likeButton);
    }

    private Venue buildVenueFromIntent(Intent intent) {
        String id = intent.getStringExtra("venueId");
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        int price = intent.getIntExtra("price", 0);
        int guest = intent.getIntExtra("guest", 0);
        int room = intent.getIntExtra("room", 0);
        double rating = intent.getDoubleExtra("rating", 0.0);
        String type = intent.getStringExtra("venueType");
        int imageResId = intent.getIntExtra("imageResId", R.drawable.image_placeholder); // fallback
        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("venue_photos");

        return new Venue(id, imageResId, name, location, price, guest, room, rating, type, 0, 0, photoList);
    }



    private double parseRating(String ratingString) {
        try {
            return Double.parseDouble(ratingString.replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void updateWishlistIcon(boolean isLiked) {
        int colorRes = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
        wishlistButton.setColorFilter(ContextCompat.getColor(this, colorRes));
    }
}
