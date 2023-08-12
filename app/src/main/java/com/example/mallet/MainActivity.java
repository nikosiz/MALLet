package com.example.mallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
//import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
//import android.widget.Toast;

import com.example.mallet.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import java.util.Objects;

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

        // Exception item index (0-based)
        int exceptionItemIndex = 2;
        setExceptionItemColor(exceptionItemIndex);

        // Set a listener for bottom navigation view item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // Check which menu item was selected and replace the fragment accordingly
            if (itemId == R.id.bottom_nav_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.bottom_nav_library) {
                replaceFragment(new LibraryFragment());
            } else if (itemId == R.id.bottom_nav_add_new) {
                showBottomDialog();
                //replaceFragment(new AddNewFragment());
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

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_sheet);

        LinearLayout createSet = dialog.findViewById(R.id.add_new_sheet_create_set);
        LinearLayout createFolder = dialog.findViewById(R.id.add_new_sheet_create_folder);
        LinearLayout createCollaboration = dialog.findViewById(R.id.add_new_sheet_create_collaboration);

        createSet.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateSetActivity.class);
            dialog.dismiss();
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Create new set", Toast.LENGTH_SHORT).show();
        });

        createFolder.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateFolderActivity.class);
            dialog.dismiss();
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Create new folder", Toast.LENGTH_SHORT).show();

        });

        createCollaboration.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateCollaborationActivity.class);
            dialog.dismiss();
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Create new collaboration", Toast.LENGTH_SHORT).show();

        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    // TODO: Naprawić
    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor(int exceptionItemIndex) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(exceptionItemIndex);
        int color = getResources().getColor(R.color.downriver_blue_300);
        //exceptionItem.setTextColor(color);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

}
