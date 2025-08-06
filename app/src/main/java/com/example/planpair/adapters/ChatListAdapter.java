//package com.example.planpair.adapters;
//
//import android.content.Context;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.planpair.R;
//import com.example.planpair.models.User;
//
//import java.util.Date;
//import java.util.List;
//
//public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
//    private Context context;
//    private List<User> userList;
//    private OnUserClickListener listener;
//
//    public interface OnUserClickListener {
//        void onUserClick(User user);
//    }
//
//    public ChatListAdapter(Context context, List<User> userList, OnUserClickListener listener) {
//        this.context = context;
//        this.userList = userList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
//        return new ChatListViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
//        User user = userList.get(position);
//
//        // Set username
//        holder.userName.setText(user.getName());
//
//        // Handle last message display
//        String message = user.getLastMessage();
//        if (message == null || message.trim().isEmpty()) {
//            holder.lastMessage.setText("No message yet");  // ✅ Show default if no message
//            holder.messageTime.setText("");                // 🟡 No timestamp shown
//        } else {
//            // ✅ Set last message text (truncate if long)
//            holder.lastMessage.setText(message.length() > 40 ? message.substring(0, 40) + "..." : message);
//
//            // ✅ Convert timestamp to formatted time
//            long timestamp = user.getLastMessageTime();
//            if (timestamp > 0) {
//                String timeString = DateFormat.format("hh:mm a", new Date(timestamp)).toString();
//                holder.messageTime.setText(timeString);
//            } else {
//                holder.messageTime.setText("");  // hide if no timestamp
//            }
//        }
//
//        // Unread badge
//        if (user.getUnreadCount() > 0) {
//            holder.unreadBadge.setText(String.valueOf(user.getUnreadCount()));
//            holder.unreadBadge.setVisibility(View.VISIBLE);
//        } else {
//            holder.unreadBadge.setVisibility(View.GONE);
//        }
//
//        // Online status
//        if (user.isOnline()) {
//            holder.onlineDot.setVisibility(View.VISIBLE);
//        } else {
//            holder.onlineDot.setVisibility(View.GONE);
//        }
//
//        // Profile picture using Glide
//        Glide.with(context)
//                .load(user.getProfileImageUrl())
//                .placeholder(R.drawable.person_icon)
//                .into(holder.userImage);
//
//        // Handle item click
//        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    static class ChatListViewHolder extends RecyclerView.ViewHolder {
//        ImageView userImage;
//        View onlineDot;
//        TextView userName, lastMessage, messageTime, unreadBadge;
//
//        public ChatListViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userImage = itemView.findViewById(R.id.userImage);
//            onlineDot = itemView.findViewById(R.id.onlineDot);
//            userName = itemView.findViewById(R.id.userName);
//            lastMessage = itemView.findViewById(R.id.lastMessage);
//            messageTime = itemView.findViewById(R.id.messageTime);
//            unreadBadge = itemView.findViewById(R.id.unreadBadge);
//        }
//    }
//}
package com.example.planpair.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
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

import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private Context context;
    private List<User> userList;
    private OnUserClickListener listener;

    // Interface for click callback
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    // Constructor
    public ChatListAdapter(Context context, List<User> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        User user = userList.get(position);
        Log.d("ChatListAdapter", "Binding user: " + user.getName() +
                ", Last message: " + user.getLastMessage() +
                ", Last message time: " + user.getLastMessageTime() +
                ", Unread: " + user.getUnreadCount());

        holder.userName.setText(user.getName());

        // Last message
        String message = user.getLastMessage();
        if (message == null || message.trim().isEmpty()) {
            holder.lastMessage.setText("No message yet");
        } else {
            holder.lastMessage.setText(message);
        }

        long timestamp = user.getLastMessageTime();
        if (timestamp > 0) {
            String timeString = android.text.format.DateFormat.format("hh:mm a", new java.util.Date(timestamp)).toString();
            holder.messageTime.setText(timeString);
        } else {
            holder.messageTime.setText("");
        }

        // Unread count badge
        if (user.getUnreadCount() > 0) {
            holder.unreadBadge.setText(String.valueOf(user.getUnreadCount()));
            holder.unreadBadge.setVisibility(View.VISIBLE);
        } else {
            holder.unreadBadge.setVisibility(View.GONE);
        }

        // Online indicator
        holder.onlineDot.setVisibility(user.isOnline() ? View.VISIBLE : View.GONE);

        // Load profile image
        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.person_icon)
                .into(holder.userImage);

        // Click listener
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }


    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    // ViewHolder class
    static class ChatListViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        View onlineDot;
        TextView userName, lastMessage, messageTime, unreadBadge;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            onlineDot = itemView.findViewById(R.id.onlineDot);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            messageTime = itemView.findViewById(R.id.messageTime);
            unreadBadge = itemView.findViewById(R.id.unreadBadge);
        }
    }

    public void updateList(List<User> newList) {
        userList = newList;
        notifyDataSetChanged();
    }

}