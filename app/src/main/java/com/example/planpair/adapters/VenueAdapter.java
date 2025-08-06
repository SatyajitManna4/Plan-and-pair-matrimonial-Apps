package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.models.Venue;
import com.example.planpair.R;
import com.example.planpair.VenueDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
    private List<Venue> venueList;

    public VenueAdapter(List<Venue> venueList) {
        this.venueList = venueList;
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item, parent, false);
        return new VenueViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        Venue venue = venueList.get(position);
        holder.venueImage.setImageResource(venue.getImageRes());
        holder.venueName.setText(venue.getName());
        holder.venueLocation.setText(venue.getLocation());
        String priceText = "₹" + venue.getPrice();  // or format price properly
        holder.venuePrice.setText(priceText);
        holder.venueGuest.setText(venue.getGuestCount() + " guest");
        holder.venueRoom.setText(venue.getRoomCount() + " room");


        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, VenueDetailActivity.class);
            intent.putExtra("venueId", venue.getId());
            intent.putExtra("imageResId", venue.getImageRes());
            intent.putExtra("name", venue.getName());
            intent.putExtra("location", venue.getLocation());
            intent.putExtra("price", venue.getPrice());
            intent.putExtra("guest", venue.getGuestCount());
            intent.putExtra("room", venue.getRoomCount());
            intent.putExtra("rating",venue.getRating());
            intent.putExtra("venueType",venue.getType());
            intent.putExtra("phone", "9876543210");
            intent.putIntegerArrayListExtra("venue_photos", new ArrayList<>(venue.getPhotoList()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    static class VenueViewHolder extends RecyclerView.ViewHolder {
        ImageView venueImage;
        TextView venueName, venueLocation, venuePrice, venueGuest, venueRoom;

        VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            venueImage = itemView.findViewById(R.id.venueImage);
            venueName = itemView.findViewById(R.id.venueName);
            venueLocation = itemView.findViewById(R.id.venueLocation);
            venuePrice = itemView.findViewById(R.id.venuePrice);
            venueGuest = itemView.findViewById(R.id.venueGuest);
            venueRoom = itemView.findViewById(R.id.venueRoom);


        }
    }

}

