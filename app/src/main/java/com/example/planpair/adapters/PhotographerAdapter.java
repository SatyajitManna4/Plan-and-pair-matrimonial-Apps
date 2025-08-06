package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.planpair.models.Photographer;
import com.example.planpair.PhotographerDetailActivity;
import com.example.planpair.R;

import java.util.ArrayList;
import java.util.List;

public class PhotographerAdapter extends RecyclerView.Adapter<PhotographerAdapter.PhotographerViewHolder> {

    private Context context;
    private List<Photographer> photographerList;

    public PhotographerAdapter(Context context, List<Photographer> photographerList) {
        this.context = context;
        this.photographerList = photographerList;
    }

    @Override
    public PhotographerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photographer, parent, false);
        return new PhotographerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotographerViewHolder holder, int position) {
        Photographer photographer = photographerList.get(position);
        holder.image.setImageResource(photographer.getImageUrl());
        holder.name.setText(photographer.getName());
        holder.city.setText("📍 " + photographer.getCity());
        holder.rating.setText("★ " + photographer.getRating() + " (" + photographer.getReviewCount() + " reviews)");
        holder.price.setText("₹" + photographer.getPvPrice() + " per day");

        // If you are loading real images later, use Glide or Picasso here
        // For now, keeping default image from XML layout
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotographerDetailActivity.class);
            intent.putExtra("photographerId", photographer.getId());
            intent.putExtra("name", photographer.getName());
            intent.putExtra("city", photographer.getCity());
            intent.putExtra("imageUrl", photographer.getImageUrl());
            intent.putExtra("rating", photographer.getRating());
            intent.putExtra("reviews", photographer.getReviewCount());
            intent.putExtra("pPrice", photographer.getpPrice());
            intent.putExtra("pvPrice",photographer.getPvPrice());
            intent.putExtra("service", photographer.getService());
            intent.putExtra("deliveryTime",photographer.getDeliveryTime());
            intent.putExtra("advancePayment",photographer.getAdvancePayment());
            intent.putExtra("travelPolicy",photographer.getTravelPolicy());
            intent.putExtra("experience",photographer.getExperience());
            intent.putExtra("feedback",photographer.getClientFeedback());
            intent.putIntegerArrayListExtra("photographer_photos", new ArrayList<>(photographer.getPhotoList()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photographerList.size();
    }

    public static class PhotographerViewHolder extends RecyclerView.ViewHolder {
        TextView name, city, rating, price;
        ImageView image;

        public PhotographerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.photographerName);
            city = itemView.findViewById(R.id.photographerCity);
            rating = itemView.findViewById(R.id.photographerRating);
            price = itemView.findViewById(R.id.photographerPrice);
            image = itemView.findViewById(R.id.photographerImage);
        }
    }
}
