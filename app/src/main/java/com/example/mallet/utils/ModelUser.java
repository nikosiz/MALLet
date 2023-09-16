package com.example.mallet.utils;

public class ModelUser {
    // Attributes to represent user information
    private String username;
    private String email;

    // Constructor to initialize user information
    public ModelUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getter for the username
    public String getUsername() {
        return username;
    }

    // Setter for the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for the email
    public String getEmail() {
        return email;
    }

    // Setter for the email
    public void setEmail(String email) {
        this.email = email;
    }
}