
package com.example.planpair.models;

public class User {
    private String name;
    private int age;
    private int compatibility;
    private int imageResId;

    public User(String name, int age, int compatibility, int imageResId) {
        this.name = name;
        this.age = age;
        this.compatibility = compatibility;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
// Satya
/*
package com.example.planpair.models;
public class User {
    private String name;
    private int age;
    private int compatibility;
    private String profileImageUrl;

    public User() {} // Firestore requires empty constructor

    public User(String name, int age, int compatibility, String profileImageUrl) {
        this.name = name;
        this.age = age;
        this.compatibility = compatibility;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public int getCompatibility() { return compatibility; }
    public String getProfileImageUrl() { return profileImageUrl; }
}
*/
