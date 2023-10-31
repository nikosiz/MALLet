package com.example.mallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mallet.databinding.ActivityMainBinding;
import com.example.mallet.databinding.DialogCreateBinding;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityMain extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
    private BottomNavigationMenuView bottomNavMenu;
    private int selectedFragmentId = R.id.bottom_nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeSelectedFragment(savedInstanceState);
        setExceptionItemColor();

        setupContents();
    }

    private void initializeSelectedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            setSelectedFragment(selectedFragmentId);
        } else {
            replaceFragment(new FragmentHome());
        }
    }

    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor() {
        BottomNavigationMenuView bottomNavMenu = (BottomNavigationMenuView) binding.mainBottomNavigation.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) bottomNavMenu.getChildAt(2);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    private void setupContents() {
        binding.mainBottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private void setSelectedFragment(int fragmentId) {
        binding.mainBottomNavigation.setSelectedItemId(fragmentId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentId);
        super.onSaveInstanceState(outState);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFl, fragment);
        fragmentTransaction.commit();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        selectedFragmentId = item.getItemId();

        if (selectedFragmentId == R.id.bottom_nav_home) {
            replaceFragment(new FragmentHome());
        } else if (selectedFragmentId == R.id.bottom_nav_library) {
            replaceFragment(new FragmentSetsDatabase());
        } else if (selectedFragmentId == R.id.bottom_nav_add_new) {
            createNewSetFolderGroupDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new FragmentProfile());
        }
        return true;
    }

    private void createNewSetFolderGroupDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_create);
        DialogCreateBinding dialogBinding = DialogCreateBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView createSet = dialogBinding.createSetTv;
        TextView createFolder = dialogBinding.createFolderTv;
        TextView createGroup = dialogBinding.createGroupTv;

        createSet.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityEditLearningSet.class);
            startActivity(intent);
        });

        createFolder.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityCreateGroup.class);
            startActivity(intent);
        });
        createGroup.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, ActivityCreateFolder.class);
            startActivity(intent);
        });
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        return dialog;
    }

    @Override
    public void onBackPressed() {
        Utils.terminateApp(this);
    }

    // TODO
    public List<ModelLearningSet> createSetList() {
        List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set1 = Utils.readFlashcards(getApplicationContext(), "fruit.txt");
        ModelLearningSet set2 = Utils.readFlashcards(getApplicationContext(), "animals.txt");
        ModelLearningSet set3 = Utils.readFlashcards(getApplicationContext(), "nrs.txt");
        ModelLearningSet set4 = Utils.readFlashcards(getApplicationContext(), "countries.txt");
        ModelLearningSet set5 = Utils.readFlashcards(getApplicationContext(), "colors.txt");

        sets.add(set1);
        sets.add(set2);
        sets.add(set3);
        sets.add(set4);
        sets.add(set5);

        return sets;
    }

    public List<ModelFolder> createFolderList() {
        List<ModelFolder> folders = new ArrayList<>();
        folders.add(new ModelFolder("Folder #1", "user123", "3"));
        folders.add(new ModelFolder("Folder #2", "user123", "7"));
        folders.add(new ModelFolder("Folder #3", "user123", "2"));
        folders.add(new ModelFolder("Folder #4", "user123", "8"));
        folders.add(new ModelFolder("Folder #5", "user123", "1"));
        return folders;
    }

    public List<ModelGroup> createGroupList() {
        List<ModelGroup> groups = new ArrayList<>();
        groups.add(new ModelGroup("Group #1", "2", "3"));
        groups.add(new ModelGroup("Group #2", "5", "3"));
        groups.add(new ModelGroup("Group #3", "2", "3"));
        groups.add(new ModelGroup("Group #4", "8", "3"));
        groups.add(new ModelGroup("Group #5", "5", "3"));
        return groups;
    }
}