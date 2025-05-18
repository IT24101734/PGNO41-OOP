package com.rentaluser.model;

public class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String fullName;

    // Constructor with all fields
    public User(String userId, String username, String password, String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    // Default constructor
    public User() {
        this.userId = "";
        this.username = "";
        this.password = "";
        this.email = "";
        this.fullName = "";
    }

}
