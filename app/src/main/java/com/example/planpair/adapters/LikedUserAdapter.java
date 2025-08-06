package com.example.planpair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.User;

import java.util.List;

public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.UserViewHolder> {

    private final List<User> userList;

    public LikedUserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_liked, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        Context context = holder.itemView.getContext();

        holder.nameText.setText(user.getName());
        holder.ageText.setText("Age: " + user.getAge());
        holder.compatibilityText.setText("Compatibility: " + user.getCompatibility() + "%");

        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.profile_icon)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView nameText, ageText, compatibilityText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profileImageLiked);
            nameText = itemView.findViewById(R.id.userNameLiked);
            ageText = itemView.findViewById(R.id.userAgeLiked);
            compatibilityText = itemView.findViewById(R.id.compatibilityScoreLiked);
        }
    }
}
