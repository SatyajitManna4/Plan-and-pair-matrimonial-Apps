package com.example.planpair.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.MessageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends FirestoreRecyclerAdapter<MessageModel, ChatAdapter.MessageViewHolder> {

    private final String currentUserId;
    private static final int MSG_TYPE_SENT = 1;
    private static final int MSG_TYPE_RECEIVED = 2;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<MessageModel> options, String currentUserId) {
        super(options);
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = getItem(position);
        if (message.getSenderId() != null && message.getSenderId().equals(currentUserId)) {
            return MSG_TYPE_SENT;
        } else {
            return MSG_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel message) {
        // Handle message type: text or image
        if ("text".equals(message.getType())) {
            holder.messageText.setVisibility(View.VISIBLE);
//            holder.messageImage.setVisibility(View.GONE);
            holder.messageText.setText(message.getMessage());
        } else {
            // For unsupported message types, show a fallback text
            holder.messageText.setVisibility(View.VISIBLE);
//            holder.messageImage.setVisibility(View.GONE);
            holder.messageText.setText("[Unsupported message type]");
        }

        // Format and set the timestamp if available
        long timestamp = message.getTimestamp();
        if (timestamp > 0) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            holder.timeText.setText(sdf.format(date));
        } else {
            holder.timeText.setText("");
        }
    }


    static class MessageViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView messageImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message);
            timeText = itemView.findViewById(R.id.text_time);
//            messageImage = itemView.findViewById(R.id.image_message);
        }
    }
}