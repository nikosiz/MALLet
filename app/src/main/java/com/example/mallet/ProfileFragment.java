package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileEmailLl.setOnClickListener(v -> showVerifyPasswordDialog());
        binding.profileUsernameLl.setOnClickListener(v -> showVerifyPasswordDialog()); // Add click listener for changing username
        binding.profileChangePasswordLl.setOnClickListener(v -> showChangePasswordDialog()); // Add click listener for changing password
        binding.profileNotificationsCl.setOnClickListener(v -> toggleNotificationSettings()); // Add click listener for changing notification settings
        binding.profileYourLanguagesLl.setOnClickListener(v -> openLanguageSettings()); // Add click listener for editing languages
        binding.profileSaveOfflineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> saveSetsOffline(isChecked)); // Add listener for saving sets offline
        binding.profileDarkModeLl.setOnClickListener(v -> toggleDarkMode()); // Add click listener for changing app mode to dark
        binding.profileLogOutLl.setOnClickListener(v -> logOut()); // Add click listener for logging out
        binding.profileAboutLl.setOnClickListener(v -> showAboutSection()); // Add click listener for viewing "about" section
        binding.profileDeleteAccountLl.setOnClickListener(v -> showDeleteAccountDialog()); // Add click listener for deleting the account

        binding.profileEmailLl.setOnClickListener(v -> showVerifyPasswordDialog());

    }

    private void showVerifyPasswordDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sheet_verify_password);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sheet_change_password);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void toggleNotificationSettings() {
        // Implement your logic to toggle notification settings
    }

    private void openLanguageSettings() {
        // Implement your logic to open the language settings screen
    }

    private void saveSetsOffline(boolean isChecked) {
        // Implement your logic to save sets for offline use
    }

    private void toggleDarkMode() {
        // Implement your logic to toggle dark mode
    }

    private void logOut() {
        // Implement your logic for logging out
    }

    private void showAboutSection() {
        // Implement your logic to show the "about" section
    }

    private void showDeleteAccountDialog() {
        // Implement your logic to show the delete account dialog
    }
}
