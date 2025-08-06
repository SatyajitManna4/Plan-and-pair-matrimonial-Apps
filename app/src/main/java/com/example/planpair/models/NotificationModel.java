/*
package com.example.planpair.models;

public class NotificationModel {
    public String senderUid, senderName, senderImageUrl, message;
    public long timestamp;

    public NotificationModel() {} // Needed for Firestore

    public NotificationModel(String senderUid, String senderName, String senderImageUrl, String message, long timestamp) {
        this.senderUid = senderUid;
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.message = message;
        this.timestamp = timestamp;
    }
}
*/
package com.example.planpair.models;

public class NotificationModel {
    private String senderId;
    private String senderName;
    private String senderImageUrl;
    private String message;
    private long timestamp;

    public NotificationModel() {
        // Needed for Firestore deserialization
    }

    public NotificationModel(String senderId, String senderName, String senderImageUrl, String message, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
