// TODO:
//  1. After clicking "add new", the selection of the previous item disappears
//  2. Fix all @SuppressLint("RestrictedApi")s

package com.example.mallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mallet.databinding.ActivityMainBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
    private int selectedFragmentId = R.id.bottom_nav_home;
    private final boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass onCreate method

        initView(); // Initialize the view components

        // Restore selected fragment state
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home); // Get the selected fragment ID from saved state
            setSelectedFragment(selectedFragmentId); // Set the selected fragment based on saved state
        } else {
            replaceFragment(new FragmentHome()); // Replace the fragment with FragmentHome if there's no saved state
        }

        int exceptionItemIndex = 2; // Specify the index for the exception item
        setExceptionItemColor(exceptionItemIndex); // Set the color for an exception item

        setBottomNavigationViewListener(); // Set a listener for bottom navigation view item selection
    }

    // Initialize the view components
    private void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // Set the selected fragment based on the given fragment ID
    private void setSelectedFragment(int fragmentId) {
        binding.bottomNavigationView.setSelectedItemId(fragmentId);
    }

    // Set a listener for bottom navigation view item selection
    private void setBottomNavigationViewListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    // Save the selected fragment ID in the savedInstanceState bundle
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentId);
        super.onSaveInstanceState(outState);
    }

    // Replace the current fragment with the given fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Show a dialog to create a new item
    private void showCreateNewDialog() {
        // Implementation for showing the create new dialog
    }

    // Set the color of the exception item in the bottom navigation view
    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor(int exceptionItemIndex) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(exceptionItemIndex);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    // Handle bottom navigation view item selection
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

        return true;
    }

    // TODO
    // Implementation for creating a new set via a dialog
    private void dialogCreateNewSet() {
        // Implementation for creating a new set
    }

    // Implementation for opening the edit set activity
    private void openEditSetActivity(Dialog dialog) {
        // Implementation for opening the edit set activity
    }

    // Implementation for creating a new folder via a dialog
    private void dialogCreateNewFolder() {
        // Implementation for creating a new folder
    }

    // Implementation for opening the view folder activity
    private void openViewFolderActivity(Dialog dialog) {
        // Implementation for opening the view folder activity
    }

    // Implementation for creating a new group via a dialog
    private void dialogCreateNewGroup() {
        // Implementation for creating a new group
    }

    // Implementation for opening the view group activity
    private void openViewGroupActivity(Dialog dialog) {
        // Implementation for opening the view group activity
    }

    @Override
    public void onBackPressed() {
        Utils.terminateApp(this);
    }

}
