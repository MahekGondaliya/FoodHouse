package com.example.food_app.model;

public class NotificationModel {

    // Firebase key
    private String notificationId;

    private String title;
    private String subtitle;
    private String description;
    private long timestamp;
    private String readableDate;
    private String userId;
    private String username;

    // Required empty constructor
    public NotificationModel() {}

    public NotificationModel(String title, String subtitle, String description,
                             long timestamp, String readableDate,
                             String userId, String username) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.timestamp = timestamp;
        this.readableDate = readableDate;
        this.userId = userId;
        this.username = username;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReadableDate() {
        return readableDate;
    }

    public void setReadableDate(String readableDate) {
        this.readableDate = readableDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
