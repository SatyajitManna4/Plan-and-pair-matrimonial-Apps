package com.example.planpair;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.VendorAdapter;
import com.example.planpair.models.Catering;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.example.planpair.models.Photographer;
import com.example.planpair.models.Vendor;
import com.example.planpair.models.Venue;
import com.example.planpair.utils.MyWishlistManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private VendorAdapter adapter;
    private List<Vendor> wishlistVendors;
    private static final String PREFS_NAME = "wishlist_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.recyclerViewWishlist);
        emptyText = findViewById(R.id.textViewEmptyWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wishlistVendors = new ArrayList<>();
        loadWishlistFromPrefs();

        adapter = new VendorAdapter(this, wishlistVendors);
        recyclerView.setAdapter(adapter);

        emptyText.setVisibility(wishlistVendors.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void loadWishlistFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        Gson gson = new Gson();
        wishlistVendors.clear(); // clear old list

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = (String) entry.getValue();
            if (json != null) {
                try {
                    // Parse category from JSON manually
                    String category = gson.fromJson(json, Map.class).get("category").toString();

                    if ("venue".equalsIgnoreCase(category)) {
                        // If category is Venue, parse as Venue then convert to Vendor
                        Venue venue = gson.fromJson(json, Venue.class);
                        Vendor vendor = MyWishlistManager.convertVenueToVendor(venue);
                        wishlistVendors.add(vendor);
                    } else if ("photographer".equalsIgnoreCase(category)) {
                        Photographer photographer = gson.fromJson(json,Photographer.class);
                        Vendor vendor = MyWishlistManager.convertPhotographerToVendor(photographer);
                        wishlistVendors.add(vendor);
                    } else if ("decoration".equalsIgnoreCase(category)) {
                        Decoration decoration = gson.fromJson(json,Decoration.class);
                        Vendor vendor = MyWishlistManager.convertDecorationToVendor(decoration);
                        wishlistVendors.add(vendor);
                    }
                    else if ("makeupartist".equalsIgnoreCase(category)) {
                        MakeupArtist makeupArtist = gson.fromJson(json,MakeupArtist.class);
                        Vendor vendor = MyWishlistManager.convertMakeupArtistToVendor(makeupArtist);
                        wishlistVendors.add(vendor);
                    }
                    else if ("caterer".equalsIgnoreCase(category)) {
                        Catering catering = gson.fromJson(json,Catering.class);
                        Vendor vendor = MyWishlistManager.convertCateringToVendor(catering);
                        wishlistVendors.add(vendor);
                    }
                    else {
                        // For other categories (Photographer, Decor...), parse directly as Vendor
                        Vendor vendor = gson.fromJson(json, Vendor.class);
                        wishlistVendors.add(vendor);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // optionally remove corrupted entry
                }
            }
        }
    }

}
