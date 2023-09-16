package com.example.mallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.example.mallet.databinding.ActivityMainFragmentProfileBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityMain_FragmentProfile extends Fragment {
    private ActivityMainFragmentProfileBinding binding;
    TextView themeTv;
    String selectedTheme;
    // Define username pattern using regex
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");
    private final String usernameIncorrect = "The username can only consist of letters, numbers, and underscores";
    private final String emailIncorrectMsg = "Email incorrect";
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");
    private final String passwordIncorrect = "The password must be at least 8 characters long and contain at least one digit, one small letter, one big letter and one special character.";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityMainFragmentProfileBinding.inflate(inflater, container, false);

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
        selectedTheme = getSavedTheme();
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

        EditText passwordEt = dialogBinding.verifyPasswordEt;
        TextView passwordErrTv = dialogBinding.verifyPasswordErrorTv;
        Utils.hideItem(passwordErrTv);
        Utils.setupTextWatcher(passwordEt, passwordErrTv, passwordPattern, "Password does not match valid pattern");

        TextView cancelTv = dialogBinding.verifyPasswordCancelTv;
        TextView confirmTv = dialogBinding.verifyPasswordConfirmTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(passwordEt, passwordErrTv);
            dialog.dismiss();
        });

        confirmTv.setOnClickListener(v -> {
            String email = Objects.requireNonNull(passwordEt.getText()).toString().trim();

            // Validate the email input in the dialog
            Utils.validateInput(passwordEt, passwordErrTv, passwordPattern, "Invalid password");

            if (!Utils.isErrVisible(passwordErrTv)) {
                dialog.dismiss();
                if (action == VerifyPasswordAction.CHANGE_EMAIL) {
                    // TODO: Implement password verification through AuthenticationManager
                    changeEmailDialog();
                    System.out.println("changeEmailDialog()");
                    System.out.println(email);
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

        TextInputEditText newEmailEt = dialogBinding.changeEmailEt;
        TextView emailErrTv = dialogBinding.changeEmailErrorTv;
        Utils.setupTextWatcher(newEmailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);

        TextView cancelTv = dialogBinding.changeEmailCancelTv;
        TextView confirmTv = dialogBinding.changeEmailConfirmTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(newEmailEt, emailErrTv);
            dialog.dismiss();
        });

        confirmTv.setOnClickListener(v -> {
            String newEmail = Objects.requireNonNull(newEmailEt.getText()).toString();
            Utils.validateInput(newEmailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrectMsg);

            // TODO: Implement password verification through AuthenticationManager

            if (!Utils.isErrVisible(emailErrTv)) {
                // TODO: Implement email change
                dialog.dismiss();
                System.out.println(newEmail);
                Utils.showToast(getContext(), "Email changed");
            }
        });
    }

    private void changeUsernameDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangeUsernameBinding dialogBinding = DialogChangeUsernameBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextInputEditText newUsernameEt = dialogBinding.changeUsernameNewEt;
        TextView usernameErrTv = dialogBinding.changeUsernameErrorTv;
        Utils.setupTextWatcher(newUsernameEt, usernameErrTv, usernamePattern, usernameIncorrect);

        TextView cancelTv = dialogBinding.changeUsernameCancelTv;
        TextView confirmTv = dialogBinding.changeUsernameConfirmTv;

        // Set click listeners and perform actions
        cancelTv.setOnClickListener(v -> dialog.dismiss());

        confirmTv.setOnClickListener(v -> {
            String newUsername = Objects.requireNonNull(newUsernameEt.getText()).toString();
            Utils.validateInput(newUsernameEt, usernameErrTv, usernamePattern, usernameIncorrect);

            // TODO: Implement password verification through AuthenticationManager

            if (!Utils.isErrVisible(usernameErrTv)) {
                // TODO: Implement username change
                dialog.dismiss();
                System.out.println(newUsername);
                Utils.showToast(getContext(), "Username changed");
            }
        });
    }

    private void changePasswordDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangePasswordBinding dialogBinding = DialogChangePasswordBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextInputEditText oldPasswordEt = dialogBinding.changePasswordOldEt;
        TextView oldPasswordErrorTv = dialogBinding.changePasswordOldErrorTv;
        Utils.setupTextWatcher(oldPasswordEt, oldPasswordErrorTv, passwordPattern, "Password incorrect");

        TextInputEditText newPasswordEt = dialogBinding.changePasswordNewEt;
        TextView newPasswordErrorTv = dialogBinding.changePasswordNewErrorTv;
        Utils.setupTextWatcher(newPasswordEt, newPasswordErrorTv, passwordPattern, passwordIncorrect);

        TextInputEditText confirmNewPasswordEt = dialogBinding.changePasswordConfirmNewEt;
        TextView confirmNewPasswordErrorTv = dialogBinding.changePasswordConfirmNewErrorTv;
        Utils.setupConfirmPasswordTextWatcher(newPasswordEt, confirmNewPasswordEt, confirmNewPasswordErrorTv, passwordPattern, passwordIncorrect);

        TextView cancelBtn = dialogBinding.changePasswordCancelTv;
        TextView confirmBtn = dialogBinding.changePasswordConfirmTv;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            String oldPassword = Objects.requireNonNull(oldPasswordEt.getText()).toString();
            Utils.validateInput(oldPasswordEt, oldPasswordErrorTv, passwordPattern, passwordIncorrect);

            String newPassword = Objects.requireNonNull(newPasswordEt.getText()).toString();
            Utils.validateInput(newPasswordEt, newPasswordErrorTv, passwordPattern, passwordIncorrect);

            String confirmNewPassword = Objects.requireNonNull(confirmNewPasswordEt.getText()).toString();
            Utils.validateInput(confirmNewPasswordEt, confirmNewPasswordErrorTv, passwordPattern, passwordIncorrect);

            if (!Utils.isErrVisible(oldPasswordErrorTv) && !Utils.isErrVisible(newPasswordErrorTv) && !Utils.isErrVisible(confirmNewPasswordErrorTv) && newPassword.equals(confirmNewPassword)) {
                // TODO: Implement password change
                Utils.resetEditText(oldPasswordEt, oldPasswordErrorTv);
                Utils.resetEditText(newPasswordEt, newPasswordErrorTv);
                Utils.resetEditText(confirmNewPasswordEt, confirmNewPasswordErrorTv);
                dialog.dismiss();
                System.out.println(oldPassword + "\n" + newPassword + "\n" + confirmNewPassword);
                Utils.showToast(getContext(), "Password changed");
            } else {
                Utils.showItem(confirmNewPasswordErrorTv);
                confirmNewPasswordErrorTv.setText("Passwords do not match");
                //Utils.showToast(getContext(), "Error is visible or passwords do not match");
            }
        });
    }


    private void notificationSettings() {
        SwitchCompat showNotificationsSwitch = binding.profileNotificationsMs;

        Utils.showToast(getContext(), "You will get notifications when the back end exists.");

        // Handle switch state change
        showNotificationsSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO: Implement notifications
                // Enable notifications
                Utils.showToast(getContext(), "You will get notifications when the back end exists.");
                binding.profileNotificationsMs.setChecked(true);
            } else {
                // TODO: Implement
                // Disable notifications
                Utils.showToast(getContext(), "You WILL NOT get notifications when the back end exists.");
                binding.profileNotificationsMs.setChecked(false);
            }
        });
    }

    private void saveSetsOffline() {
        SwitchCompat saveOfflineSwitch = binding.profileSaveOfflineMs;

        Utils.showToast(getContext(), "Sets will not be saved for offline use. The downloaded ones will be deleted (when the back end exists).");

        saveOfflineSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO
                // Download sets
                Utils.showToast(getContext(), "Sets downloaded (when the back end exists).");
            } else {
                // TODO
                // Delete downloaded sets
                Utils.showToast(getContext(), "Sets will not be saved for offline use. The downloaded ones will be deleted (when the back end exists).");
            }
        });
    }

    private void changeThemeDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogChangeThemeBinding dialogBinding = DialogChangeThemeBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        RadioGroup themeRadioGroup = dialogBinding.changeThemeRg;
        TextView cancelBtn = dialogBinding.changeThemeCancelBtn;
        TextView confirmBtn = dialogBinding.changeThemeConfirmBtn;

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

            saveSelectedTheme(selectedTheme);
            applyTheme(selectedTheme);
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

        Utils.showToast(getContext(), "You were logged out. For now, as we do not have any backend, we are just going to move you to the ActivityOpening");
        requireActivity().finish();
    }

    private void showDeleteAccountDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogDeleteAccountBinding dialogBinding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextInputEditText passwordEt = dialogBinding.deleteAccountPasswordEt;
        TextView passwordErrTv = dialogBinding.deleteAccountErrorTv;
        Utils.setupTextWatcher(passwordEt, passwordErrTv, passwordPattern, "Password incorrect");

        CheckBox cb = dialogBinding.deleteAccountCb;
        TextView cbErrTv = dialogBinding.deleteAccountCbErrorTv;

        TextView cancelTv = dialogBinding.deleteAccountCancelTv;
        TextView confirmTv = dialogBinding.deleteAccountConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());

        confirmTv.setOnClickListener(v -> {
            String password = Objects.requireNonNull(passwordEt.getText()).toString();
            Utils.validateInput(passwordEt, passwordErrTv, passwordPattern, "Password incorrect");

            if (!Utils.isErrVisible(passwordErrTv)) {
                if (!cb.isChecked()) {
                    cbErrTv.setText("You need to check this box");
                    Utils.showItem(cbErrTv);
                } else {
                    dialog.dismiss();
                    Utils.resetEditText(passwordEt, passwordErrTv);
                    System.out.println(password);

                    // TODO: Add functionality for checking if the password is correct
                    Utils.hideItem(passwordErrTv);
                    Utils.hideItem(cbErrTv);

                    Utils.showToast(getContext(), "When we have backend, we will check if the password is correct. For now we just jump to ChooseLogInSignup.java.");

                    Intent intent = new Intent(getContext(), ActivityOpening.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }
        });
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