package com.example.mallet.utils;

import com.google.android.material.materialswitch.MaterialSwitch;

public class AccountManager {
    private static AccountManager instance;

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public boolean signup(String email, String password) {

        return true;
    }

    public boolean login(String email, String password) {
        return true;
    }

    public boolean changeEmail(String newEmail) {
        return true;
    }

    public boolean changeUsername(String newUsername) {
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        return true;
    }

    public boolean notifications(MaterialSwitch notificationsSwitch) {
        return true;
    }

    public boolean setsOffline(MaterialSwitch saveOfflineSwitch) {
        return true;
    }

    public boolean logout() {
        return true;
    }

    public boolean deleteAccount(String password) {
        return true;
    }
}
