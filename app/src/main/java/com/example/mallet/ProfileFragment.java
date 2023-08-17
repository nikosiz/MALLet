package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

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

        // Click listeners for various profile actions
        setupClickListeners();
    }

    // Set up click listeners for profile actions
    private void setupClickListeners() {
        binding.profileEmailLl.setOnClickListener(v -> showVerifyPasswordDialog());
        binding.profileUsernameLl.setOnClickListener(v -> showVerifyPasswordDialog());
        binding.profileChangePasswordLl.setOnClickListener(v -> showChangePasswordDialog());
        binding.profileNotificationsCl.setOnClickListener(v -> toggleNotificationSettings());
        binding.profileYourLanguagesLl.setOnClickListener(v -> openLanguageSettings());
        binding.profileSaveOfflineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> saveSetsOffline(isChecked));
        binding.profileDarkModeLl.setOnClickListener(v -> toggleDarkMode());
        binding.profileLogOutLl.setOnClickListener(v -> logOut());
        binding.profileAboutLl.setOnClickListener(v -> showAboutSection());
        binding.profileDeleteAccountLl.setOnClickListener(v -> showDeleteAccountDialog());
    }

    // Show the verify password dialog
    private void showVerifyPasswordDialog() {
        final Dialog dialog = createDialog(R.layout.sheet_verify_password);

        dialog.show();

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.verify_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.verify_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView passwordError = dialog.findViewById(R.id.verify_password_error);
            TextInputEditText passwordEditText = dialog.findViewById(R.id.verify_password_et);

            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(password)) {

                passwordError.setVisibility(View.VISIBLE);
            } else {
                passwordError.setVisibility(View.GONE);
                showToast("OK button was clicked. The password should be verified but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    // Create a dialog with common properties
    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sheet_change_password);

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.change_password_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_password_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView oldPasswordError = dialog.findViewById(R.id.change_password_old_error_tv);
            TextView newPasswordError = dialog.findViewById(R.id.change_password_new_error_tv);
            TextView confirmNewPasswordError = dialog.findViewById(R.id.change_password_confirm_new_error_tv);
            TextInputEditText oldPasswordEditText = dialog.findViewById(R.id.change_password_old_et);
            TextInputEditText newPasswordEditText = dialog.findViewById(R.id.change_password_new_et);
            TextInputEditText confirmNewPasswordEditText = dialog.findViewById(R.id.change_password_confirm_new_et);

            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
                // Display error for empty fields
                if (TextUtils.isEmpty(oldPassword)) {
                    oldPasswordError.setVisibility(View.VISIBLE);
                } else {
                    oldPasswordError.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(newPassword)) {
                    newPasswordError.setVisibility(View.VISIBLE);
                } else {
                    newPasswordError.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(confirmNewPassword)) {
                    confirmNewPasswordError.setVisibility(View.VISIBLE);
                } else {
                    confirmNewPasswordError.setVisibility(View.GONE);
                }

                showToast("All fields must be filled.");
            } else {
                // Hide errors and close the dialog
                oldPasswordError.setVisibility(View.GONE);
                newPasswordError.setVisibility(View.GONE);
                confirmNewPasswordError.setVisibility(View.GONE);

                showToast("OK button was clicked. The password should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });

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
