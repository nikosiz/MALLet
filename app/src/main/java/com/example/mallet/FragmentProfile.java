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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.DialogAboutBinding;
import com.example.mallet.databinding.DialogChangeEmailBinding;
import com.example.mallet.databinding.DialogChangePasswordBinding;
import com.example.mallet.databinding.DialogChangeUsernameBinding;
import com.example.mallet.databinding.DialogDeleteAccountBinding;
import com.example.mallet.databinding.DialogVerifyPasswordBinding;
import com.example.mallet.databinding.FragmentProfileBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.regex.Pattern;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding binding;
    private int clickCount = 0;
    int[] themeClickCounter = {0};
    private final Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^*()<>?/|}{~:]).{8,}$");

    // Declare your strings here, but don't initialize them immediately
    private String usernameIncorrect;
    private String emailIncorrect;
    private String passwordIncorrect;
    private TextView themeTv;
    private LinearLayout themeRgLl;
    private RadioButton systemThemeRb;
    private RadioButton lightThemeRb;
    private RadioButton darkThemeRb;
    private static final String PREFS_NAME = "ProfileAppSettings";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KET_SETS_OFFLINE = "setsOffline";
    private int selectedTheme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupContents();
        // Set themeTv based on the current theme
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            themeTv.setText("Light theme");
            lightThemeRb.setChecked(true);
            darkThemeRb.setChecked(false);
        } else if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            themeTv.setText("Dark theme");
            lightThemeRb.setChecked(false);
            darkThemeRb.setChecked(true);
        }

        // Initialize the strings requiring the context here
        usernameIncorrect = getString(R.string.username_incorrect);
        emailIncorrect = getString(R.string.email_incorrect);
        passwordIncorrect = getString(R.string.password_incorrect);


        return binding.getRoot();
    }

    private void setupContents() {
        LinearLayout emailLl = binding.profileEmailLl;
        LinearLayout usernameLl = binding.profileUsernameLll;
        TextView passwordTv = binding.profileChangePasswordTv;

        MaterialSwitch notificationsMs = binding.profileNotificationsMs;
        MaterialSwitch setsOfflineMs = binding.profileSaveOfflineMs;
        LinearLayout themeLl = binding.profileThemeLl;
        themeTv = binding.profileThemeTv;
        RadioGroup themeRg = binding.profileThemeRg;
        lightThemeRb = binding.profileLightThemeRb;
        darkThemeRb = binding.profileDarkThemeRb;
        TextView aboutTv = binding.profileAboutTv;
        TextView logoutTv = binding.profileLogoutTv;
        TextView deleteAccTv = binding.profileDeleteAccTv;

        //binding.profileUserPhotoIv.setOnClickListener(v -> Utils.openActivity(getContext(), ActivityViewProfile.class));

        emailLl.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_EMAIL));
        usernameLl.setOnClickListener(v -> verifyPasswordDialog(VerifyPasswordAction.CHANGE_USERNAME));
        passwordTv.setOnClickListener(v -> changePasswordDialog());

        notificationsMs.setChecked(getSwitchState(KEY_NOTIFICATIONS));

        notificationsMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // TODO: Implement notifications
                showNotifications();
                Utils.showToast(getContext(), "You will get notifications when the back end exists");
                binding.profileNotificationsMs.setChecked(true);
            } else {
                // TODO: Implement
                doNotShowNotifications();
                Utils.showToast(getContext(), "You will NOT get notifications when the back end exists");
                binding.profileNotificationsMs.setChecked(false);
            }
            saveSwitchState(KEY_NOTIFICATIONS, isChecked);
        });

        setsOfflineMs.setChecked(getSwitchState(KEY_NOTIFICATIONS));

        setsOfflineMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // TODO
                // Download sets
                Utils.showToast(getContext(), "Sets downloaded (when the back end exists)");
            } else {
                // TODO
                // Delete downloaded sets
                Utils.showToast(getContext(), "Sets will not be saved for offline use. The downloaded ones will be deleted (when the back end exists)");
            }
            saveSwitchState(KET_SETS_OFFLINE, isChecked);
        });

        Utils.hideItem(themeRg);
        Utils.disableItems(themeRg);

        themeLl.setOnClickListener(v -> {
            themeClickCounter[0]++;
            if (themeClickCounter[0] % 2 != 0) {
                Utils.showItem(themeRg);
                Utils.enableItems(themeRg);
            } else {
                Utils.hideItem(themeRg);
                Utils.disableItems(themeRg);
            }
        });

        themeRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.profile_lightThemeRb) {
                themeTv.setText("Light theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveSelectedTheme(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkedId == R.id.profile_darkThemeRb) {
                themeTv.setText("Dark theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveSelectedTheme(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        aboutTv.setOnClickListener(v -> aboutDialog());
        logoutTv.setOnClickListener(v -> logOut());

        deleteAccTv.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void saveSelectedTheme(int selectedTheme) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("selectedTheme", selectedTheme);

        editor.apply();
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
        Utils.setupTextWatcher(newEmailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrect);

        TextView cancelTv = dialogBinding.changeEmailCancelTv;
        TextView confirmTv = dialogBinding.changeEmailConfirmTv;

        cancelTv.setOnClickListener(v -> {
            Utils.resetEditText(newEmailEt, emailErrTv);
            dialog.dismiss();
        });

        confirmTv.setOnClickListener(v -> {
            String newEmail = Objects.requireNonNull(newEmailEt.getText()).toString();
            Utils.validateInput(newEmailEt, emailErrTv, Patterns.EMAIL_ADDRESS, emailIncorrect);

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

    private void showNotifications() {
        //todo
    }

    private void doNotShowNotifications() {
        //todo
    }

    private void saveSwitchState(String switchKey, boolean isChecked) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(switchKey, isChecked);
        editor.apply();
    }

    private boolean getSwitchState(String switchKey) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(switchKey, false); // Default value (false) if key not found
    }


    private void aboutDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_change_password);
        DialogAboutBinding dialogBinding = DialogAboutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void logOut() {
        // TODO: Implement logic for logging out

        clickCount++;
        if (clickCount == 1) {
            Utils.showToast(getContext(), "Click once again to log out");
        } else if (clickCount == 2) {
            requireActivity().finish();
            clickCount = 0;
        }
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

                    Utils.showToast(getContext(), "When we have backend, we will check if the password is correct. For now we just jump to ChooseLogInSignup.java");

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