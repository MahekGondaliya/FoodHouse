package com.example.food_app.model;

public class Usermodel {
    private String username;
    private String email;
    private String imageUrl;
    private String phoneNo;
    private String role;

    // Empty constructor for Firebase
    public Usermodel() {}

    public Usermodel(String username, String email, String imageUrl, String phoneNo) {
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.phoneNo = phoneNo;
        this.role = "user";
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
