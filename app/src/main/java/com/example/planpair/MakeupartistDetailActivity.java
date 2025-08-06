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
//import com.example.planpair.Adapter.MakeupPhotoAdapter;
//import com.example.planpair.Model.Vendor;
//import com.example.planpair.Utils.MyWishlistManager;
//
//import java.util.ArrayList;
//
//public class MakeupartistDetailActivity extends AppCompatActivity {
//
//    private ImageView imageView;
//    private TextView nameView, locationView, ratingView, priceView, servicesView;
//    private ImageButton backButton, wishlistButton;
//    private Button contactButton, aboutButton, whatsappButton, mapButton;
//    private RecyclerView makeupPhotosRecyclerView;
//    private String aboutText = "";
//
//    private static final String PHONE_NUMBER = "+919876543210";
//    private static final String PREFS_NAME = "martist_likes";
//    private static final String DEFAULT_MAKEUP_ID = "default_makeup";
//    private static final String CATEGORY = "makeup";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_makeupartist_detail);
//
//        initViews();
//
//        Intent intent = getIntent();
//        // Set data
//        imageView.setImageResource(intent.getIntExtra("imageUrl", R.drawable.decor));
//        nameView.setText(intent.getStringExtra("name"));
//        locationView.setText(intent.getStringExtra("location"));
//        double rating = intent.getDoubleExtra("rating", 0.0);
//        int reviewCount = intent.getIntExtra("reviewCount", 0);
//        ratingView.setText("★ "+ rating + " (" + reviewCount + " reviews)");
//        int startingPrice = intent.getIntExtra("makeupPrice", 0);
//        priceView.setText("Price For Makeup \n ₹" + startingPrice + " per Function");
////        servicesView.setText(intent.getStringExtra("services"));
//        String services = intent.getStringExtra("services");
//        servicesView.setText("Services: "+ services);
//        aboutText = intent.getStringExtra("about");
//        if (aboutText == null || aboutText.trim().isEmpty()) {
//            aboutText = "No additional information available.";
//        }
//
//
//        // Like (wishlist) feature using SharedPreferences
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        String tempMakeupId = intent.getStringExtra("makeupId");
//        if (tempMakeupId == null) tempMakeupId = "default_martist";
//        final String makeupId = tempMakeupId;
//        final String key = CATEGORY + "_" + (makeupId != null ? makeupId : DEFAULT_MAKEUP_ID);
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
//                // Optional: Add vendor to Wishlist manager
//                Vendor vendor = new Vendor(
//                        CATEGORY,
//                        makeupId != null ? makeupId : DEFAULT_MAKEUP_ID,
//                        nameView.getText().toString(),
//                        locationView.getText().toString(),
//                        Double.parseDouble(ratingView.getText().toString().replaceAll("[^0-9.]", "")),
//                        reviewCount, // You can parse review count if needed
//                        Integer.parseInt(priceView.getText().toString().replaceAll("[^0-9]", "")),
//                        intent.getIntExtra("imageUrl", R.drawable.makup_artist),
//                        servicesView.getText().toString(),
//                        aboutText
//                );
////                MyWishlistManager.getInstance().addVendor(vendor);
//
//                Toast.makeText(this, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show();
//            } else {
//                updateWishlistIcon(false);
//                editor.remove(key);
////                MyWishlistManager.getInstance().removeVendor(key);
//                Toast.makeText(this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
//            }
//            editor.apply();
//        });
//
//        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("Makeup_photos");
//        if (photoList != null && !photoList.isEmpty()) {
//            MakeupPhotoAdapter adapter = new MakeupPhotoAdapter(this, photoList);
//            makeupPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//            makeupPhotosRecyclerView.setAdapter(adapter);
//        }
//
//        contactButton.setOnClickListener(v -> {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
//            startActivity(callIntent);
//        });
//
//        whatsappButton.setOnClickListener(v -> {
//            String url = "https://api.whatsapp.com/send?phone=" + PHONE_NUMBER;
//            Intent wintent = new Intent(Intent.ACTION_VIEW);
//            wintent.setData(Uri.parse(url));
//            startActivity(wintent);
//        });
//
////        aboutButton.setOnClickListener(v -> {
////            new AlertDialog.Builder(this)
////                    .setTitle("About the Decorator")
////                    .setMessage("FLORA is a premium home function decorator in Kolkata, offering budget-friendly yet elegant setups for all types of events.")
////                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
////                    .show();
////        });
//
//        aboutButton.setOnClickListener(v -> {
//            new AlertDialog.Builder(this)
//                    .setTitle("About the MakeupArtist")
//                    .setMessage(aboutText)
//                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
//                    .show();
//        });
//
//
//        mapButton.setOnClickListener(v -> {
//            String address = intent.getStringExtra("location");
//            if (address != null && !address.isEmpty()) {
//                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//
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
//        backButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, MakeupActivity.class));
//            finish();
//        });
//    }
//
//    private void initViews() {
//        imageView = findViewById(R.id.imageViewMakeup);
//        nameView = findViewById(R.id.textViewName);
//        locationView = findViewById(R.id.textViewLocation);
//        ratingView = findViewById(R.id.textViewRating);
//        priceView = findViewById(R.id.textViewStartingPrice);
//        servicesView = findViewById(R.id.detailServices);
//        backButton = findViewById(R.id.backButton);
//        wishlistButton = findViewById(R.id.likeButton);
//        contactButton = findViewById(R.id.contactButton);
//        aboutButton = findViewById(R.id.aboutButton);
//        whatsappButton = findViewById(R.id.whatsappButton);
//        mapButton = findViewById(R.id.studioLocationButton);
//        makeupPhotosRecyclerView = findViewById(R.id.makeupPhotosRecyclerView);
//    }
//
//    private void updateWishlistIcon(boolean isLiked) {
//        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
//        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
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

