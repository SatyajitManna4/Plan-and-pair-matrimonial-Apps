/*
package com.example.planpair.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.LikedYouActivity;
import com.example.planpair.R;
import com.example.planpair.models.NotificationModel;
import com.example.planpair.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private boolean isPremium;
    private OnUserClickListener listener;
    private FirebaseFirestore db;
    private String currentUid;

    public UserAdapter(LikedYouActivity context, List<User> likedUsers) {

    }

    public interface OnUserClickListener {
        void onItemClick(User user);
    }

    public UserAdapter(Context context, List<User> userList, boolean isPremium, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.isPremium = isPremium;
        this.listener = listener;

        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
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

        holder.userName.setText(user.getName());
        holder.userAge.setText("Age: " + user.getAge());
        holder.compatibilityScore.setText("Compatibility: " + user.getCompatibility() + "%");

        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.profile_icon)
                .into(holder.profileImage);

        holder.likeButton.setImageResource(user.isLiked()
                ? R.drawable.baseline_favorite_24
                : R.drawable.baseline_favorite_border_24);

        if (position >= 5 && !isPremium) {
            holder.lockOverlay.setVisibility(View.VISIBLE);
            holder.cardView.setAlpha(0.3f);
            holder.cardView.setClickable(false);
        } else {
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
            boolean newStatus = !user.isLiked();
            user.setLiked(newStatus);

            holder.likeButton.setImageResource(newStatus
                    ? R.drawable.baseline_favorite_24
                    : R.drawable.baseline_favorite_border_24);

            db.collection("UsersData").document(user.getUid())
                    .update("isLiked", newStatus)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Like", "Updated like status for " + user.getName());

                        if (newStatus) {
                            sendLikeNotification(user);
                        } else {
                            removeLikeNotification(user.getUid());
                        }

                        Toast.makeText(context, newStatus ? "Liked" : "Unliked", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Like", "Failed to update like status", e);
                        Toast.makeText(context, "Failed to update like", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void sendLikeNotification(User likedUser) {
        if (currentUid == null) return;

        db.collection("UsersData").document(currentUid).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;

                    String senderName = snapshot.getString("username");
                    String senderImageUrl = snapshot.getString("profileImageUrl");

                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("Likes").document(currentUid).get()
                            .addOnSuccessListener(receiverDoc -> {

                                db.collection("UsersData").document(currentUid)
                                        .collection("Likes").document(likedUser.getUid())
                                        .set(new HashMap<>())
                                        .addOnSuccessListener(unused -> {

                                            boolean isMutualLike = receiverDoc.exists();
                                            String message = isMutualLike
                                                    ? "It's a Match! You and " + senderName + " liked each other!"
                                                    : senderName + " liked your profile!";

                                            NotificationModel notification = new NotificationModel(
                                                    currentUid,
                                                    senderName,
                                                    senderImageUrl,
                                                    message,
                                                    System.currentTimeMillis()
                                            );

                                            db.collection("UsersData").document(likedUser.getUid())
                                                    .collection("Notifications")
                                                    .document(currentUid)
                                                    .set(notification)
                                                    .addOnSuccessListener(aVoid ->
                                                            Log.d("Notification", "Notification sent"))
                                                    .addOnFailureListener(e ->
                                                            Log.e("Notification", "Failed to send notification", e));

                                            if (isMutualLike) {
                                                NotificationModel matchNotification = new NotificationModel(
                                                        likedUser.getUid(),
                                                        likedUser.getName(),
                                                        likedUser.getProfileImageUrl(),
                                                        "It's a Match! You and " + likedUser.getName() + " liked each other!",
                                                        System.currentTimeMillis()
                                                );

                                                db.collection("UsersData").document(currentUid)
                                                        .collection("Notifications")
                                                        .document(likedUser.getUid())
                                                        .set(matchNotification);
                                            }
                                        });
                            });
                });
    }

    private void removeLikeNotification(String likedUserUid) {
        if (currentUid == null) return;

        db.collection("UsersData").document(likedUserUid)
                .collection("Notifications")
                .document(currentUid)
                .delete()
                .addOnSuccessListener(unused -> Log.d("Notification", "Notification removed"))
                .addOnFailureListener(e -> Log.e("Notification", "Failed to remove notification", e));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, userAge, compatibilityScore;
        ImageButton likeButton;
        CardView cardView;
        FrameLayout lockOverlay;

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
}*/
// fix the bug of liked problem
/*

package com.example.planpair.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.NotificationModel;
import com.example.planpair.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private boolean isPremium;
    private OnUserClickListener listener;
    private FirebaseFirestore db;
    private String currentUid;

    public interface OnUserClickListener {
        void onItemClick(User user);
    }

    public UserAdapter(Context context, List<User> userList, boolean isPremium, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.isPremium = isPremium;
        this.listener = listener;

        db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (position >= userList.size()) return; // Prevents crash
        User user = userList.get(position);

        holder.userName.setText(user.getName());
        holder.userAge.setText("Age: " + user.getAge());
        holder.compatibilityScore.setText("Compatibility: " + user.getCompatibility() + "%");

        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .into(holder.profileImage);

        holder.likeButton.setImageResource(user.isLiked()
                ? R.drawable.baseline_favorite_24
                : R.drawable.baseline_favorite_border_24);

        // Premium lock
        if (position >= 5 && !isPremium) {
            holder.lockOverlay.setVisibility(View.VISIBLE);
            holder.cardView.setAlpha(0.3f);
            holder.cardView.setClickable(false);
        } else {
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
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION || adapterPosition >= userList.size()) return;

            User currentUser = userList.get(adapterPosition);
            boolean newStatus = !currentUser.isLiked();
            currentUser.setLiked(newStatus);

            if (newStatus) {
                likeUser(currentUser);
            } else {
                unlikeUser(currentUser);
            }

            notifyItemChanged(adapterPosition);
        });

    }

    private void likeUser(User likedUser) {
        if (currentUid == null || likedUser.getUid() == null) return;

        // Add to current user's Likes
        db.collection("UsersData").document(currentUid)
                .collection("Likes").document(likedUser.getUid())
                .set(new HashMap<>())
                .addOnSuccessListener(aVoid -> {

                    // Add to liked user's LikedBy
                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("LikedBy").document(currentUid)
                            .set(new HashMap<>());

                    // Check if mutual
                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("Likes").document(currentUid)
                            .get()
                            .addOnSuccessListener(snapshot -> {

                                boolean isMutual = snapshot.exists();

                                sendLikeNotification(likedUser, isMutual);
                                Toast.makeText(context, isMutual ? "It's a Match!" : "Liked", Toast.LENGTH_SHORT).show();
                            });
                });
    }

    private void unlikeUser(User likedUser) {
        if (currentUid == null || likedUser.getUid() == null) return;

        // Remove from Likes and LikedBy
        db.collection("UsersData").document(currentUid)
                .collection("Likes").document(likedUser.getUid())
                .delete();

        db.collection("UsersData").document(likedUser.getUid())
                .collection("LikedBy").document(currentUid)
                .delete();

        // Remove notification
        db.collection("UsersData").document(likedUser.getUid())
                .collection("Notifications").document(currentUid)
                .delete();

        // Optional: Remove match notification from current user too
        db.collection("UsersData").document(currentUid)
                .collection("Notifications").document(likedUser.getUid())
                .delete();

        Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show();
    }

    private void sendLikeNotification(User likedUser, boolean isMutual) {
        if (currentUid == null) return;

        db.collection("UsersData").document(currentUid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    String senderName = snapshot.getString("username");
                    String senderImageUrl = snapshot.getString("profileImageUrl");

                    // Notify liked user
                    NotificationModel notification = new NotificationModel(
                            currentUid,
                            senderName,
                            senderImageUrl,
                            isMutual
                                    ? "It's a Match! You and " + senderName + " liked each other!"
                                    : senderName + " liked your profile!",
                            System.currentTimeMillis()
                    );

                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("Notifications").document(currentUid)
                            .set(notification);

                    // Notify current user if mutual
                    if (isMutual) {
                        NotificationModel matchBack = new NotificationModel(
                                likedUser.getUid(),
                                likedUser.getName(),
                                likedUser.getProfileImageUrl(),
                                "It's a Match! You and " + likedUser.getName() + " liked each other!",
                                System.currentTimeMillis()
                        );

                        db.collection("UsersData").document(currentUid)
                                .collection("Notifications").document(likedUser.getUid())
                                .set(matchBack);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, userAge, compatibilityScore;
        ImageButton likeButton;
        CardView cardView;
        FrameLayout lockOverlay;

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
}*/
// before
//Updated the like button notification and FCM Features.
//like fix code

