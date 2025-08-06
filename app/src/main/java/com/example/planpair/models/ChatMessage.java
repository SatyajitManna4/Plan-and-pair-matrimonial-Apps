package com.example.planpair.models;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private String messageType; // "text" or "image"
    private Timestamp timestamp;
    private boolean seen; // new field to track if message was seen

    public ChatMessage() {
        // Required empty constructor for Firestore serialization
    }

    public ChatMessage(String senderId, String receiverId, String message, String messageType, Timestamp timestamp, boolean seen) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }

    /**
     * Returns timestamp in milliseconds since epoch
     */
    public long getTimestamp() {
        if (timestamp != null) {
            return timestamp.toDate().getTime();
        } else {
            return 0;
        }
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
