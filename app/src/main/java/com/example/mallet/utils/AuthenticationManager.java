package com.example.mallet.utils;

import com.example.mallet.client.user.UserServiceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationManager {

    private static AuthenticationManager instance;
    private final UserServiceImpl userService;

    // Singleton pattern to ensure a single instance of AuthenticationManager
    public static AuthenticationManager getInstance() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    private AuthenticationManager() {
        userService = new UserServiceImpl();
        // Private constructor to prevent instantiation
    }

    // TODO: Add authentication logic for user registration (sign-up) here
    public boolean signUp(String email, String password) {
        // TODO: Implement sign-up logic here
        //  Return true if registration is successful, false otherwise
        //  Firebase Authentication or backend API for registration can be used
        /** Sign-up logic here*/
        return true;
    }

    // Add your authentication logic for user login here
    public boolean login(String email, String password) {
        // Implement your login logic here
        // Return true if login is successful, false otherwise
        // You can use Firebase Authentication or your own backend API for login
        // Replace this with your actual implementation
        // Your login logic here

        return false;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
