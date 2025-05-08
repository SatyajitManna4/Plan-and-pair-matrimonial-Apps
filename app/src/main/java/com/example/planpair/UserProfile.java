package com.example.planpair;

import java.util.List;

public class UserProfile {

    // --- Basic Personal Info ---
    private String fullName;
    private String dateOfBirth;  // format: yyyy-MM-dd
    private int age;
    private String gender;
    private String religionOrCaste;

    // --- Interests & Preferences ---
    private List<String> hobbiesLifestyle;
    private List<String> relationshipPreferences;
    private List<String> personalPassions;
    private List<String> culturalIdentity;
    private List<String> communicationPreferences;

    // --- Wedding Goals ---
    private String weddingType;
    private String celebrationPreference;
    private String weddingBudget;
    private String guestCount;
    private String weddingSeason;
    private String weddingTheme;
    private String cateringStyle;
    private List<String> weddingEntertainment;

    // --- Firebase / Meta Info ---
    private String uid;  // Firebase UID
    private String profileImageUrl;

    // --- Getters & Setters ---
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getReligionOrCaste() { return religionOrCaste; }
    public void setReligionOrCaste(String religionOrCaste) { this.religionOrCaste = religionOrCaste; }

    public List<String> getHobbiesLifestyle() { return hobbiesLifestyle; }
    public void setHobbiesLifestyle(List<String> hobbiesLifestyle) { this.hobbiesLifestyle = hobbiesLifestyle; }

    public List<String> getRelationshipPreferences() { return relationshipPreferences; }
    public void setRelationshipPreferences(List<String> relationshipPreferences) { this.relationshipPreferences = relationshipPreferences; }

    public List<String> getPersonalPassions() { return personalPassions; }
    public void setPersonalPassions(List<String> personalPassions) { this.personalPassions = personalPassions; }

    public List<String> getCulturalIdentity() { return culturalIdentity; }
    public void setCulturalIdentity(List<String> culturalIdentity) { this.culturalIdentity = culturalIdentity; }

    public List<String> getCommunicationPreferences() { return communicationPreferences; }
    public void setCommunicationPreferences(List<String> communicationPreferences) { this.communicationPreferences = communicationPreferences; }

    public String getWeddingType() { return weddingType; }
    public void setWeddingType(String weddingType) { this.weddingType = weddingType; }

    public String getCelebrationPreference() { return celebrationPreference; }
    public void setCelebrationPreference(String celebrationPreference) { this.celebrationPreference = celebrationPreference; }

    public String getWeddingBudget() { return weddingBudget; }
    public void setWeddingBudget(String weddingBudget) { this.weddingBudget = weddingBudget; }

    public String getGuestCount() { return guestCount; }
    public void setGuestCount(String guestCount) { this.guestCount = guestCount; }

    public String getWeddingSeason() { return weddingSeason; }
    public void setWeddingSeason(String weddingSeason) { this.weddingSeason = weddingSeason; }

    public String getWeddingTheme() { return weddingTheme; }
    public void setWeddingTheme(String weddingTheme) { this.weddingTheme = weddingTheme; }

    public String getCateringStyle() { return cateringStyle; }
    public void setCateringStyle(String cateringStyle) { this.cateringStyle = cateringStyle; }

    public List<String> getWeddingEntertainment() { return weddingEntertainment; }
    public void setWeddingEntertainment(List<String> weddingEntertainment) { this.weddingEntertainment = weddingEntertainment; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}

