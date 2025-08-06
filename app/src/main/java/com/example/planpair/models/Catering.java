package com.example.planpair.models;

import java.util.List;

public class Catering {
    private String id;
    private String category;
    private String name;
    private String location;
    private double rating;
    private int reviewCount;
    private int imageUrl;
    private int startingPrice;
    private String services;
    private String about;
    private List<Integer> photoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setPhotoList(List<Integer> photoList) {
        this.photoList = photoList;
    }

    public Catering(String id, String name, String location, double rating, int reviewCount,
                    int imageUrl, int startingPrice, String services, String about, List<Integer> photoList) {
        this.id = id;
        this.category = "caterer";
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.startingPrice = startingPrice;
        this.services = services;
        this.about = about;
        this.photoList = photoList;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServices() {
        return services;
    }

    public String getAbout() {
        return about;
    }

    public List<Integer> getPhotoList() {
        return photoList;
    }
}
