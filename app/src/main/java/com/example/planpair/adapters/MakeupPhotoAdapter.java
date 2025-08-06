package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.ImagePreviewActivity;
import com.example.planpair.R;

import java.util.List;

public class MakeupPhotoAdapter extends RecyclerView.Adapter<MakeupPhotoAdapter.MakeupPhotosViewHolder> {
    private Context context;
    private List<Integer> photoList;

    public MakeupPhotoAdapter(Context context, List<Integer> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public MakeupPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false);
        return new MakeupPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeupPhotosViewHolder holder, int position) {
        int imageResId = photoList.get(position);
        holder.imageView.setImageResource(imageResId);

        // Set click listener
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            intent.putExtra("imageResId", imageResId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    class MakeupPhotosViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MakeupPhotosViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
        }
    }
}

