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
import android.widget.EditText;
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
import com.example.mallet.databinding.DialogChangePictureBinding;
import com.example.mallet.databinding.DialogChangeThemeBinding;
import com.example.mallet.databinding.DialogChangeUsernameBinding;
import com.example.mallet.databinding.DialogDeleteAccountBinding;
import com.example.mallet.databinding.DialogVerifyPasswordBinding;
import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding binding;
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    TextView themeTv;
    String selectedTheme;
    MALLet mallet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupContents();

        return binding.getRoot();

    }

    private void setupContents() {
        binding.profileUserPhotoIv.setOnClickListener(v -> changePictureDialog());

        binding.profileEmailLl.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_EMAIL));
        binding.profileUsernameLll.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_USERNAME));
        binding.profileChangePasswordTv.setOnClickListener(v -> changePasswordDialog());

        binding.profileNotificationsMs.setOnClickListener(v -> notificationSettings());

        binding.profileSaveOfflineMs.setOnCheckedChangeListener((buttonView, isChecked) -> saveSetsOffline());

        binding.profileThemeLl.setOnClickListener(v -> changeThemeDialog());
        themeTv = binding.profileThemeTv;
        selectedTheme=getSavedTheme();
        themeTv.setText(selectedTheme);

        binding.profileAboutTv.setOnClickListener(v -> showAboutSection());
        binding.profileLogoutTv.setOnClickListener(v -> logOut());

        binding.profileDeleteAccTv.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void changePictureDialog() {
        // TODO: Dialog with pictures to choose from and apply
        Dialog dialog = createDialog(R.layout.dialog_change_picture);
        DialogChangePictureBinding dialogBinding = DialogChangePictureBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public enum VerifyPasswordAction {
        CHANGE_EMAIL, CHANGE_USERNAME
    }

    private void verifyPasswordDialog(VerifyPasswordAction action) {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogVerifyPasswordBinding dialogBinding = DialogVerifyPasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextInputLayout passwordTil = dialogBinding.verifyPasswordTil;
        EditText passwordEt = dialogBinding.verifyPasswordEt;
        TextView passwordErrorTv = dialogBinding.verifyPasswordErrorTv;
        Utils.hideItem(passwordErrorTv);
        Utils.setupTextWatcher(passwordEt, passwordErrorTv, passwordPattern, "Password does not match valid pattern");

        TextView cancelTv = dialogBinding.verifyPasswordCancelTv;
        TextView confirmTv = dialogBinding.verifyPasswordConfirmTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(passwordEt, passwordErrorTv);
            dialog.dismiss();
        });

        confirmTv.setOnClickListener(v -> {
            String email = Objects.requireNonNull(passwordEt.getText()).toString().trim();

            // Validate the email input in the dialog
            Utils.validateInput(passwordEt, passwordErrorTv, passwordPattern, "ASDF");

            if (!Utils.isErrorVisible(passwordErrorTv)) {
                dialog.dismiss();
                if (action == VerifyPasswordAction.CHANGE_EMAIL) {
                    // TODO: Implement password verification through AuthenticationManager
                    changeEmailDialog();
                    System.out.println("changeEmailDialog()");
                }

                if (action == VerifyPasswordAction.CHANGE_USERNAME) {
                    // TODO: Implement password verification through AuthenticationManager
                    changeUsernameDialog();
                    System.out.println("changeUsernameDialog()");
                }
            }
        });
    }

    private void changeEmailDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangeEmailBinding dialogBinding = DialogChangeEmailBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView cancelBtn = dialogBinding.changeEmailCancelBtn;
        TextView confirmBtn = dialogBinding.changeEmailConfirmBtn;

        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView validEmailError = dialogBinding.changeEmailProvideValidErrorTv;
            TextView alreadyExistsError = dialogBinding.changeEmailAlreadyExistsErrorTv;
            TextView emptyFieldError = dialogBinding.changeEmailEmptyErrorTv;

            TextInputEditText newEmailEditText = dialogBinding.changeEmailNewEt;

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
                Utils.showToast(getContext(), "OK button was clicked. The email should be changed but there is no backend yet.");
                dialog.dismiss();
            }
            dialog.show();
        });

        dialog.show();
    }

    private void changeUsernameDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangeUsernameBinding dialogBinding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();


        // Find views inside the dialog layout
        TextView cancelBtn = dialogBinding.changeUsernameCancelBtn;
        TextView confirmBtn = dialogBinding.changeUsernameConfirmBtn;

        // Set click listeners and perform actions
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView alreadyExistsError = dialogBinding.changeUsernameAlreadyExistsErrorTv;
            TextView emptyFieldError = dialogBinding.changeUsernameEmptyErrorTv;

            TextInputEditText newUsernameEditText = dialogBinding.changeUsernameNewEt;

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
                Utils.showToast(getContext(), "OK button was clicked. The username should be changed but there is no backend yet.");
                dialog.dismiss();
            }
            dialog.show();
        });

        dialog.show();
    }

    private void changePasswordDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView cancelBtn = dialogBinding.changePasswordCancelBtn;
        TextView confirmBtn = dialogBinding.changePasswordConfirmBtn;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView oldPasswordError = dialogBinding.changePasswordOldErrorTv;
            TextView newPasswordError = dialogBinding.changePasswordNewErrorTv;
            TextView confirmNewPasswordError = dialogBinding.changePasswordConfirmNewErrorTv;
            TextInputEditText oldPasswordEditText = dialogBinding.changePasswordOldEt;
            TextInputEditText newPasswordEditText = dialogBinding.changePasswordNewEt;
            TextInputEditText confirmNewPasswordEditText = dialogBinding.changePasswordConfirmNewEt;

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

                Utils.showToast(getContext(), "All fields must be filled.");
            } else {
                // TODO: Add functionality changing password
                oldPasswordError.setVisibility(View.GONE);
                newPasswordError.setVisibility(View.GONE);
                confirmNewPasswordError.setVisibility(View.GONE);
                Utils.showToast(getContext(), "OK button was clicked. The password should be changed but there is no backend yet.");
                dialog.dismiss();
            }
        });
    }

    // Handle notification settings
    private void notificationSettings() {
        SwitchCompat showNotificationsSwitch = binding.profileNotificationsMs;

        Utils.showToast(getContext(), "You will get notifications when the back end exists.");

        // Handle switch state change
        showNotificationsSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Enable notifications
                Utils.showToast(getContext(), "You will get notifications when the back end exists.");
                binding.profileNotificationsMs.setChecked(true);
            } else {
                // TODO
                // Disable notifications
                Utils.showToast(getContext(), "You WILL NOT get notifications when the back end exists.");
                binding.profileNotificationsMs.setChecked(false);
            }
        });
    }

    // Save sets offline or delete them
    private void saveSetsOffline() {
        SwitchCompat saveOfflineSwitch = binding.profileSaveOfflineMs;

        Utils.showToast(getContext(), "The sets will not be saved for offline use.");

        saveOfflineSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Download sets
                Utils.showToast(getContext(), "Sets downloaded (when the back end exists).");
            } else {
                // TODO
                // Delete downloaded sets
                Utils.showToast(getContext(), "Sets deleted (when the back end exists).");
            }
        });
    }

    // Change the app's theme
    private void changeThemeDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangeThemeBinding dialogBinding = DialogChangeThemeBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        // Find views inside the dialog layout
        TextView cancelBtn = dialogBinding.changeThemeCancelBtn;
        TextView confirmBtn = dialogBinding.changeThemeConfirmBtn;
        RadioGroup themeRadioGroup = dialogBinding.changeThemeRg;

        String savedTheme = getSavedTheme();

        if (savedTheme.equals("Light")) {
            themeRadioGroup.check(R.id.light_theme_rb);
        } else if (savedTheme.equals("Dark")) {
            themeRadioGroup.check(R.id.dark_theme_rb);
        } else {
            themeRadioGroup.check(R.id.system_default_rb);
        }

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            int checkedId = themeRadioGroup.getCheckedRadioButtonId();

            if (checkedId == R.id.light_theme_rb) {
                selectedTheme = "Light";

            } else if (checkedId == R.id.dark_theme_rb) {
                selectedTheme = "Dark";
            } else {
                selectedTheme = "System default";
            }

            // Save the selected theme
            saveSelectedTheme(selectedTheme);

            // Apply the selected theme immediately
            applyTheme(selectedTheme);

            // Update the displayed theme in your UI
            themeTv.setText(selectedTheme);

            dialog.dismiss();
        });
    }

    private void saveSelectedTheme(String themeName) {
        SharedPreferences preferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedTheme", themeName);
        editor.apply();
    }

    private String getSavedTheme() {
        SharedPreferences preferences = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        return preferences.getString("selectedTheme", "System default");
    }

    private void applyTheme(String themeName) {
        int themeMode;
        if (themeName.equals("Light")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (themeName.equals("Dark")) {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }



        AppCompatDelegate.setDefaultNightMode(themeMode);

        requireActivity().recreate();
    }

    private void showAboutSection() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogAboutBinding dialogBinding = DialogAboutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    private void logOut() {
        // TODO: Implement logic for logging out

        Utils.showToast(getContext(), "You were logged out. For now, as we do not have any backend, we are just going to move you to the ChooseLogInSignupActivity.java");
        requireActivity().finish();
    }

    private void showDeleteAccountDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogDeleteAccountBinding dialogBinding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView cancelBtn = dialogBinding.deleteAccCancelBtn;
        TextView confirmBtn = dialogBinding.deleteAccConfirmBtn;
        CheckBox deleteAccountCheckBox = dialogBinding.deleteAccCb;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyPasswordError = dialogBinding.deleteAccEmptyError;
            TextView wrongPasswordError = dialogBinding.deleteAccWrongPassError;
            TextView checkError = dialogBinding.deleteAccCheckboxError;
            TextInputEditText passwordEditText = dialogBinding.deleteAccPasswordEt;

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
                Utils.showToast(getContext(), "When we have backend, we will check if the password is correct. For now we just jump to ChooseLogInSignup.java.");
                Intent intent = new Intent(getContext(), ActivityOpening.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        dialog.show();
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}