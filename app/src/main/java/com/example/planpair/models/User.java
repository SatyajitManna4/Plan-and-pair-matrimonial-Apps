//Map document fields with firestore database..

package com.example.planpair.models;

public class User {
    private String username;
    private String uid;
    private int age;
    private int compatibility;
    private String profileImageUrl;
    private boolean isLiked;

    private boolean isPremium; // NEW

    public User() {} // Firestore requires empty constructor

    public User(String username, int age, int compatibility, String profileImageUrl, boolean isPremium, String uid,boolean isLiked) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
        this.isPremium = isPremium;
        this.uid = uid;
        this.isLiked = isLiked;
    }

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

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getUid() { return uid; }

    public boolean isLiked() { return isLiked; }

    public void setLiked(boolean liked) { isLiked = liked; }
}