import com.example.planpair.adapters.MakeupPhotoAdapter;
import com.example.planpair.adapters.PhotoAdapter;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MakeupartistDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameView, locationView, ratingView, priceView, servicesView;
    private ImageButton backButton, wishlistButton;
    private Button contactButton, aboutButton, whatsappButton, mapButton;
    private RecyclerView makeupPhotosRecyclerView;
    private String aboutText = "";

    private static final String PHONE_NUMBER = "+919876543210";
    private static final String PREF_NAME = "wishlist_prefs";
    private static final String CATEGORY = "makeupartist";

    private MakeupArtist currentMakeupArtist;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeupartist_detail);

        initViews();

        Intent intent = getIntent();
        currentMakeupArtist = buildMakeupArtistFromIntent(intent);

        if (currentMakeupArtist == null || currentMakeupArtist.getId() == null) {
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set data
        imageView.setImageResource(currentMakeupArtist.getImageUrl());
        nameView.setText(currentMakeupArtist.getName());
        locationView.setText(currentMakeupArtist.getLocation());
//        double rating = intent.getDoubleExtra("rating", 0.0);
//        int reviewCount = intent.getIntExtra("reviewCount", 0);
        ratingView.setText("★ "+ currentMakeupArtist.getRating() + " (" + currentMakeupArtist.getReviewCount() + " reviews)");
//        int startingPrice = intent.getIntExtra("makeupPrice", 0);
        priceView.setText("Price For Makeup \n ₹" + currentMakeupArtist.getStartingPrice() + " per Function");
//        servicesView.setText(intent.getStringExtra("services"));
//        String services = intent.getStringExtra("services");
        servicesView.setText("Services: "+ currentMakeupArtist.getServices());
        aboutText = currentMakeupArtist.getAbout();
        if (aboutText == null || aboutText.trim().isEmpty()) {
            aboutText = "No additional information available.";
        }


//       Wishlist logic
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = CATEGORY + "_" + currentMakeupArtist.getId();
        boolean isLiked = prefs.contains(key);
        updateWishlistIcon(isLiked);

        wishlistButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (!prefs.contains(key)) {
                String json = gson.toJson(currentMakeupArtist);
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

        ArrayList<Integer> photoList = intent.getIntegerArrayListExtra("Makeup_photos");
        if (photoList != null && !photoList.isEmpty()) {
            MakeupPhotoAdapter adapter = new MakeupPhotoAdapter(this, photoList);
            makeupPhotosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            makeupPhotosRecyclerView.setAdapter(adapter);
        }

        contactButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
            startActivity(callIntent);
        });

        whatsappButton.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=" + PHONE_NUMBER;
            Intent wintent = new Intent(Intent.ACTION_VIEW);
            wintent.setData(Uri.parse(url));
            startActivity(wintent);
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
                    .setTitle("About the MakeupArtist")
                    .setMessage(aboutText)
                    .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        mapButton.setOnClickListener(v -> {
            String address = intent.getStringExtra("location");
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

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MakeupActivity.class));
            finish();
        });
    }

    private void initViews() {
        imageView = findViewById(R.id.imageViewMakeup);
        nameView = findViewById(R.id.textViewName);
        locationView = findViewById(R.id.textViewLocation);
        ratingView = findViewById(R.id.textViewRating);
        priceView = findViewById(R.id.textViewStartingPrice);
        servicesView = findViewById(R.id.detailServices);
        backButton = findViewById(R.id.backButton);
        wishlistButton = findViewById(R.id.likeButton);
        contactButton = findViewById(R.id.contactButton);
        aboutButton = findViewById(R.id.aboutButton);
        whatsappButton = findViewById(R.id.whatsappButton);
        mapButton = findViewById(R.id.studioLocationButton);
        makeupPhotosRecyclerView = findViewById(R.id.makeupPhotosRecyclerView);
    }

    private MakeupArtist buildMakeupArtistFromIntent(Intent intent) {
        String id = intent.getStringExtra("martistId");
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");
        double rating = intent.getDoubleExtra("rating",0.0);
        int reviews = intent.getIntExtra("reviewCount", 0);
        int imageRes = intent.getIntExtra("imageUrl", R.drawable.photographer);
        int price = intent.getIntExtra("makeupPrice", 0);
        String service = intent.getStringExtra("services");
        String about = intent.getStringExtra("about");
        ArrayList<Integer> photos = intent.getIntegerArrayListExtra("Makeup_photos");

        return new MakeupArtist(id,name,location,rating,reviews,imageRes,price,service,about,photos);
    }

    private void updateWishlistIcon(boolean isLiked) {
        int color = isLiked ? android.R.color.holo_red_dark : android.R.color.black;
        wishlistButton.setColorFilter(ContextCompat.getColor(this, color));
    }
}
