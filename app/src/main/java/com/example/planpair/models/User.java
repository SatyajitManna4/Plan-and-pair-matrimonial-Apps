/*
package com.example.planpair.models;

public class User {
    private String username;
    private String uid;
    private int age;
    private int compatibility;
    private String profileImageUrl;
    private boolean isLiked;
    private boolean isPremium;

    // Chat-related fields
    private String lastMessage;
    private long lastMessageTime;
    private boolean isOnline;
    private int unreadCount;

    // Required for Firestore
    public User() {}

    // Constructor for HomeActivity (7 params)
    public User(String username, int age, int compatibility, String profileImageUrl,
                boolean isPremium, String uid, boolean isLiked) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
        this.isPremium = isPremium;
        this.uid = uid;
        this.isLiked = isLiked;

        // Set default values for chat-related fields
        this.lastMessage = "";
        this.lastMessageTime = 0L;
        this.isOnline = false;
        this.unreadCount = 0;
    }

    // Constructor for ChatActivity (11 params)
    public User(String uid, String username, String profileImageUrl, String lastMessage,
                long lastMessageTime, boolean isOnline, int unreadCount) {
        this.uid = uid;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.isOnline = isOnline;
        this.unreadCount = unreadCount;

        // Set default values for other fields
        this.age = 0;
        this.compatibility = 0;
        this.isPremium = false;
        this.isLiked = false;
    }

    public User(String username, int age, int compatibility, String profileImageUrl) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
    }


    // Getters and Setters
    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(int compatibility) {
        this.compatibility = compatibility;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
*/
package com.example.planpair.models;

public class User {
    private String username;
    private String uid;
    private int age;
    private int compatibility;
    private String profileImageUrl;
    private boolean isLiked;
    private boolean isPremium;

    // Chat-related fields
    private String lastMessage;
    private long lastMessageTime;
    private boolean isOnline;
    private int unreadCount;

    // Default constructor for Firestore
    public User() {
        this.username = "";
        this.uid = "";
        this.age = 0;
        this.compatibility = 0;
        this.profileImageUrl = "";
        this.isLiked = false;
        this.isPremium = false;
        this.lastMessage = "";
        this.lastMessageTime = 0L;
        this.isOnline = false;
        this.unreadCount = 0;
    }

    // Constructor for HomeActivity
    public User(String username, int age, int compatibility, String profileImageUrl,
                boolean isPremium, String uid, boolean isLiked) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
        this.isPremium = isPremium;
        this.uid = uid;
        this.isLiked = isLiked;

        this.lastMessage = "";
        this.lastMessageTime = 0L;
        this.isOnline = false;
        this.unreadCount = 0;
    }

    // Constructor for ChatActivity
    public User(String uid, String username, String profileImageUrl, String lastMessage,
                long lastMessageTime, boolean isOnline, int unreadCount) {
        this.uid = uid;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.isOnline = isOnline;
        this.unreadCount = unreadCount;

        this.age = 0;
        this.compatibility = 0;
        this.isPremium = false;
        this.isLiked = false;
    }

    // Constructor for simplified use
    public User(String username, int age, int compatibility, String profileImageUrl) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;

        this.isLiked = false;
        this.isPremium = false;
        this.uid = "";
        this.lastMessage = "";
        this.lastMessageTime = 0L;
        this.isOnline = false;
        this.unreadCount = 0;
    }

    // Getters and Setters
    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(int compatibility) {
        this.compatibility = compatibility;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
