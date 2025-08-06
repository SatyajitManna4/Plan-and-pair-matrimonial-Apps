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

import com.example.planpair.DecorationDetailActivity;
import com.example.planpair.models.Decoration;
import com.example.planpair.R;

import java.util.ArrayList;
import java.util.List;

public class DecorationAdapter extends RecyclerView.Adapter<DecorationAdapter.DecorationViewHolder> {

    private Context context;
    private List<Decoration> decorationList;

    public DecorationAdapter(Context context, List<Decoration> decorationList) {
        this.context = context;
        this.decorationList = decorationList;
    }

    @NonNull
    @Override
    public DecorationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_decoration, parent, false);
        return new DecorationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DecorationViewHolder holder, int position) {
        Decoration decoration = decorationList.get(position);
        holder.name.setText(decoration.getName());
        holder.location.setText("📍 "+ decoration.getLocation());
        holder.imageView.setImageResource(decoration.getImageUrl());
        holder.rating.setText("★ "+ decoration.getRating() + " (" + decoration.getReviewCount() + " reviews)");
        holder.startingPrice.setText("Starting Price For Decor \n"+"₹" + decoration.getStartingPrice() + " Onwards");
//        holder.tag.setText(decoration.getTag());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DecorationDetailActivity.class);
            intent.putExtra("decorId", decoration.getId());
            intent.putExtra("name",decoration.getName());
            intent.putExtra("location",decoration.getLocation());
            intent.putExtra("rating",decoration.getRating());
            intent.putExtra("reviewCount",decoration.getReviewCount());
            intent.putExtra("imageResId",decoration.getImageUrl());
            intent.putExtra("startingPrice",decoration.getStartingPrice());
            intent.putExtra("tag",decoration.getTag());
            intent.putExtra("services",decoration.getServices());
            intent.putExtra("about", decoration.getAbout());
            intent.putIntegerArrayListExtra("Decoration_photos", new ArrayList<>(decoration.getPhotoList()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return decorationList.size();
    }

    public static class DecorationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, location, rating, startingPrice;

        public DecorationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageDecoration);
            name = itemView.findViewById(R.id.textName);
            location = itemView.findViewById(R.id.textLocation);
            rating = itemView.findViewById(R.id.textRating);
            startingPrice = itemView.findViewById(R.id.textPrice);
//            tag = itemView.findViewById(R.id.textTag);
        }
    }
    public void updateList(List<Decoration> newList) {
        this.decorationList = newList;
        notifyDataSetChanged();
    }
}

