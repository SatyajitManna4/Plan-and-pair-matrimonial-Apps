
package com.example.planpair.models;

public class User {
    private String username;
    private int age;
    private int compatibility;
    private int profileImageUrl;

    public User(String username, int age, int compatibility, int profileImageUrl) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
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

    public int getprofileImageUrl() {
        return profileImageUrl;
    }

    public void setImageResId(int profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
// Satya
/*
package com.example.planpair.models;
public class User {
    private String username;
    private int age;
    private int compatibility;
    private String profileImageUrl;

    public User() {} // Firestore requires empty constructor

    public User(String username, int age, int compatibility, String profileImageUrl) {
        this.username = username;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() { return username; }
    public int getAge() { return age; }
    public int getCompatibility() { return compatibility; }
    public String getProfileImageUrl() { return profileImageUrl; }
}
*/
