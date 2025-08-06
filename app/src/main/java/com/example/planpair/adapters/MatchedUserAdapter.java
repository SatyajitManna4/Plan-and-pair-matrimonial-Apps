/*
package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.ChatActivity;
import com.example.planpair.models.MatchedUser;

import java.util.List;

public class MatchedUserAdapter extends RecyclerView.Adapter<MatchedUserAdapter.UserViewHolder> {

    private List<MatchedUser> userList;
    private Context context;

    public MatchedUserAdapter(List<MatchedUser> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        MatchedUser user = userList.get(position);
        holder.textView.setText(user.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("matchedUserId", user.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
*/
package com.example.planpair.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.ChatActivity;
import com.example.planpair.R;
import com.example.planpair.models.MatchedUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchedUserAdapter extends RecyclerView.Adapter<MatchedUserAdapter.ViewHolder> {

    private List<MatchedUser> matchedUsers;
    private Context context;

    public MatchedUserAdapter(List<MatchedUser> matchedUsers, Context context) {
        this.matchedUsers = matchedUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchedUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_matched_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchedUserAdapter.ViewHolder holder, int position) {
        MatchedUser user = matchedUsers.get(position);
        holder.nameTextView.setText(user.getName());

        if (user.getProfileImageUrl() != null) {
            Glide.with(context).load(user.getProfileImageUrl()).into(holder.profileImageView);
        } else {
            holder.profileImageView.setImageResource(R.drawable.default_profile); // fallback
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userId", MatchedUser.getUserId());
            intent.putExtra("userName", MatchedUser.getName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return matchedUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public CircleImageView profileImageView;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.matchedUserName);
            profileImageView = itemView.findViewById(R.id.matchedUserProfileImage);
        }
    }
}
