package com.example.planpair.models;

public class MatchedUser {
    private static String userId;
    private static String name;
    private String profileImageUrl; // Optional: for displaying profile image
    private String lastMessage;
    private String timestamp;

    public MatchedUser() {
        // Required empty constructor for Firebase
    }

    public MatchedUser(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public MatchedUser(String userId, String name, String profileImageUrl, String lastMessage, String timestamp) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
