package com.example.planpair;

public class SliderItem {
    private int imageResId;
    private String info;

    public SliderItem(int imageResId, String info) {
        this.imageResId = imageResId;
        this.info = info;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getInfo() {
        return info;
    }

}