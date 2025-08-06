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
import com.example.planpair.MakeupartistDetailActivity;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.example.planpair.R;

import java.util.ArrayList;
import java.util.List;

public class MakeupAdapter extends RecyclerView.Adapter<MakeupAdapter.MakeupViewHolder> {

    private Context context;
    private List<MakeupArtist> mArtistList;

    public MakeupAdapter(Context context, List<MakeupArtist> mArtistList) {
        this.context = context;
        this.mArtistList = mArtistList;
    }

    @NonNull
    @Override
    public MakeupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_makeup, parent, false);
        return new MakeupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeupViewHolder holder, int position) {
        MakeupArtist mArtist = mArtistList.get(position);
        holder.name.setText(mArtist.getName());
        holder.location.setText("📍 "+ mArtist.getLocation());
        holder.imageView.setImageResource(mArtist.getImageUrl());
        holder.rating.setText("★ "+ mArtist.getRating() + " (" + mArtist.getReviewCount() + " reviews)");
        holder.startingPrice.setText("Price For Makeup \n"+"₹" + mArtist.getStartingPrice() + " Per Function");
//        holder.tag.setText(decoration.getTag());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MakeupartistDetailActivity.class);
            intent.putExtra("martistId", mArtist.getId());
            intent.putExtra("name",mArtist.getName());
            intent.putExtra("location",mArtist.getLocation());
            intent.putExtra("rating",mArtist.getRating());
            intent.putExtra("reviewCount",mArtist.getReviewCount());
            intent.putExtra("imageUrl",mArtist.getImageUrl());
            intent.putExtra("makeupPrice",mArtist.getStartingPrice());
            intent.putExtra("services",mArtist.getServices());
            intent.putExtra("about", mArtist.getAbout());
            intent.putIntegerArrayListExtra("Makeup_photos", new ArrayList<>(mArtist.getPhotoList()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }

    public static class MakeupViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, location, rating, startingPrice;

        public MakeupViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageMakeup);
            name = itemView.findViewById(R.id.artistName);
            location = itemView.findViewById(R.id.studioLocation);
            rating = itemView.findViewById(R.id.textRating);
            startingPrice = itemView.findViewById(R.id.textRate);
//            tag = itemView.findViewById(R.id.textTag);
        }
    }
    public void updateList(List<MakeupArtist> newList) {
        this.mArtistList = newList;
        notifyDataSetChanged();
    }
}

