package com.example.planpair.models;

import java.util.List;

public class Photographer {
    private String id;
    private String category;
    private String name;
    private String city;
    private int imageUrl;
    private String service;
    private double rating;
    private int reviewCount;
    private int pPrice;// per day charge
    private int pvPrice;

    // New fields
    private String deliveryTime;
    private String advancePayment;
    private String travelPolicy;
    private String experience;
    private String clientFeedback;
    private List<Integer> photoList;

    public Photographer() {
        // Needed for Firebase
    }

    public Photographer(String id,String name, String city, int imageUrl, String service,
                        double rating, int reviewCount, int pPrice,int pvPrice,
                        String deliveryTime, String advancePayment, String travelPolicy,
                        String experience, String clientFeedback,List<Integer> photoList) {
        this.id =id;
        this.category = "photographer";
        this.name = name;
        this.city = city;
        this.imageUrl = imageUrl;
        this.service = service;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.pPrice = pPrice;
        this.pvPrice = pvPrice;
        this.deliveryTime = deliveryTime;
        this.advancePayment = advancePayment;
        this.travelPolicy = travelPolicy;
        this.experience = experience;
        this.clientFeedback = clientFeedback;
        this.photoList = photoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhotoList(List<Integer> photoList) {
        this.photoList = photoList;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getImageUrl() { return imageUrl; }
    public void setImageUrl(int imageUrl) { this.imageUrl = imageUrl; }

    public String getService() { return service; }
    public void setService(String description) { this.service = description; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public int getpPrice() {
        return pPrice;
    }

    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }

    public int getPvPrice() {
        return pvPrice;
    }

    public List<Integer> getPhotoList() {
        return photoList;
    }

    public void setPvPrice(int pvPrice) {
        this.pvPrice = pvPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getAdvancePayment() { return advancePayment; }
    public void setAdvancePayment(String advancePayment) { this.advancePayment = advancePayment; }

    public String getTravelPolicy() { return travelPolicy; }
    public void setTravelPolicy(String travelPolicy) { this.travelPolicy = travelPolicy; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getClientFeedback() { return clientFeedback; }
    public void setClientFeedback(String clientFeedback) { this.clientFeedback = clientFeedback; }
}
