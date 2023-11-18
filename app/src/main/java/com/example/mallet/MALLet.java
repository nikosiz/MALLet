package com.example.mallet;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Calendar;

public class MALLet extends Application {
    private MALLet mallet;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static int MIN_FLASHCARDS_FOR_TEST = 25;
    public static int MIN_FLASHCARDS_FOR_MATCH = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTheme();

        mallet = new MALLet();
    }

    public MALLet getMALLet() {
        return mallet;
    }

    private void initializeTheme() {
        // Load the selected theme from SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        int selectedTheme = sharedPreferences.getInt("selectedTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // Set the theme based on the selectedTheme value
        AppCompatDelegate.setDefaultNightMode(selectedTheme);
    }

    public String getSavedTheme() {
        SharedPreferences preferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        return preferences.getString("selectedTheme", "System default");
    }

    public void saveSelectedTheme(String themeName) {
        SharedPreferences preferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTheme", themeName);
        editor.apply();
    }

    public void applyTheme(String themeName) {
        int themeMode;
        if (themeName.equals("Light")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (themeName.equals("Dark")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }

        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    // Method to set the theme based on user selection
    public void setTheme(String themeName) {
        saveSelectedTheme(themeName);
        applyTheme(themeName);
    }
}
