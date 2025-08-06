/*
package com.example.planpair;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.ChatAdapter;
import com.example.planpair.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    private FirebaseFirestore db;
    private String currentUserId;
    private String matchedUserId;
    private String matchedUserProfileUrl;
    private String chatId;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchedUserId = getIntent().getStringExtra("userId");
        matchedUserProfileUrl = getIntent().getStringExtra("matchedUserProfileUrl");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;


        if (currentUserId == null || matchedUserId == null) {
            Toast.makeText(this, "User information missing. Exiting chat.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chatId = currentUserId.compareTo(matchedUserId) < 0
                ? currentUserId + "_" + matchedUserId
                : matchedUserId + "_" + currentUserId;

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.message_send_btn);

        adapter = new ChatAdapter(this, messageList, currentUserId, matchedUserProfileUrl);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        loadMessages();

        sendButton.setOnClickListener(v -> {
            String msg = messageEditText.getText().toString().trim();
            if (!msg.isEmpty()) {
                sendMessage(msg);
                messageEditText.setText(""); // Clear input after sending
            }
        });

        ImageButton imagePickerButton = findViewById(R.id.imagePickerButton);
        imagePickerButton.setOnClickListener(v -> openImagePicker());
    }

    private void loadMessages() {
        db.collection("Chats").document(chatId)
                .collection("Messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("Chat", "Listen failed: " + e.getMessage());
                        return;
                    }
                    if (snapshots == null) return;
                    messageList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        ChatMessage message = doc.toObject(ChatMessage.class);
                        if (message != null) {
                            messageList.add(message);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!messageList.isEmpty()) {
                        chatRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    private void sendMessage(String msg) {
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", currentUserId);
        message.put("receiverId", matchedUserId);
        message.put("message", msg);
        message.put("timestamp", FieldValue.serverTimestamp());
        message.put("messageType", "text");

        db.collection("Chats").document(chatId)
                .collection("Messages")
                .add(message)
                .addOnSuccessListener(doc -> Log.d("Chat", "Text message sent"))
                .addOnFailureListener(e -> Log.e("Chat", "Failed to send text message: " + e.getMessage()));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        String fileName = "images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    sendImageMessage(uri.toString());
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show());
    }

    private void sendImageMessage(String imageUrl) {
        Map<String, Object> message = new HashMap<>();
        message.put("senderId", currentUserId);
        message.put("receiverId", matchedUserId);
        message.put("message", imageUrl);
        message.put("timestamp", FieldValue.serverTimestamp());
        message.put("messageType", "image");

        db.collection("Chats").document(chatId)
                .collection("Messages")
                .add(message)
                .addOnSuccessListener(doc -> Log.d("Chat", "Image message sent."))
                .addOnFailureListener(e -> Log.e("Chat", "Failed to send image message: " + e.getMessage()));
    }
}
*/
//s

package com.example.planpair;

import android.content.Intent;
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

        usernameText.setText(otherUserName);

        loadOtherUserProfileImage();
        setupRecyclerView();

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
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
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
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}