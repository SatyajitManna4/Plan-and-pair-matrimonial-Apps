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

import com.example.planpair.CateringDetailActivity;
import com.example.planpair.DecorationDetailActivity;
import com.example.planpair.MakeupartistDetailActivity;
import com.example.planpair.models.Catering;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.example.planpair.R;

import java.util.ArrayList;
import java.util.List;

public class CateringAdapter extends RecyclerView.Adapter<CateringAdapter.CateringViewHolder> {

    private Context context;
    private List<Catering> cateringList;

    public CateringAdapter(Context context, List<Catering> cateringList) {
        this.context = context;
        this.cateringList = cateringList;
    }

    @NonNull
    @Override
    public CateringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_catering, parent, false);
        return new CateringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateringViewHolder holder, int position) {
        Catering catering = cateringList.get(position);
        holder.name.setText(catering.getName());
        holder.location.setText("📍 "+ catering.getLocation());
        holder.imageView.setImageResource(catering.getImageUrl());
        holder.rating.setText("★ "+ catering.getRating() + " (" + catering.getReviewCount() + " reviews)");
        holder.startingPrice.setText("Starting price \n"+"₹" + catering.getStartingPrice() + " per plate");
//        holder.tag.setText(decoration.getTag());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CateringDetailActivity.class);
            intent.putExtra("catererId", catering.getId());
            intent.putExtra("name",catering.getName());
            intent.putExtra("location",catering.getLocation());
            intent.putExtra("rating",catering.getRating());
            intent.putExtra("reviewCount",catering.getReviewCount());
            intent.putExtra("imageUrl",catering.getImageUrl());
            intent.putExtra("cateringPrice",catering.getStartingPrice());
            intent.putExtra("services",catering.getServices());
            intent.putExtra("about", catering.getAbout());
            intent.putIntegerArrayListExtra("Catering_photos", new ArrayList<>(catering.getPhotoList()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cateringList.size();
    }

    public static class CateringViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, location, rating, startingPrice;

        public CateringViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageCaterar);
            name = itemView.findViewById(R.id.catererServiceName);
            location = itemView.findViewById(R.id.cServiceLocation);
            rating = itemView.findViewById(R.id.textRating);
            startingPrice = itemView.findViewById(R.id.textRate);
//            tag = itemView.findViewById(R.id.textTag);
        }
    }
    public void updateList(List<Catering> newList) {
        this.cateringList = newList;
        notifyDataSetChanged();
    }
}

