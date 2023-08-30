// TODO:
//  1. Change SELECTED THEME TextView according to the theme selected

package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FragmentProfile extends Fragment {

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
        setupClickListeners();
    }

    // Initialize click listeners for various UI elements
    private void setupClickListeners() {
        binding.profileUserPhotoIv.setOnClickListener(v -> changePicture());
        binding.profileEmailLl.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_EMAIL));
        binding.profileUsernameLl.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_USERNAME));
        binding.profileChangePasswordLl.setOnClickListener(v -> changePasswordDialog());
        binding.profileNotificationsSwitch.setOnClickListener(v -> notificationSettings());
        binding.profileSaveOfflineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> saveSetsOffline());
        binding.profileThemeLl.setOnClickListener(v -> changeThemeDialog());
        binding.profileAboutLl.setOnClickListener(v -> showAboutSection());
        binding.profileLogOutLl.setOnClickListener(v -> logOut());
        binding.profileDeleteAccountLl.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void changePicture() {
        // TODO: Dialog with pictures to choose from and apply for user
        FrontendUtils.showToast(getContext(), "Here a dialog will appear and you will have the possibility to choose a new picture.");
    }


    // Enum to represent password verification actions
    public enum VerifyPasswordAction {
        CHANGE_EMAIL, CHANGE_USERNAME;
    }
    // Show a dialog to change email address

    private void changeEmailDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_email);

        // Find views inside the dialog layout
        TextView cancelBtn = dialog.findViewById(R.id.change_email_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_email_confirm_btn);

        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView validEmailError = dialog.findViewById(R.id.change_email_provide_valid_error_tv);
            TextView alreadyExistsError = dialog.findViewById(R.id.change_email_already_exists_error_tv);
            TextView emptyFieldError = dialog.findViewById(R.id.change_email_empty_error_tv);

            TextInputEditText newEmailEditText = dialog.findViewById(R.id.change_email_new_et);

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
                validEmailError.setVisibility(View.GONE);
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
                FrontendUtils.showToast(getContext(), "OK button was clicked. The email should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });

        FrontendUtils.showDialog(dialog);
    }

    // Show a dialog to verify the password before certain actions
    private void verifyPasswordDialog(VerifyPasswordAction action) {
        final Dialog dialog = createDialog(R.layout.dialog_verify_password);

        // Find views inside the dialog layout
        TextView cancelBtn = dialog.findViewById(R.id.verify_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.verify_confirm_btn);

        // Cancel button click listener
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        // Confirm button click listener
        confirmBtn.setOnClickListener(v -> {
            // TODO
            // Process password verification
            TextView passwordError = dialog.findViewById(R.id.verify_password_error);
            TextInputEditText passwordEditText = dialog.findViewById(R.id.verify_password_et);
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            // Perform action based on verification and action type
            if (TextUtils.isEmpty(password)) {
                passwordError.setVisibility(View.VISIBLE);
            } else {
                // TODO
                // Add functionality verifying password
                passwordError.setVisibility(View.GONE);
                FrontendUtils.showToast(getContext(), "OK button was clicked. The password should be verified but there is no backend yet.");
                dialog.dismiss();

                if (action == VerifyPasswordAction.CHANGE_EMAIL) {
                    changeEmailDialog();
                } else if (action == VerifyPasswordAction.CHANGE_USERNAME) {
                    changeUsername();
                }
            }
        });

        // Show the dialog
        FrontendUtils.showDialog(dialog);
    }

    private void changeUsername() {
        final Dialog dialog = createDialog(R.layout.dialog_change_username);

        // Find views inside the dialog layout
        TextView cancelBtn = dialog.findViewById(R.id.change_username_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_username_confirm_btn);

        // Set click listeners and perform actions
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
                emptyFieldError.setVisibility(View.GONE);
                alreadyExistsError.setVisibility(View.GONE);
                FrontendUtils.showToast(getContext(), "OK button was clicked. The username should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });

        FrontendUtils.showDialog(dialog);
    }

    private void changePasswordDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);

        // Find views inside the dialog layout
        TextView cancelBtn = dialog.findViewById(R.id.change_password_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_password_confirm_btn);

        // Set click listeners and perform actions
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

                FrontendUtils.showToast(getContext(), "All fields must be filled.");
            } else {
                // TODO: Add functionality changing password
                oldPasswordError.setVisibility(View.GONE);
                newPasswordError.setVisibility(View.GONE);
                confirmNewPasswordError.setVisibility(View.GONE);
                FrontendUtils.showToast(getContext(), "OK button was clicked. The password should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });
        FrontendUtils.showDialog(dialog);

    }

    // Handle notification settings
    private void notificationSettings() {
        SwitchCompat showNotificationsSwitch = binding.profileNotificationsSwitch;

        showNotificationsSwitch.setChecked(true);
        FrontendUtils.showToast(getContext(), "You will get notifications when the back end exists.");

        // Handle switch state change
        showNotificationsSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Enable notifications
                FrontendUtils.showToast(getContext(), "You will get notifications when the back end exists.");
                binding.profileNotificationsSwitch.setChecked(true);
            } else {
                // TODO
                // Disable notifications
                FrontendUtils.showToast(getContext(), "You WILL NOT get notifications when the back end exists.");
                binding.profileNotificationsSwitch.setChecked(false);
            }
        });
    }

    // Save sets offline or delete them
    private void saveSetsOffline() {
        SwitchCompat saveOfflineSwitch = binding.profileSaveOfflineSwitch;

        saveOfflineSwitch.setChecked(false);
        FrontendUtils.showToast(getContext(), "The sets will not be saved for offline use.");

        saveOfflineSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Download sets
                FrontendUtils.showToast(getContext(), "Sets downloaded (when the back end exists).");
            } else {
                // TODO
                // Delete sets
                FrontendUtils.showToast(getContext(), "Sets deleted (when the back end exists).");
            }
        });
    }

    // Change the app's theme
    private void changeThemeDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_theme);

        // Find views inside the dialog layout
        TextView cancelBtn = dialog.findViewById(R.id.change_theme_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.change_theme_confirm_btn);
        RadioGroup themeRadioGroup = dialog.findViewById(R.id.change_theme_rg);

        String savedTheme = getSavedTheme();

        if (savedTheme.equals("Light Theme")) {
            themeRadioGroup.check(R.id.light_theme_rb);

        } else if (savedTheme.equals("Dark Theme")) {
            themeRadioGroup.check(R.id.dark_theme_rb);
        } else {
            themeRadioGroup.check(R.id.system_default_rb);
        }
        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            int checkedId = themeRadioGroup.getCheckedRadioButtonId();

            String selectedThemeName;
            if (checkedId == R.id.light_theme_rb) {
                selectedThemeName = "Light Theme";
            } else if (checkedId == R.id.dark_theme_rb) {
                selectedThemeName = "Dark Theme";
            } else {
                selectedThemeName = "System Default Theme";
            }

            saveSelectedTheme(selectedThemeName);
            FrontendUtils.showToast(getContext(), selectedThemeName);
            applyTheme(selectedThemeName);
            dialog.dismiss();
        });
        FrontendUtils.showDialog(dialog);
    }


    private void saveSelectedTheme(String themeName) {
        SharedPreferences preferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTheme", themeName);
        editor.apply();
    }

    private String getSavedTheme() {
        SharedPreferences preferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        return preferences.getString("selectedTheme", "System Default Theme");
    }

    private void applyTheme(String themeName) {
        int themeMode;
        if (themeName.equals("Light Theme")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (themeName.equals("Dark Theme")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }

        AppCompatDelegate.setDefaultNightMode(themeMode);

        requireActivity().recreate();
    }

    private void showAboutSection() {
        final Dialog dialog = createDialog(R.layout.dialog_about);

        // Find views inside the dialog layout

        FrontendUtils.showDialog(dialog);
        //FrontendUtils.showToast(getContext(), "This is the about section.");
    }

    private void logOut() {
        // TODO: Implement logic for logging out

        FrontendUtils.showToast(getContext(), "You were logged out. For now, as we do not have any backend, we are just going to move you to the ChooseLogInSignUpActivity.java");
        requireActivity().finish();
    }

    private void showDeleteAccountDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_account);


        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView cancelBtn = dialog.findViewById(R.id.delete_acc_cancel_btn);
        TextView confirmBtn = dialog.findViewById(R.id.delete_acc_confirm_btn);
        CheckBox deleteAccountCheckBox = dialog.findViewById(R.id.delete_acc_cb);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyPasswordError = dialog.findViewById(R.id.delete_acc_empty_error);
            TextView wrongPasswordError = dialog.findViewById(R.id.delete_acc_wrong_pass_error);
            TextView checkError = dialog.findViewById(R.id.delete_acc_checkbox_error);
            TextInputEditText passwordEditText = dialog.findViewById(R.id.delete_acc_password_et);

            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if (TextUtils.isEmpty(password) && !deleteAccountCheckBox.isChecked()) {
                emptyPasswordError.setVisibility(View.VISIBLE);
                checkError.setVisibility(View.VISIBLE);
                wrongPasswordError.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(password) && !deleteAccountCheckBox.isChecked()) {
                emptyPasswordError.setVisibility(View.GONE);
                checkError.setVisibility(View.VISIBLE);
                wrongPasswordError.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(password) && deleteAccountCheckBox.isChecked()) {
                emptyPasswordError.setVisibility(View.VISIBLE);
                checkError.setVisibility(View.GONE);
                wrongPasswordError.setVisibility(View.GONE);
            } else {
                // TODO: Add functionality for checking if the password is correct
                emptyPasswordError.setVisibility(View.GONE);
                checkError.setVisibility(View.GONE);
                FrontendUtils.showToast(getContext(), "When we have backend, we will check if the password is correct. For now we just jump to ChooseLogInSignUp.java.");
                Intent intent = new Intent(getContext(), ActivityOpening.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        FrontendUtils.showDialog(dialog);
    }

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