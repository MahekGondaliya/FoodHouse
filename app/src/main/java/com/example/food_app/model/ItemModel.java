package com.example.food_app.model;

public class ItemModel {
    private final String  imageResId;
    private final String name;

    public ItemModel(String imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }
}

