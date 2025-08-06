//package com.example.planpair.Adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.planpair.MakeupartistDetailActivity;
//import com.example.planpair.Model.Vendor;
//import com.example.planpair.R;
//import com.example.planpair.CateringDetailActivity;
//import com.example.planpair.DecorationDetailActivity;
//import com.example.planpair.PhotographerDetailActivity;
//import com.example.planpair.Utils.MyWishlistManager;
//import com.example.planpair.VenueDetailActivity;
//
//import java.util.List;
//
//public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> {
//    private Context context;
//    private List<Vendor> vendorList;
//
//    public VendorAdapter(Context context, List<Vendor> vendorList) {
//        this.context = context;
//        this.vendorList = vendorList;
//    }
//
//    @Override
//    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_vendor, parent, false);
//        return new VendorViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(VendorViewHolder holder, int position) {
//        Vendor vendor = vendorList.get(position);
//        holder.name.setText(vendor.getName());
//        holder.location.setText(vendor.getLocation());
//        holder.rating.setText("★ " + vendor.getRating());
//        holder.price.setText("₹" + vendor.getStartingPrice());
//        holder.image.setImageResource(vendor.getImageResId());
////        if (vendor.getImageResId() != 0) {
////            Glide.with(context).load(vendor.getImageResId()).into(holder.image);
////        } else {
////            holder.image.setImageResource(R.drawable.image_placeholder); // fallback
////        }
//
//        holder.wishlist.setOnClickListener(v -> {
//            Vendor likedVendor = new Vendor(
//                    vendor.getCategory(),
//                    vendor.getId(),
//                    vendor.getName(),
//                    vendor.getLocation(),
//                    vendor.getRating(),
//                    vendor.getReviewCount(),
//                    vendor.getStartingPrice(),
//                    vendor.getImageResId(),
//                    vendor.getServices(),
//                    vendor.getAbout()
//            );
//            MyWishlistManager.addToWishlist(context, likedVendor);
//
//            Toast.makeText(context, "Added to wishlist!", Toast.LENGTH_SHORT).show();
//        });
//
//
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent;
//            String category = vendor.getCategory();
//
//            if (category == null) {
//                Toast.makeText(context, "Vendor category is missing", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            switch (vendor.getCategory()) {
//                case "caterer":
//                    intent = new Intent(context, CateringDetailActivity.class);
//                    intent.putExtra("catererId", vendor.getId());
//                    break;
//                case "photographer":
//                    intent = new Intent(context, PhotographerDetailActivity.class);
//                    intent.putExtra("photographerId", vendor.getId());
//                    break;
//                case "Venue":
//                    intent = new Intent(context, VenueDetailActivity.class);
//                    intent.putExtra("venueId", vendor.getId());
//                    break;
//                case "Decoration":
//                    intent = new Intent(context, DecorationDetailActivity.class);
//                    intent.putExtra("decorId", vendor.getId());
//                    break;
//                case "makeup":
//                    intent = new Intent(context, MakeupartistDetailActivity.class);
//                    intent.putExtra("makeupId", vendor.getId());
//                    break;
//                default:
//                    return;
//            }
//
//
//            // Common vendor data
//            intent.putExtra("name", vendor.getName());
//            intent.putExtra("location", vendor.getLocation());
//            intent.putExtra("imageResId", vendor.getImageResId());
//            intent.putExtra("rating", vendor.getRating());
//            intent.putExtra("reviewCount", vendor.getReviewCount());
//            intent.putExtra("vendorPrice", vendor.getStartingPrice());
//            intent.putExtra("services", vendor.getServices());
//            intent.putExtra("about", vendor.getAbout());
//
//            holder.itemView.getContext().startActivity(intent);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return vendorList.size();
//    }
//
//    public static class VendorViewHolder extends RecyclerView.ViewHolder {
//        TextView name, location, rating, price;
//        ImageView image;
//        ImageButton wishlist;
//
//        public VendorViewHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.textViewVendorName);
//            location = itemView.findViewById(R.id.textViewVendorLocation);
//            rating = itemView.findViewById(R.id.textViewVendorRating);
//            price = itemView.findViewById(R.id.textViewVendorPrice);
//            image = itemView.findViewById(R.id.imageViewVendor);
//            wishlist = itemView.findViewById(R.id.imageButtonWishlist);
//        }
//
//    }
//}
package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.CateringDetailActivity;
import com.example.planpair.DecorationDetailActivity;
import com.example.planpair.MakeupartistDetailActivity;
import com.example.planpair.models.Vendor;
import com.example.planpair.R;
import com.example.planpair.utils.MyWishlistManager;
import com.example.planpair.VenueDetailActivity;
import com.example.planpair.PhotographerDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> {

    private Context context;
    private List<Vendor> vendorList;

    public VendorAdapter(Context context, List<Vendor> vendorList) {
        this.context = context;
        this.vendorList = vendorList;
    }

    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vendor, parent, false);
        return new VendorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorViewHolder holder, int position) {
        Vendor vendor = vendorList.get(position);

        holder.name.setText(vendor.getName() != null ? vendor.getName() : "N/A");
        holder.location.setText(vendor.getLocation() != null ? vendor.getLocation() : "Unknown");
        holder.rating.setText("★ " + vendor.getRating());
        holder.price.setText("₹" + vendor.getStartingPrice());
        holder.category.setText(vendor.getCategory() != null ? vendor.getCategory() : "N/A");


        if (vendor.getImageResId() != 0) {
            Glide.with(context)
                    .load(vendor.getImageResId())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.image_placeholder)
                    .into(holder.image);
        }



        holder.wishlist.setOnClickListener(v -> {
            if (!MyWishlistManager.isInWishlist(context, vendor)) {
                MyWishlistManager.addToWishlist(context, vendor);
                Toast.makeText(context, "Added to wishlist!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Already in wishlist", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String category = vendor.getCategory() != null ? vendor.getCategory().trim().toLowerCase() : "";

            switch (category) {
                case "venue":
                    intent = new Intent(context, VenueDetailActivity.class);
                    intent.putExtra("venueId", vendor.getId());
                    intent.putExtra("imageResId", vendor.getImageResId());
                    intent.putExtra("name", vendor.getName());
                    intent.putExtra("location", vendor.getLocation());
                    intent.putExtra("price", vendor.getStartingPrice());
                    intent.putExtra("guest", vendor.getGuestCount());
                    intent.putExtra("room", vendor.getRoomCount());
                    intent.putExtra("rating", vendor.getRating());
                    intent.putExtra("venueType", vendor.getType());
                    intent.putExtra("phone", "9876543210");
                    if (vendor.getPhotos() != null)
                        intent.putIntegerArrayListExtra("venue_photos", new ArrayList<>(vendor.getPhotos()));
                    break;

                case "photographer":
                    intent = new Intent(context, PhotographerDetailActivity.class);
                    intent.putExtra("photographerId", vendor.getId());
                    intent.putExtra("imageUrl", vendor.getImageResId());
                    intent.putExtra("name", vendor.getName());
                    intent.putExtra("city", vendor.getLocation());
                    intent.putExtra("rating", vendor.getRating());
                    intent.putExtra("reviews", vendor.getReviewCount());
                    intent.putExtra("pPrice", vendor.getStartingPrice());
                    intent.putExtra("pvPrice", vendor.getStartingPrice() + 5000); // optional logic
                    intent.putExtra("service", vendor.getServices());
                    intent.putExtra("deliveryTime", "15 days"); // Placeholder or vendor.getDeliveryTime()
                    intent.putExtra("advancePayment", "50%");   // Placeholder or vendor.getAdvancePayment()
                    intent.putExtra("travelPolicy", "Travel & Stay paid by client"); // Placeholder
                    intent.putExtra("experience", "5+ years");  // Placeholder
                    intent.putExtra("feedback", vendor.getAbout());

                    if (vendor.getPhotos() != null)
                        intent.putIntegerArrayListExtra("photographer_photos", new ArrayList<>(vendor.getPhotos()));
                    break;

                case "caterer":
                    intent = new Intent(context, CateringDetailActivity.class);
                    intent.putExtra("catererId", vendor.getId());
                    intent.putExtra("name",vendor.getName());
                    intent.putExtra("location",vendor.getLocation());
                    intent.putExtra("rating",vendor.getRating());
                    intent.putExtra("reviewCount",vendor.getReviewCount());
                    intent.putExtra("imageUrl",vendor.getImageResId());
                    intent.putExtra("cateringPrice",vendor.getStartingPrice());
                    intent.putExtra("services",vendor.getServices());
                    intent.putExtra("about", vendor.getAbout());
                    if (vendor.getPhotos() != null)
                        intent.putIntegerArrayListExtra("Catering_photos", new ArrayList<>(vendor.getPhotos()));
                    break;

                case "decoration":
                    intent = new Intent(context, DecorationDetailActivity.class);
                    intent.putExtra("decorId", vendor.getId());
                    intent.putExtra("name",vendor.getName());
                    intent.putExtra("location",vendor.getLocation());
                    intent.putExtra("rating",vendor.getRating());
                    intent.putExtra("reviewCount",vendor.getReviewCount());
                    intent.putExtra("imageResId",vendor.getImageResId());
                    intent.putExtra("startingPrice",vendor.getStartingPrice());
                    intent.putExtra("tag","");
                    intent.putExtra("services",vendor.getServices());
                    intent.putExtra("about", vendor.getAbout());

                    if (vendor.getPhotos() != null)
                        intent.putIntegerArrayListExtra("Decoration_photos", new ArrayList<>(vendor.getPhotos()));
                    break;

                case "makeupartist":
                    intent = new Intent(context, MakeupartistDetailActivity.class);
                    intent.putExtra("martistId", vendor.getId());
                    intent.putExtra("name",vendor.getName());
                    intent.putExtra("location",vendor.getLocation());
                    intent.putExtra("rating",vendor.getRating());
                    intent.putExtra("reviewCount",vendor.getReviewCount());
                    intent.putExtra("imageUrl",vendor.getImageResId());
                    intent.putExtra("makeupPrice",vendor.getStartingPrice());
                    intent.putExtra("services",vendor.getServices());
                    intent.putExtra("about", vendor.getAbout());
                    if (vendor.getPhotos() != null)
                        intent.putIntegerArrayListExtra("Makeup_photos", new ArrayList<>(vendor.getPhotos()));
                    break;

                default:
                    Toast.makeText(context, "Unknown vendor category", Toast.LENGTH_SHORT).show();
                    return;
            }

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, rating, price,category;
        ImageView image;
        ImageButton wishlist;

        public VendorViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewVendorName);
            location = itemView.findViewById(R.id.textViewVendorLocation);
            rating = itemView.findViewById(R.id.textViewVendorRating);
            price = itemView.findViewById(R.id.textViewVendorPrice);
            image = itemView.findViewById(R.id.imageViewVendor);
            wishlist = itemView.findViewById(R.id.imageButtonWishlist);
            category = itemView.findViewById(R.id.textViewVendorType);
        }
    }
}
