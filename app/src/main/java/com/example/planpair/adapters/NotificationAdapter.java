package com.example.planpair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<NotificationModel> notifications;

    public NotificationAdapter(Context context, List<NotificationModel> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationModel model = notifications.get(position);

        holder.notificationMessage.setText(model.getMessage());
        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(model.getTimestamp());
        holder.notificationTimestamp.setText(relativeTime);

        Glide.with(context)
                .load(model.getSenderImageUrl())
                .placeholder(R.drawable.default_profile)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView notificationMessage, notificationTimestamp;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            notificationMessage = itemView.findViewById(R.id.notificationMessage);
            notificationTimestamp = itemView.findViewById(R.id.notificationTimestamp);
        }
    }
}
