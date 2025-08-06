package com.example.planpair.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Vendor implements Parcelable {
    private String id;
    private String category;
    private String name;
    private String location;
    private double rating;
    private int reviewCount;
    private int startingPrice;
    private int imageResId;
    private String type;
    private int guestCount;
    private int roomCount;
    private String services;
    private String about;
    private ArrayList<Integer> photos;

    // ✅ Constructor — matches your usage
    public Vendor(String category, String id, String name, String location,
                  double rating, int reviewCount, int startingPrice,
                  int imageResId,String type, int guestCount, int roomCount, String services, String about) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.startingPrice = startingPrice;
        this.imageResId = imageResId;
        this.type = type;
        this.guestCount = guestCount;
        this.roomCount = roomCount;
        this.services = services;
        this.about = about;
        this.photos = new ArrayList<>();
    }

    // ✅ Parcelable constructor
    protected Vendor(Parcel in) {
        category = in.readString();
        id = in.readString();
        name = in.readString();
        location = in.readString();
        rating = in.readDouble();
        reviewCount = in.readInt();
        startingPrice = in.readInt();
        imageResId = in.readInt();
        services = in.readString();
        about = in.readString();
        photos = new ArrayList<>();
        in.readList(photos, Integer.class.getClassLoader()); // ✅ safer than createIntegerArrayList
    }

    public static final Creator<Vendor> CREATOR = new Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeDouble(rating);
        dest.writeInt(reviewCount);
        dest.writeInt(startingPrice);
        dest.writeInt(imageResId);
        dest.writeString(services);
        dest.writeString(about);
        dest.writeList(photos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // ✅ Getters & Setters
    public String getCategory() { return category; }
    public String getId() { return id; }
    public String getName() { return name; }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setServices(String services) {
        this.services = services;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() { return location; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public int getStartingPrice() { return startingPrice; }
    public int getImageResId() { return imageResId; }
    public String getServices() { return services; }
    public String getAbout() { return about; }
    public ArrayList<Integer> getPhotos() { return photos; }
    public void setPhotos(ArrayList<Integer> photos) { this.photos = photos; }
}
