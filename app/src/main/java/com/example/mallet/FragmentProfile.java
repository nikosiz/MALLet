// TODO:
//  1. Change SELECTED THEME TextView according to the theme selected

package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

import com.example.mallet.databinding.DialogAboutBinding;
import com.example.mallet.databinding.DialogChangeEmailBinding;
import com.example.mallet.databinding.DialogChangePasswordBinding;
import com.example.mallet.databinding.DialogChangeThemeBinding;
import com.example.mallet.databinding.DialogChangeUsernameBinding;
import com.example.mallet.databinding.DialogDeleteAccountBinding;
import com.example.mallet.databinding.DialogVerifyPasswordBinding;
import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.utils.FrontendUtils;
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
        setupListeners();
    }

    // Initialize click listeners for various UI elements
    private void setupListeners() {
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
        CHANGE_EMAIL, CHANGE_USERNAME
    }
    // Show a dialog to change email address

    private void changeEmailDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogChangeEmailBinding binding = DialogChangeEmailBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());


        TextView cancelBtn = binding.changeEmailCancelBtn;
        TextView confirmBtn = binding.changeEmailConfirmBtn;

        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView validEmailError = binding.changeEmailProvideValidErrorTv;
            TextView alreadyExistsError = binding.changeEmailAlreadyExistsErrorTv;
            TextView emptyFieldError = binding.changeEmailEmptyErrorTv;

            TextInputEditText newEmailEditText = binding.changeEmailNewEt;

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

        //dialog.show();
        FrontendUtils.showDialog(dialog);
    }

    // Show a dialog to verify the password before certain actions
    private void verifyPasswordDialog(VerifyPasswordAction action) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyPasswordBinding binding = DialogVerifyPasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

        // Find views inside the dialog layout
        TextView cancelBtn = binding.verifyCancelBtn;
        TextView confirmBtn = binding.verifyConfirmBtn;

        // Cancel button click listener
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        // Confirm button click listener
        confirmBtn.setOnClickListener(v -> {
            // TODO
            // Process password verification
            TextView passwordError = binding.verifyPasswordError;
            TextInputEditText passwordEditText = binding.verifyPasswordEt;
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
                    changeUsernameDialog();
                }
            }
        });

        // Show the dialog
        FrontendUtils.showDialog(dialog);
    }

    private void changeUsernameDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogChangeUsernameBinding binding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

        // Find views inside the dialog layout
        TextView cancelBtn = binding.changeUsernameCancelBtn;
        TextView confirmBtn = binding.changeUsernameConfirmBtn;

        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView alreadyExistsError = binding.changeUsernameAlreadyExistsErrorTv;
            TextView emptyFieldError = binding.changeUsernameEmptyErrorTv;

            TextInputEditText newUsernameEditText = binding.changeUsernameNewEt;

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
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogChangePasswordBinding binding = DialogChangePasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

        TextView cancelBtn = binding.changePasswordCancelBtn;
        TextView confirmBtn = binding.changePasswordConfirmBtn;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView oldPasswordError = binding.changePasswordOldErrorTv;
            TextView newPasswordError = binding.changePasswordNewErrorTv;
            TextView confirmNewPasswordError = binding.changePasswordConfirmNewErrorTv;
            TextInputEditText oldPasswordEditText = binding.changePasswordOldEt;
            TextInputEditText newPasswordEditText = binding.changePasswordNewEt;
            TextInputEditText confirmNewPasswordEditText = binding.changePasswordConfirmNewEt;

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

        FrontendUtils.showToast(getContext(), "The sets will not be saved for offline use.");

        saveOfflineSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Download sets
                FrontendUtils.showToast(getContext(), "Sets downloaded (when the back end exists).");
            } else {
                // TODO
                // Delete downloaded sets
                FrontendUtils.showToast(getContext(), "Sets deleted (when the back end exists).");
            }
        });
    }

    // Change the app's theme
    private void changeThemeDialog() {
        //final Dialog dialog = createDialog(R.layout.dialog_change_theme);
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogChangeThemeBinding binding = DialogChangeThemeBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

        // Find views inside the dialog layout
        TextView cancelBtn = binding.changeThemeCancelBtn;
        TextView confirmBtn = binding.changeThemeConfirmBtn;
        RadioGroup themeRadioGroup = binding.changeThemeRg;

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
        //final Dialog dialog = createDialog(R.layout.dialog_about);
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAboutBinding binding = DialogAboutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

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
        //final Dialog dialog = new Dialog(requireContext());
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogDeleteAccountBinding binding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(binding.getRoot());

        TextView cancelBtn = binding.deleteAccCancelBtn;
        TextView confirmBtn = binding.deleteAccConfirmBtn;
        CheckBox deleteAccountCheckBox = binding.deleteAccCb;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyPasswordError = binding.deleteAccEmptyError;
            TextView wrongPasswordError = binding.deleteAccWrongPassError;
            TextView checkError = binding.deleteAccCheckboxError;
            TextInputEditText passwordEditText = binding.deleteAccPasswordEt;

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
}