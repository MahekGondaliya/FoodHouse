package com.example.food_app.model;

public class Option {
    private final int iconRes;
    private final String title;
    private final Runnable action;

    public Option(int iconRes, String title, Runnable action) {
        this.iconRes = iconRes;
        this.title = title;
        this.action = action;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }

    public Runnable getAction() {
        return action;
    }
}
