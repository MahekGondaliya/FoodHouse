package com.example.food_app.model;

public class RecycleViewRecommendModel {


    private final String imageResId;
    private final String title;

    private final String price;
    private final String rating;

    private final String description;

    public RecycleViewRecommendModel(
            String imageResId, String title, String price, String rating, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.description = description;
    }


    public String getImageResId() {
        return imageResId;
    }

    public String gettitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

}
