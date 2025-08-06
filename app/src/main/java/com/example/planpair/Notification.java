package com.example.planpair;

public class Notification {
    private String username;
    private boolean isMessage; // true = message, false = like
    private String profileImageUrl; // resource ID for user image

    public Notification(String username, boolean isMessage, String profileImageUrl) {
        this.username = username;
        this.isMessage = isMessage;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}