package com.example.mallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private EditText folderNameEt, folderDescriptionEt;
    private SharedPreferences sharedPreferences;
    private int savedTheme;

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
        binding.mainBottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private void initializeSelectedFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            setSelectedFragment(selectedFragmentId);
        } else {
            replaceFragment(new FragmentHome());
        }
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

    @SuppressLint("RestrictedApi")
    private void setExceptionItemColor() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.mainBottomNavigation.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(2);
        int color = ContextCompat.getColor(this, R.color.downriver_blue_300);
        exceptionItem.setIconTintList(ColorStateList.valueOf(color));
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        selectedFragmentId = item.getItemId();

        if (selectedFragmentId == R.id.bottom_nav_home) {
            replaceFragment(new FragmentHome());
        } else if (selectedFragmentId == R.id.bottom_nav_library) {
            replaceFragment(new FragmentSetsDatabase());
        } else if (selectedFragmentId == R.id.bottom_nav_add_new) {
            newSetFolderGroupDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new FragmentProfile());
        }
        return true;
    }

    private void newSetFolderGroupDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_create);
        DialogCreateBinding dialogBinding = DialogCreateBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView createSet = dialogBinding.createSetTv;
        TextView createFolder = dialogBinding.createFolderTv;
        TextView createGroup = dialogBinding.createGroupTv;

        createSet.setOnClickListener(v -> {
            dialog.dismiss();
            createSetDialog();
        });
        createFolder.setOnClickListener(v -> {
            dialog.dismiss();
            createFolderDialog();
        });
        createGroup.setOnClickListener(v -> {
            dialog.dismiss();
            createGroupDialog();
        });
    }

    private void createSetDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_create_set);
        DialogCreateSetBinding dialogBinding = DialogCreateSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

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
                Utils.showItems(nameErrTv);
                nameErrTv.setText(getString(R.string.field_cannot_be_empty));
                return;
            }

            createNewSet(setName, setDescription);
            dialog.dismiss();
        });
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
        final Dialog dialog = createDialog(R.layout.dialog_create_folder);
        DialogCreateFolderBinding dialogBinding = DialogCreateFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        folderNameEt = dialogBinding.createFolderNameEt;
        TextView nameErrTv = dialogBinding.createFolderEmptyNameError;

        TextView addDescriptionTv = dialogBinding.createFolderAddDescription;
        View aboveDescription = dialogBinding.createFolderAboveDescriptionV;
        TextInputLayout descriptionTil = dialogBinding.createFolderDescriptionTil;
        folderDescriptionEt = dialogBinding.createFolderDescriptionEt;

        TextView cancelBtn = dialogBinding.createFolderCancelBtn;
        cancelBtn.setVisibility(View.VISIBLE);

        TextView confirmBtn = dialogBinding.createFolderConfirmBtn;
        confirmBtn.setVisibility(View.VISIBLE);

        Utils.showItems(folderNameEt, addDescriptionTv, cancelBtn, confirmBtn);
        Utils.hideItems(aboveDescription, descriptionTil);

        Utils.setupTextWatcher(folderNameEt, nameErrTv, Pattern.compile("^.*$"), "Please, enter a valid set name");

        addDescriptionTv.setOnClickListener(v -> {
            clickCount++;
            if (clickCount == 1) {
                Utils.showItems(addDescriptionTv, aboveDescription, descriptionTil);
            } else if (clickCount == 2) {
                Utils.hideItems(aboveDescription, descriptionTil);
                clickCount = 0;
            }
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            String folderName = Objects.requireNonNull(folderNameEt.getText()).toString().trim();
            String folderDescription = Objects.requireNonNull(folderDescriptionEt.getText()).toString().trim();
            // TODO: fix
            String folderCreator = "";
            String folderSets = "";
            ModelFolder folder = new ModelFolder(folderName, folderCreator, folderSets);


            if (folderName.isEmpty()) {
                Utils.showItems(nameErrTv);
                nameErrTv.setText(getString(R.string.field_cannot_be_empty));
                return;
            }

            createNewFolder(folder, folderDescription);
            dialog.dismiss();
        });
    }

    private void createNewFolder(ModelFolder folder, String folderDescription) {
        Utils.showToast(this, "The folder will be created in the future with backend. Now we will just open the \"ActivityViewFolder\"");
        Intent intent = new Intent(this, ActivityViewFolder.class);

        String enteredFolderDescription;

        intent.putExtra("folder", folder);

        if (folderDescription == null) {
            folderDescription = "";
            intent.putExtra("folderDescription", folderDescription);
        } else {
            enteredFolderDescription = folderDescription;
            intent.putExtra("folderDescription", enteredFolderDescription);
        }

        startActivity(intent);
    }

    private void createGroupDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_create_group);
        DialogCreateGroupBinding dialogBinding = DialogCreateGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    public List<ModelLearningSet> createSetList() {
        List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set1 = FlashcardManager.readFlashcards(getApplicationContext(), "fruit.txt");
        ModelLearningSet set2 = FlashcardManager.readFlashcards(getApplicationContext(), "animals.txt");
        ModelLearningSet set3 = FlashcardManager.readFlashcards(getApplicationContext(), "nrs.txt");
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
        groups.add(new ModelGroup("Group #1", "2", "3"));
        groups.add(new ModelGroup("Group #2", "5", "3"));
        groups.add(new ModelGroup("Group #3", "2", "3"));
        groups.add(new ModelGroup("Group #4", "8", "3"));
        groups.add(new ModelGroup("Group #5", "5", "3"));
        return groups;
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
}