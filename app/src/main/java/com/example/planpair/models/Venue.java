package com.example.planpair.models;

import java.util.List;

public class Venue {
    private String id;
    private String category;
    private int imageRes;
    private String name, location;
    private int price,guestCount, roomCount;
    private double rating;
    private String type;
    private int maxPrice, minPrice;
    private List<Integer> photoList;

    public Venue() {}
    public Venue(String id, int imageRes, String name, String location, int price, int guestCount, int roomCount, double rating, String type, int maxPrice, int minPrice, List<Integer> photoList) {
        this.category = "venue";
        this.id = id;
        this.imageRes = imageRes;
        this.name = name;
        this.location = location;
        this.price = price;
        this.guestCount = guestCount;
        this.roomCount = roomCount;
        this.rating = rating;
        this.type = type;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.photoList = photoList;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public List<Integer> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Integer> photoList) {
        this.photoList = photoList;
    }


    // getters for all fields including id and category

    public String getId() { return id; }
    public String getCategory() { return category; }
    //... other getters
}

