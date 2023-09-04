// TODO:
//  1. After clicking "add new", the selection of the previous item disappears
//  2. Fix all @SuppressLint("RestrictedApi")s

package com.example.mallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mallet.databinding.ActivityMainBinding;
import com.example.mallet.databinding.DialogCreateFolderBinding;
import com.example.mallet.databinding.DialogCreateGroupBinding;
import com.example.mallet.databinding.DialogCreateNewBinding;
import com.example.mallet.databinding.DialogCreateSetBinding;
import com.example.mallet.databinding.DialogForgotPasswordBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ActivityMain extends AppCompatActivity {

    // Binding object to access views in the layout
    private ActivityMainBinding binding;
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
    private int selectedFragmentId = R.id.bottom_nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Restore selected fragment state
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            binding.bottomNavigationView.setSelectedItemId(selectedFragmentId);
        } else {
            replaceFragment(new FragmentHome());
        }

        // Set exception item index (0-based)
        int exceptionItemIndex = 2;
        setExceptionItemColor(exceptionItemIndex);

        // Set a listener for bottom navigation view item selection
        //default: binding.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentId);
        super.onSaveInstanceState(outState);
    }

    // Method to replace the fragment in the bottom navigation container
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the fragment in the main_activity_frame_layout container with the new fragment
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Method to show the bottom dialog for "Create new" options
    private void showCreateNewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogCreateNewBinding binding = DialogCreateNewBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(R.layout.dialog_create_new);

        LinearLayout createSet = binding.addNewCreateSet;
        LinearLayout createFolder = binding.addNewCreateFolder;
        LinearLayout createGroup = binding.addNewCreateGroup;

        createSet.setOnClickListener(v -> {
            dialog.dismiss();
            dialogCreateNewSet();
        });

        createFolder.setOnClickListener(v -> {
            dialog.dismiss();
            dialogCreateNewFolder();
        });

        createGroup.setOnClickListener(v -> {
            dialog.dismiss();
            dialogCreateNewGroup();
        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void dialogCreateNewSet() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogCreateSetBinding binding = DialogCreateSetBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        TextInputEditText setNameTIET = binding.createSetNameEt;
        TextInputLayout descriptionTIL = binding.createSetDescriptionTil;
        binding.createSetAddDescription.setOnClickListener(v -> FrontendUtils.showItem(descriptionTIL));
        binding.createSetCancelBtn.setOnClickListener(v -> dialog.dismiss());
        binding.createSetConfirmBtn.setOnClickListener(v -> {
            TextInputEditText setNameEditText = binding.createSetNameEt;
            String setName = Objects.requireNonNull(setNameEditText.getText()).toString();
            TextView emptyNameError = binding.createSetEmptyNameError;

            if (TextUtils.isEmpty(setName)) {
                FrontendUtils.showItem(emptyNameError);
            } else {
                FrontendUtils.hideItem(emptyNameError);
                openEditSetActivity(dialog);
                dialog.dismiss();
            }
        });

        FrontendUtils.showDialog(dialog);
    }

    private void openEditSetActivity(Dialog dialog) {
        Intent intent = new Intent(this, ActivityEditLearningSet.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        FrontendUtils.showToast(this, "Here you can add vocabulary to your new set... In the future... With backend...");
    }

    private void dialogCreateNewFolder() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogCreateFolderBinding binding = DialogCreateFolderBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        TextInputEditText setNameTIET = binding.createFolderNameEt;
        TextInputLayout descriptionTIL = binding.createFolderDescriptionTil;

        binding.createFolderAddDescription.setOnClickListener(v -> FrontendUtils.showItem(descriptionTIL));
        binding.createFolderCancelBtn.setOnClickListener(v -> dialog.dismiss());
        binding.createFolderConfirmBtn.setOnClickListener(v -> {
            TextInputEditText setNameEditText = binding.createFolderNameEt;
            String setName = Objects.requireNonNull(setNameEditText.getText()).toString();
            TextView emptyNameError = binding.createFolderEmptyNameError;

            if (TextUtils.isEmpty(setName)) {
                FrontendUtils.showItem(emptyNameError);
            } else {
                FrontendUtils.hideItem(emptyNameError);
                openViewFolderActivity(dialog);
                dialog.dismiss();
            }
        });

        FrontendUtils.showDialog(dialog);
    }

    private void openViewFolderActivity(Dialog dialog) {
        Intent intent = new Intent(this, ActivityViewFolder.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        FrontendUtils.showToast(this, "Here you can view and manage your folder... In the future... With backend...");
    }

    private void dialogCreateNewGroup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogCreateGroupBinding binding = DialogCreateGroupBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        TextInputEditText setNameTIET = binding.createGroupNameEt;
        TextInputLayout descriptionTIL = binding.createGroupDescriptionTil;

        binding.createGroupAddDescription.setOnClickListener(v -> FrontendUtils.showItem(descriptionTIL));
        binding.createGroupCancelBtn.setOnClickListener(v -> dialog.dismiss());
        binding.createGroupConfirmBtn.setOnClickListener(v -> {
            TextInputEditText setNameEditText = binding.createGroupNameEt;
            String setName = Objects.requireNonNull(setNameEditText.getText()).toString();
            TextView emptyNameError = binding.createGroupEmptyNameError;

            if (TextUtils.isEmpty(setName)) {
                FrontendUtils.showItem(emptyNameError);
            } else {
                FrontendUtils.hideItem(emptyNameError);
                openViewGroupActivity(dialog);
                dialog.dismiss();
            }
        });

        FrontendUtils.showDialog(dialog);
    }

    private void openViewGroupActivity(Dialog dialog) {
        Intent intent = new Intent(this, ActivityViewGroup.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        FrontendUtils.showToast(this, "Here you can view and manage your folder... In the future... With backend...");
    }

    // Method to set icon tint for a specific item
    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor(int exceptionItemIndex) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(exceptionItemIndex);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    // Method to handle bottom navigation item selection
    private boolean onNavigationItemSelected(MenuItem item) {
        selectedFragmentId = item.getItemId(); // Update the selected fragment ID
        // Check which menu item was selected and replace the fragment accordingly
        if (selectedFragmentId == R.id.bottom_nav_home) {
            replaceFragment(new FragmentHome());
        } else if (selectedFragmentId == R.id.bottom_nav_library) {
            replaceFragment(new FragmentSetsDatabase());
        } else if (selectedFragmentId == R.id.bottom_nav_add_new) {
            showCreateNewDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new FragmentProfile());
        }
        // Return true to indicate that the item selection was handled
        return true;
    }


}