package com.example.planpair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.R;
import com.example.planpair.models.NotificationModel;
import com.example.planpair.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final boolean isPremium;
    private final OnUserClickListener listener;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String currentUid  = FirebaseAuth.getInstance().getCurrentUser().getUid();


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
        if (position >= userList.size()) return;
        User user = userList.get(position);

        holder.userName.setText(user.getName());
        holder.userAge.setText("Age: " + user.getAge());
        holder.compatibilityScore.setText("Compatibility: " + user.getCompatibility() + "%");

        Glide.with(context)
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.default_profile)
                .into(holder.profileImage);

        holder.likeButton.setImageResource(
                user.isLiked()
                        ? R.drawable.baseline_favorite_24
                        : R.drawable.baseline_favorite_border_24
        );

        // Premium lock
        if (position >= 5 && !isPremium) {
            holder.lockOverlay.setVisibility(View.VISIBLE);
            holder.cardView.setAlpha(0.3f);
            holder.cardView.setClickable(false);
        } else {
            holder.lockOverlay.setVisibility(View.GONE);
            holder.cardView.setAlpha(1f);
            holder.cardView.setClickable(true);
        }
        holder.cardView.setOnClickListener(v -> {
            if (position < 5 || isPremium) {
                listener.onUserClick(user);
            }
        });


        holder.likeButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION || adapterPosition >= userList.size()) return;

            User currentUser = userList.get(adapterPosition);
            boolean newStatus = !currentUser.isLiked();
            currentUser.setLiked(newStatus);

            if (newStatus) {
                likeUser(currentUser);
            } else {
                unlikeUser(currentUser);
            }
            notifyItemChanged(adapterPosition);
        });

    }

    private void likeUser(User likedUser) {
        if (currentUid == null || likedUser.getUid() == null) return;

        // Add to current user's Likes
        db.collection("UsersData").document(currentUid)
                .collection("Likes").document(likedUser.getUid())
                .set(new HashMap<>())
                .addOnSuccessListener(aVoid -> {

                    // Add to liked user's LikedBy
                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("LikedBy").document(currentUid)
                            .set(new HashMap<>());

                    // Check if mutual
                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("Likes").document(currentUid)
                            .get()
                            .addOnSuccessListener(snapshot -> {

                                boolean isMutual = snapshot.exists();

                                sendLikeNotification(likedUser, isMutual);
                                Toast.makeText(context, isMutual ? "It's a Match!" : "Liked", Toast.LENGTH_SHORT).show();
                            });
                });
    }

    private void unlikeUser(User likedUser) {
        if (currentUid == null || likedUser.getUid() == null) return;

        // Remove from Likes and LikedBy
        db.collection("UsersData").document(currentUid)
                .collection("Likes").document(likedUser.getUid())
                .delete();

        db.collection("UsersData").document(likedUser.getUid())
                .collection("LikedBy").document(currentUid)
                .delete();

        // Remove notification
        db.collection("UsersData").document(likedUser.getUid())
                .collection("Notifications").document(currentUid)
                .delete();

        // Optional: Remove match notification from current user too
        db.collection("UsersData").document(currentUid)
                .collection("Notifications").document(likedUser.getUid())
                .delete();

        Toast.makeText(context, "Unliked", Toast.LENGTH_SHORT).show();
    }

    private void sendLikeNotification(User likedUser, boolean isMutual) {
        if (currentUid == null) return;

        db.collection("UsersData").document(currentUid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    String senderName = snapshot.getString("username");
                    String senderImageUrl = snapshot.getString("profileImageUrl");

                    // Create notification for the liked user
                    NotificationModel notification = new NotificationModel(
                            currentUid,
                            senderName,
                            senderImageUrl,
                            isMutual
                                    ? "It's a Match! You and " + senderName + " liked each other!"
                                    : senderName + " liked your profile!",
                            System.currentTimeMillis()
                    );

                    // Use .add() instead of .set() to avoid overwriting previous notifications
                    db.collection("UsersData").document(likedUser.getUid())
                            .collection("Notifications")
                            .add(notification);

                    // If mutual, send match notification to the current user too
                    if (isMutual) {
                        NotificationModel matchBack = new NotificationModel(
                                likedUser.getUid(),
                                likedUser.getName(),
                                likedUser.getProfileImageUrl(),
                                "It's a Match! You and " + likedUser.getName() + " liked each other!",
                                System.currentTimeMillis()
                        );

                        db.collection("UsersData").document(currentUid)
                                .collection("Notifications")
                                .add(matchBack); // Use .add() here too
                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, userAge, compatibilityScore;
        ImageButton likeButton;
        FrameLayout lockOverlay;
        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            compatibilityScore = itemView.findViewById(R.id.compatibilityScore);
            likeButton = itemView.findViewById(R.id.likeButton);
            lockOverlay = itemView.findViewById(R.id.lockOverlay);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
