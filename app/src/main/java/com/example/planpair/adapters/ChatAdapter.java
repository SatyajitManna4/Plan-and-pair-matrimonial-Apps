/*
package com.example.planpair.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.ChatMessage;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<ChatMessage> messageList;
    private String currentUserId;
    private String matchedUserProfileUrl;

    public ChatAdapter(Context context, List<ChatMessage> messageList, String currentUserId, String matchedUserProfileUrl) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.matchedUserProfileUrl = matchedUserProfileUrl;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        boolean isSender = message.getSenderId().equals(currentUserId);

        if (isSender) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;

            if ("image".equals(message.getMessageType())) {
                sentHolder.messageText.setVisibility(View.GONE);
                sentHolder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getMessage()).into(sentHolder.messageImage);
            } else {
                sentHolder.messageText.setText(message.getMessage());
                sentHolder.messageText.setVisibility(View.VISIBLE);
                sentHolder.messageImage.setVisibility(View.GONE);
            }

            if (position == messageList.size() - 1) {
                sentHolder.statusIcon.setVisibility(View.VISIBLE);
                if (message.isSeen()) {
                    sentHolder.statusIcon.setImageResource(R.drawable.ic_seen); // Seen icon
                } else {
                    sentHolder.statusIcon.setImageResource(R.drawable.ic_delivered); // Delivered icon
                }

            } else {
                sentHolder.statusIcon.setVisibility(View.GONE);
            }

        } else {
            ReceivedMessageViewHolder recvHolder = (ReceivedMessageViewHolder) holder;

            if ("image".equals(message.getMessageType())) {
                recvHolder.messageText.setVisibility(View.GONE);
                recvHolder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getMessage()).into(recvHolder.messageImage);
            } else {
                recvHolder.messageText.setText(message.getMessage());
                recvHolder.messageText.setVisibility(View.VISIBLE);
                recvHolder.messageImage.setVisibility(View.GONE);
            }

            Glide.with(context).load(matchedUserProfileUrl).circleCrop().into(recvHolder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Sent ViewHolder
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView statusIcon;
        ImageView messageImage;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageImage = itemView.findViewById(R.id.messageImage);
            statusIcon = itemView.findViewById(R.id.statusIcon);
        }
    }

    // Received ViewHolder
    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        ImageView messageImage, profileImage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageImage = itemView.findViewById(R.id.messageImage);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}


*/

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
import com.example.planpair.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<ChatMessage> messageList;
    private String currentUserId;
    private String matchedUserProfileUrl;

    public ChatAdapter(Context context, List<ChatMessage> messageList, String currentUserId, String matchedUserProfileUrl) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.matchedUserProfileUrl = matchedUserProfileUrl;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        boolean isSender = message.getSenderId().equals(currentUserId);

        if (isSender) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;

            if ("image".equals(message.getMessageType())) {
                sentHolder.messageText.setVisibility(View.GONE);
                sentHolder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getMessage())
                        .placeholder(R.drawable.default_profile)
                        .into(sentHolder.messageImage);
            } else {
                sentHolder.messageText.setText(message.getMessage());
                sentHolder.messageText.setVisibility(View.VISIBLE);
                sentHolder.messageImage.setVisibility(View.GONE);
            }

            // Show status icon only for last message sent by current user
            if (position == messageList.size() - 1) {
                sentHolder.statusIcon.setVisibility(View.VISIBLE);
                if (message.isSeen()) {
                    sentHolder.statusIcon.setImageResource(R.drawable.ic_seen); // Seen icon
                } else {
                    sentHolder.statusIcon.setImageResource(R.drawable.ic_delivered); // Delivered icon
                }
            } else {
                sentHolder.statusIcon.setVisibility(View.GONE);
            }

            // Optional: display message timestamp
            sentHolder.timeText.setText(formatTimestamp(message.getTimestamp()));

        } else {
            ReceivedMessageViewHolder recvHolder = (ReceivedMessageViewHolder) holder;

            if ("image".equals(message.getMessageType())) {
                recvHolder.messageText.setVisibility(View.GONE);
                recvHolder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getMessage())
                        .placeholder(R.drawable.default_profile)
                        .into(recvHolder.messageImage);
            } else {
                recvHolder.messageText.setText(message.getMessage());
                recvHolder.messageText.setVisibility(View.VISIBLE);
                recvHolder.messageImage.setVisibility(View.GONE);
            }

            Glide.with(context)
                    .load(matchedUserProfileUrl)
                    .placeholder(R.drawable.default_profile)
                    .circleCrop()
                    .into(recvHolder.profileImage);

            // Optional: display message timestamp
            recvHolder.timeText.setText(formatTimestamp(message.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private String formatTimestamp(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(timestamp);
        } catch (Exception e) {
            return "";
        }
    }

    // Sent ViewHolder
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView statusIcon, messageImage;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageImage = itemView.findViewById(R.id.messageImage);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }

    // Received ViewHolder
    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView messageImage, profileImage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageImage = itemView.findViewById(R.id.messageImage);
            profileImage = itemView.findViewById(R.id.profileImage);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
*/
//s
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