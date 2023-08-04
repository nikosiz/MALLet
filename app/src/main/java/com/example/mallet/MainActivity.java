package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mallet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Binding object to access views in the layout
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Replace the initial fragment with the HomeFragment
        replaceFragment(new HomeFragment());

        // Remove the background of the bottom navigation view to make it transparent
        //binding.bottomNavigationView.setBackground(null);

        // Set a listener for bottom navigation view item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // Check which menu item was selected and replace the fragment accordingly
            if (itemId == R.id.bottom_nav_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.bottom_nav_library) {
                replaceFragment(new LibraryFragment());
            } else if (itemId == R.id.bottom_nav_add_new) {
                replaceFragment(new AddNewFragment());
            } else if (itemId == R.id.bottom_nav_your_library) {
                replaceFragment(new YourFragment());
            } else if (itemId == R.id.bottom_nav_profile) {
                replaceFragment(new ProfileFragment());
            }

            // Return true to indicate that the item selection was handled
            return true;
        });

    }

    // Method to replace the fragment in the bottom navigation container
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the fragment in the main_activity_frame_layout container with the new fragment
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
