package com.example.planpair.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.planpair.models.Catering;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.example.planpair.models.Photographer;
import com.example.planpair.models.Vendor;
import com.example.planpair.models.Venue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWishlistManager {

    private static final String PREF_NAME = "wishlist_pref";

    // ✅ Add to Wishlist
    public static void addToWishlist(Context context, Vendor vendor) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String key = vendor.getCategory() + "_" + vendor.getId();
        String json = new Gson().toJson(vendor);
        editor.putString(key, json);
        editor.apply();
    }

    // ✅ Remove a single item from Wishlist using its key
    public static void removeFromWishlist(Context context, Vendor vendor) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String key = vendor.getCategory() + "_" + vendor.getId();
        editor.remove(key);
        editor.apply();
    }

    // ✅ Retrieve all wishlist items
    public static List<Vendor> getWishlist(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        List<Vendor> list = new ArrayList<>();
        Gson gson = new Gson();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                try {
                    Vendor vendor = gson.fromJson((String) value, Vendor.class);
                    if (vendor != null) {
                        list.add(vendor);
                    }
                } catch (Exception e) {
                    // Skip malformed entries
                }
            }
        }
        return list;
    }

    // ✅ Clear existing and save all new wishlist items (use with caution)
    public static void saveWishlist(Context context, List<Vendor> wishlist) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Clears all previous wishlist entries

        Gson gson = new Gson();
        for (Vendor vendor : wishlist) {
            String key = vendor.getCategory() + "_" + vendor.getId();
            editor.putString(key, gson.toJson(vendor));
        }

        editor.apply();
    }

    // ✅ Optional: Check if a specific vendor is in wishlist
    public static boolean isInWishlist(Context context, Vendor vendor) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = vendor.getCategory() + "_" + vendor.getId();
        return prefs.contains(key);
    }

    // ✅ Convert Venue object to Vendor object for wishlist purposes
    public static Vendor convertVenueToVendor(Venue venue) {
        Vendor vendor = new Vendor(
                "venue",
                venue.getId(),
                venue.getName(),
                venue.getLocation(),
                venue.getRating(),
                0, // reviewCount not in Venue model
                venue.getPrice(),
                venue.getImageRes(),
                venue.getType(),
                venue.getGuestCount(),
                venue.getRoomCount(),
                "", // services not in Venue
                ""  // about not in Venue
        );
        vendor.setPhotos(new ArrayList<>(venue.getPhotoList()));
        return vendor;
    }
    public static Vendor convertPhotographerToVendor(Photographer photographer) {
        Vendor vendor = new Vendor(
                "photographer",
                photographer.getId(),
                photographer.getName(),
                photographer.getCity(),
                photographer.getRating(),
                photographer.getReviewCount(),
                photographer.getpPrice(),
                photographer.getImageUrl(),
                "",  // type not applicable
                0,   // guestCount not applicable
                0,   // roomCount not applicable
                photographer.getService(),
                photographer.getClientFeedback()
        );
        vendor.setPhotos(new ArrayList<>(photographer.getPhotoList()));
        return vendor;
    }
    public static Vendor convertDecorationToVendor(Decoration decoration) {
        Vendor vendor = new Vendor(
                "decoration",
                decoration.getId(),
                decoration.getName(),
                decoration.getLocation(),
                decoration.getRating(),
                decoration.getReviewCount(),
                decoration.getStartingPrice(),
                decoration.getImageUrl(),
                "",  // type not applicable
                0,   // guestCount not applicable
                0,   // roomCount not applicable
                decoration.getServices(),
                decoration.getAbout()
        );
        vendor.setPhotos(new ArrayList<>(decoration.getPhotoList()));
        return vendor;
    }
    public static Vendor convertMakeupArtistToVendor(MakeupArtist makeupArtist) {
        Vendor vendor = new Vendor(
                "makeupartist",
                makeupArtist.getId(),
                makeupArtist.getName(),
                makeupArtist.getLocation(),
                makeupArtist.getRating(),
                makeupArtist.getReviewCount(),
                makeupArtist.getStartingPrice(),
                makeupArtist.getImageUrl(),
                "",  // type not applicable
                0,   // guestCount not applicable
                0,   // roomCount not applicable
                makeupArtist.getServices(),
                makeupArtist.getAbout()
        );
        vendor.setPhotos(new ArrayList<>(makeupArtist.getPhotoList()));
        return vendor;
    }
    public static Vendor convertCateringToVendor(Catering catering) {
        Vendor vendor = new Vendor(
                "caterer",
                catering.getId(),
                catering.getName(),
                catering.getLocation(),
                catering.getRating(),
                catering.getReviewCount(),
                catering.getStartingPrice(),
                catering.getImageUrl(),
                "",  // type not applicable
                0,   // guestCount not applicable
                0,   // roomCount not applicable
                catering.getServices(),
                catering.getAbout()
        );
        vendor.setPhotos(new ArrayList<>(catering.getPhotoList()));
        return vendor;
    }
}
