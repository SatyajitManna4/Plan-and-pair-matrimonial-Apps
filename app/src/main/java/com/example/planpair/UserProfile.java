/*
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

*/
// satyajit 1.00

package com.example.planpair;
import java.util.List;

public class UserProfile {

    private String uid;

    private List<String> travel;
    private List<String> creative_passions;
    private List<String> movies;
    private List<String> music;
    private List<String> marriage;
    private List<String> language;
    private List<String> food;
    private List<String> family_structure;
    private List<String> familyType;
    private List<String> WeddingType;

    private String social_media;
    private String religion;
    private String degree;
    private String Season;
    private String gender;
    private String profileImageUrl;
    private String age;
    private String community;
    private String education;


    // Required empty constructor for Firestore
    public UserProfile() {
    }

    // UID (optional, not stored in Firestore document by default)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getTravel() {
        return travel;
    }

    public void setTravel(List<String> travel) {
        this.travel = travel;
    }

    public List<String> getCreative_passions() {
        return creative_passions;
    }

    public void setCreative_passions(List<String> creative_passions) {
        this.creative_passions = creative_passions;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    public List<String> getMusic() {
        return music;
    }

    public void setMusic(List<String> music) {
        this.music = music;
    }

    public List<String> getMarriage() {
        return marriage;
    }

    public void setMarriage(List<String> marriage) {
        this.marriage = marriage;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getFood() {
        return food;
    }

    public void setFood(List<String> food) {
        this.food = food;
    }

    public List<String> getFamily_structure() {
        return family_structure;
    }

    public void setFamily_structure(List<String> family_structure) {
        this.family_structure = family_structure;
    }

    public List<String> getFamilyType() {
        return familyType;
    }

    public void setFamilyType(List<String> familyType) {
        this.familyType = familyType;
    }

    public List<String> getWeddingType() {
        return WeddingType;
    }

    public void setWeddingType(List<String> weddingType) {
        this.WeddingType = weddingType;
    }

    public String getSocial_media() {
        return social_media;
    }

    public void setSocial_media(String social_media) {
        this.social_media = social_media;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSeason() {
        return Season;
    }

    public void setSeason(String season) {
        this.Season = season;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

}
