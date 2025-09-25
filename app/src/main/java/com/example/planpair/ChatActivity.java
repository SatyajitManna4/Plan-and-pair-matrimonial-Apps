package com.example.planpair;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpair.adapters.ChatAdapter;
import com.example.planpair.models.MessageModel;
import com.example.planpair.utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private String currentUserId, otherUserId, otherUserName;
    private FirebaseFirestore db;
    private ChatAdapter adapter;
    private EditText messageInput;
    private ImageButton sendBtn,backBtn;
    private ImageView profileImage;
    private TextView usernameText;
    private RecyclerView chatRecycler;

    private TextView newMessageIndicator;
    private int unreadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUserId = getIntent().getStringExtra("otherUserId");
        otherUserName = getIntent().getStringExtra("otherUsername");

        messageInput = findViewById(R.id.chat_message_input);
        sendBtn = findViewById(R.id.message_send_btn);

        profileImage = findViewById(R.id.profile_pic_image_view);
        usernameText = findViewById(R.id.other_username);
        chatRecycler = findViewById(R.id.chat_recycler_view);
        backBtn = findViewById(R.id.back_btn);

        newMessageIndicator = findViewById(R.id.new_message_indicator);

        usernameText.setText(otherUserName);

        loadOtherUserProfileImage();
        setupRecyclerView();
        chatRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null && lm.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // ✅ User reached the latest message
                    unreadCount = 0;
                    newMessageIndicator.setVisibility(View.GONE);
                }
            }
        });
        newMessageIndicator.setOnClickListener(v -> {
            chatRecycler.smoothScrollToPosition(adapter.getItemCount() - 1);
            unreadCount = 0;
            newMessageIndicator.setVisibility(View.GONE);
        });

        sendBtn.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendTextMessage(text);
            }
        });


        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ChatActivity.this,ChatListActivity.class);
            startActivity(intent);
        });
    }
    private void loadOtherUserProfileImage() {
        db.collection("UsersData").document(otherUserId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String imageUrl = snapshot.getString("profileImageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            if (!isDestroyed() && !isFinishing()) {   // <-- Add this check
                                Glide.with(this).load(imageUrl).into(profileImage);
                            }
                        }
                    }
                });
    }
    private void setupRecyclerView() {
        Query query = db.collection("UsersData")
                .document(currentUserId)
                .collection("Matches")
                .document(otherUserId)
                .collection("Messages")
                .orderBy("timestamp", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();

        adapter = new ChatAdapter(options, currentUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);   // 👈 important
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(adapter);
    }

    private void sendTextMessage(String text) {
        MessageModel message = new MessageModel(null, currentUserId, otherUserId, text, "text", System.currentTimeMillis(), false);
        sendMessageToFirestore(message);
    }
    private void sendMessageToFirestore(MessageModel message) {
        db.collection("UsersData")
                .document(currentUserId)
                .collection("Matches")
                .document(otherUserId)
                .collection("Messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    db.collection("UsersData")
                            .document(otherUserId)
                            .collection("Matches")
                            .document(currentUserId)
                            .collection("Messages")
                            .add(message);

                    updateLastMessage(message);
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Send message failed", e);
                });
    }

    private void updateLastMessage(MessageModel message) {
        Map<String, Object> lastMessageData = new HashMap<>();
        lastMessageData.put("lastMessage", message.getType().equals("text") ? message.getMessage() : "📷 Image");
        lastMessageData.put("timestamp", message.getTimestamp());
        lastMessageData.put("type", message.getType());

        db.collection("UsersData").document(currentUserId)
                .collection("Matches").document(otherUserId)
                .set(lastMessageData, SetOptions.merge());

        db.collection("UsersData").document(otherUserId)
                .collection("Matches").document(currentUserId)
                .set(lastMessageData, SetOptions.merge());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    int lastVisiblePosition =
                            ((LinearLayoutManager) chatRecycler.getLayoutManager())
                                    .findLastCompletelyVisibleItemPosition();

                    if (lastVisiblePosition == -1) return;

                    // ✅ Get the new message
                    MessageModel newMessage = adapter.getItem(positionStart);

                    // ✅ Play sound only if it's from the other user
                    if (newMessage != null && !newMessage.getSenderId().equals(currentUserId)) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(ChatActivity.this, R.raw.message_tone);
                        mediaPlayer.setOnCompletionListener(mp -> mp.release()); // release after playing
                        mediaPlayer.start();
                    }

                    // ✅ Handle scroll / unread badge
                    if (lastVisiblePosition == adapter.getItemCount() - itemCount - 1) {
                        chatRecycler.smoothScrollToPosition(adapter.getItemCount() - 1);
                        unreadCount = 0;
                        newMessageIndicator.setVisibility(View.GONE);
                    } else {
                        unreadCount++;
                        newMessageIndicator.setText(unreadCount + " new message"
                                + (unreadCount > 1 ? "s" : ""));
                        newMessageIndicator.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}