package com.example.mallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mallet.databinding.ActivityMainBinding;
import com.example.mallet.databinding.DialogCreateBinding;
import com.example.mallet.databinding.DialogCreateFolderBinding;
import com.example.mallet.databinding.DialogCreateGroupBinding;
import com.example.mallet.databinding.DialogCreateSetBinding;
import com.example.mallet.utils.FlashcardManager;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int clickCount = 0;
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
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

    private void setupContents() {
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private void initializeSelectedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            setSelectedFragment(selectedFragmentId);
        } else {
            replaceFragment(new ActivityMain_FragmentHome());
        }
    }

    private void setSelectedFragment(int fragmentId) {
        binding.bottomNavigationView.setSelectedItemId(fragmentId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_FRAGMENT_KEY, selectedFragmentId);
        super.onSaveInstanceState(outState);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(2);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        selectedFragmentId = item.getItemId();

        if (selectedFragmentId == R.id.bottom_nav_home) {
            replaceFragment(new ActivityMain_FragmentHome());
        } else if (selectedFragmentId == R.id.bottom_nav_library) {
            replaceFragment(new ActivityMain_FragmentSetsDatabase());
        } else if (selectedFragmentId == R.id.bottom_nav_add_new) {
            createNewDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new ActivityMain_FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new ActivityMain_FragmentProfile());
        }
        return true;
    }

    private void createNewDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_create);
        DialogCreateBinding dialogBinding = DialogCreateBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        TextView createSet = dialogBinding.createNewCreateSet;
        TextView createFolder = dialogBinding.createNewCreateFolder;
        TextView createGroup = dialogBinding.createNewCreateGroup;

        createSet.setOnClickListener(v -> {
            dialog.dismiss();
            createSetDialog().show();
        });
        createFolder.setOnClickListener(v -> {
            dialog.dismiss();
            createFolderDialog();
        });
        createGroup.setOnClickListener(v -> {
            dialog.dismiss();
            createGroupDialog();
        });

        dialog.show();
    }

    private Dialog createSetDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_create_set);
        DialogCreateSetBinding dialogBinding = DialogCreateSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        EditText nameEt = dialogBinding.createSetNameEt;
        TextView nameErrTv = dialogBinding.createSetErrorTv;

        TextView addDescriptionBtn = dialogBinding.createSetAddDescription;
        View aboveDescription = dialogBinding.createSetAboveDescriptionV;
        TextInputLayout descriptionTil = dialogBinding.createSetDescriptionTil;
        EditText descriptionEt = dialogBinding.createSetDescriptionEt;

        TextView cancelBtn = dialogBinding.createSetCancelBtn;
        cancelBtn.setVisibility(View.VISIBLE);

        TextView confirmBtn = dialogBinding.createSetConfirmBtn;
        confirmBtn.setVisibility(View.VISIBLE);

        Utils.showItems(nameEt, addDescriptionBtn, cancelBtn, confirmBtn);
        Utils.hideItems(aboveDescription, descriptionTil);

        Utils.setupTextWatcher(nameEt, nameErrTv, Pattern.compile("^.*$"), "Please, enter a valid set name");

        addDescriptionBtn.setOnClickListener(v -> {
            clickCount++;
            if (clickCount == 1) {
                Utils.showItems(addDescriptionBtn, aboveDescription, descriptionTil);
            } else if (clickCount == 2) {
                Utils.hideItems(aboveDescription, descriptionTil);
                clickCount = 0;
            }
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            String setName = Objects.requireNonNull(nameEt.getText()).toString().trim();
            String setDescription = Objects.requireNonNull(descriptionEt.getText()).toString().trim();

            if (setName.isEmpty()) {
                Utils.showItem(nameErrTv);
                nameErrTv.setText("This field cannot be empty");
                return;
            }

            createNewSet(setName, setDescription);
            dialog.dismiss();
        });

        return dialog;
    }

    private void createNewSet(String setName, String setDescription) {
        Utils.showToast(this, "The set will be created in the future with backend. Now we will just open the \"ActivityEditLearningSet\"");
        Intent intent = new Intent(this, ActivityEditLearningSet.class);

        String enteredSetDescription;

        intent.putExtra("learningSetName", setName);

        if (setDescription == null) {
            setDescription = "";
            intent.putExtra("learningSetDescription", setDescription);
        } else {
            enteredSetDescription = setDescription;
            intent.putExtra("learningSetDescription", enteredSetDescription);
        }

        startActivity(intent);
    }

    private void createFolderDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_create_folder);
        DialogCreateFolderBinding dialogBinding = DialogCreateFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    private void createGroupDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_create_group);
        DialogCreateGroupBinding dialogBinding = DialogCreateGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    public List<ModelLearningSet> createSetList() {
        List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set1 = FlashcardManager.readFlashcards(getApplicationContext(), "fruit.txt");
        ModelLearningSet set2 = FlashcardManager.readFlashcards(getApplicationContext(), "animals.txt");
        ModelLearningSet set3 = FlashcardManager.readFlashcards(getApplicationContext(), "numbers.txt");
        ModelLearningSet set4 = FlashcardManager.readFlashcards(getApplicationContext(), "countries.txt");
        ModelLearningSet set5 = FlashcardManager.readFlashcards(getApplicationContext(), "colors.txt");

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
        groups.add(new ModelGroup("Group #1", "2"));
        groups.add(new ModelGroup("Group #2", "5"));
        groups.add(new ModelGroup("Group #3", "2"));
        groups.add(new ModelGroup("Group #4", "8"));
        groups.add(new ModelGroup("Group #5", "5"));
        return groups;
    }

    @Override
    public void onBackPressed() {
        Utils.terminateApp(this);
    }
}
