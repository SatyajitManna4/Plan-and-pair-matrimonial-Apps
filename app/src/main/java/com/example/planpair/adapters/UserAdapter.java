
package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.planpair.BlurViewHelper;
import com.example.planpair.R;
import com.example.planpair.PaymentActivity;
import com.example.planpair.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private OnItemClickListener listener;
    private SharedPreferences preferences;
    private boolean isPremium;

    public UserAdapter(Context context, List<User> userList, boolean isPremium, OnItemClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
        this.isPremium = isPremium;
        this.preferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.profileImage.setImageResource(user.getprofileImageUrl());
        holder.userName.setText(user.getName());
        holder.userAge.setText("Age: " + user.getAge());
        holder.compatibilityScore.setText("Compatibility: " + user.getCompatibility() + "%");

        boolean isPremium = preferences.getBoolean("isPremium", false);

        if (position >= 5 && !isPremium) {
            // Lock premium content for non-premium users
            holder.lockOverlay.setVisibility(View.VISIBLE);
            holder.cardView.setAlpha(0.3f);
            holder.cardView.setClickable(false);
        } else {
            // Unlock content for first 5 cards or premium users
            holder.lockOverlay.setVisibility(View.GONE);
            holder.cardView.setAlpha(1f);
            holder.cardView.setClickable(true);
        }

        holder.cardView.setOnClickListener(v -> {
            if (position < 5 || isPremium) {
                listener.onItemClick(user);
            }
        });

        holder.likeButton.setOnClickListener(v -> {
            holder.isLiked = !holder.isLiked;
            holder.likeButton.setImageResource(holder.isLiked ?
                    R.drawable.baseline_favorite_24 :
                    R.drawable.baseline_favorite_border_24);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, userAge, compatibilityScore;
        CardView cardView;
        ImageButton likeButton;
        View lockOverlay;
        boolean isLiked = false;

        public UserViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            compatibilityScore = itemView.findViewById(R.id.compatibilityScore);
            cardView = itemView.findViewById(R.id.cardView);
            likeButton = itemView.findViewById(R.id.likeButton);
            lockOverlay = itemView.findViewById(R.id.lockOverlay);
        }
    }
}


//satya
/*
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final boolean isPremium;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public UserAdapter(Context context, List<User> userList, boolean isPremium, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.isPremium = isPremium;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameAgeText.setText(user.getName() + ", " + user.getAge());
        holder.compatibilityText.setText("Compatibility: " + user.getCompatibility() + "%");

        // Load image using Glide
        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(holder.profileImage);

        // Blur overlay visibility for non-premium users
        holder.lockOverlay.setVisibility(isPremium ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, lockOverlay;
        TextView nameAgeText, compatibilityText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            lockOverlay = itemView.findViewById(R.id.lockOverlay);
            nameAgeText = itemView.findViewById(R.id.nameAgeText);
            compatibilityText = itemView.findViewById(R.id.compatibilityText);
        }
    }
}
*/
