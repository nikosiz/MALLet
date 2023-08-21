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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

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

    public enum ProfileAction {
        CHANGE_EMAIL, CHANGE_USERNAME
    }

    // Set up click listeners for profile actions
    private void setupClickListeners() {
        binding.profileEmailLl.setOnClickListener(v -> showVerifyPasswordDialog(ProfileAction.CHANGE_EMAIL));
        binding.profileUsernameLl.setOnClickListener(v -> showVerifyPasswordDialog(ProfileAction.CHANGE_USERNAME));
        binding.profileChangePasswordLl.setOnClickListener(v -> changePassword());
        binding.profileNotificationsSwitch.setOnClickListener(v -> notificationSettings());
        binding.profileSaveOfflineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> saveSetsOffline(isChecked));
        binding.profileThemeLl.setOnClickListener(v -> toggleDarkMode());
        binding.profileLogOutLl.setOnClickListener(v -> logOut());
        binding.profileAboutLl.setOnClickListener(v -> showAboutSection());
        binding.profileDeleteAccountLl.setOnClickListener(v -> showDeleteAccountDialog());
    }

    // Show the verify password dialog
    private void showVerifyPasswordDialog(ProfileAction action) {
        final Dialog dialog = createDialog(R.layout.dialog_verify_password);

        dialog.show();

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.verify_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.verify_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView passwordError = dialog.findViewById(R.id.verify_password_error);
            TextInputEditText passwordEditText = dialog.findViewById(R.id.verify_password_et);

            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if (TextUtils.isEmpty(password)) {
                passwordError.setVisibility(View.VISIBLE);
            } else {
                // TODO: Add functionality verifying password
                passwordError.setVisibility(View.GONE);
                showToast("OK button was clicked. The password should be verified but there is no backend yet.");
                dialog.dismiss();

                // Perform the appropriate action based on the provided action parameter
                if (action == ProfileAction.CHANGE_EMAIL) {
                    changeEmail();
                } else if (action == ProfileAction.CHANGE_USERNAME) {
                    changeUsername();
                }
            }
        });
    }

    private void changeEmail() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_email);
        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.change_email_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_email_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView validEmailError = dialog.findViewById(R.id.change_email_provide_valid_error_tv);
            TextView alreadyExistsError = dialog.findViewById(R.id.change_email_already_exists_error_tv);
            TextView emptyFieldError = dialog.findViewById(R.id.change_email_empty_error_tv);

            TextInputEditText newEmailEditText = dialog.findViewById(R.id.change_email_new_et); // Correct the ID here

            String newEmail = Objects.requireNonNull(newEmailEditText.getText()).toString();

            if (TextUtils.isEmpty(newEmail)) {
                emptyFieldError.setVisibility(View.VISIBLE);
                validEmailError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                validEmailError.setVisibility(View.VISIBLE);
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
            } else {
                // TODO: Add functionality checking if email is already assigned to another account\
                // if() {} else {}
                // TODO: Add functionality changing email
                // Hide errors, close the dialog, and show toast
                validEmailError.setVisibility(View.GONE);
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
                showToast("OK button was clicked. The email should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    private void changeUsername() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_username);
        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Find and set up cancel and confirm buttons
        TextView cancelBtn = dialog.findViewById(R.id.change_username_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_username_confirm_btn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView alreadyExistsError = dialog.findViewById(R.id.change_username_already_exists_error_tv);
            TextView emptyFieldError = dialog.findViewById(R.id.change_username_empty_error_tv);

            TextInputEditText newUsernameEditText = dialog.findViewById(R.id.change_username_new_et);

            String newUsername = Objects.requireNonNull(newUsernameEditText.getText()).toString();

            if (TextUtils.isEmpty(newUsername)) {
                emptyFieldError.setVisibility(View.VISIBLE);
                alreadyExistsError.setVisibility(View.GONE);
                // TODO: Add functionality checking if new username matches the pattern
            /*} else if (!Patterns.EMAIL_ADDRESS.matcher(newUsername).matches()) {
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);*/
            } else {
                // TODO: Add functionality checking if username already exists
                // if() {} else {}
                // TODO: Add functionality changing username
                // Hide errors, close the dialog, and show toast
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
                showToast("OK button was clicked. The username should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    private void changePassword() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

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

            String oldPassword = Objects.requireNonNull(oldPasswordEditText.getText()).toString();
            String newPassword = Objects.requireNonNull(newPasswordEditText.getText()).toString();
            String confirmNewPassword = Objects.requireNonNull(confirmNewPasswordEditText.getText()).toString();

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
                // TODO: Add functionality changing password
                // Hide errors and close the dialog
                oldPasswordError.setVisibility(View.GONE);
                newPasswordError.setVisibility(View.GONE);
                confirmNewPasswordError.setVisibility(View.GONE);
                showToast("OK button was clicked. The password should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });

    }

    private void notificationSettings() {
        SwitchCompat showNotificationsSwitch = binding.profileNotificationsSwitch;

        showNotificationsSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO: Add functionality showing notifications
                showToast("You will get notifications when the back end exists.");
            } else {
                showToast("You will not get notifications when the back end exists.");
            }
        });
    }

    private void saveSetsOffline(boolean isChecked) {
        SwitchCompat saveOfflineSwitch = binding.profileSaveOfflineSwitch;

        saveOfflineSwitch.setOnCheckedChangeListener((compoundButton, isChecked1) -> {
            if (isChecked1) {
                // TODO: Add functionality downloading sets
                showToast("Sets downloaded (when the back end exists).");
            } else {
                // TODO: Add functionality deleting sets
                showToast("Sets deleted (when the back end exists).");
            }
        });
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

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
}