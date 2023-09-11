// TODO:
//  1. After clicking "add new", the selection of the previous item disappears
//  2. Fix all @SuppressLint("RestrictedApi")s

package com.example.mallet;

import static com.example.mallet.utils.Utils.createDialog;

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
import com.example.mallet.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityMain extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int clickCount = 0;
    private static final String SELECTED_FRAGMENT_KEY = "selected_fragment";
    private int selectedFragmentId = R.id.bottom_nav_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Use your layout file here

        initView();

        if (savedInstanceState != null) {
            selectedFragmentId = savedInstanceState.getInt(SELECTED_FRAGMENT_KEY, R.id.bottom_nav_home);
            setSelectedFragment(selectedFragmentId);
        } else {
            replaceFragment(new FragmentHome());
        }

        int exceptionItemIndex = 2;
        setExceptionItemColor(exceptionItemIndex);
        setBottomNavigationViewListener();
    }

    private void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setSelectedFragment(int fragmentId) {
        binding.bottomNavigationView.setSelectedItemId(fragmentId);
    }

    private void setBottomNavigationViewListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
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
    private void setExceptionItemColor(int exceptionItemIndex) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView exceptionItem = (BottomNavigationItemView) menuView.getChildAt(exceptionItemIndex);
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
            createNewDialog();
        } else if (selectedFragmentId == R.id.bottom_nav_your_library) {
            replaceFragment(new FragmentUserLibrary());
        } else if (selectedFragmentId == R.id.bottom_nav_profile) {
            replaceFragment(new FragmentProfile());
        }

        return true;
    }

    // You can add your dialog creation and activity opening methods here as needed.

    @Override
    public void onBackPressed() {
        Utils.terminateApp(this);
    }

    private void createNewDialog() {
        final Dialog dialog = Utils.createDialog(this, R.layout.dialog_create);
        DialogCreateBinding dialogBinding = DialogCreateBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        //dialog.show();;

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
        final Dialog dialog = createDialog(this, R.layout.dialog_create_set);

        DialogCreateSetBinding dialogBinding = DialogCreateSetBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(dialogBinding.getRoot());

        EditText nameEt = dialogBinding.createSetNameEt;

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

        addDescriptionBtn.setOnClickListener(v -> {
            clickCount++; // Increment the click count on each click

            if (clickCount == 1) {
                Utils.showItems(addDescriptionBtn, aboveDescription, descriptionTil);
            } else if (clickCount == 2) {
                Utils.hideItems(aboveDescription, descriptionTil);
                clickCount = 0; // Reset the click count after the second click
            }
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            String setName = nameEt.getText().toString().trim();
            String setDescription = descriptionEt.getText().toString().trim();
            if (!setName.isEmpty()) {
                createNewSet(setName, setDescription);
                dialog.dismiss();
            } else {
                Utils.showToast(this, "Please, enter a valid set name");
            }
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
        final Dialog dialog = createDialog(this, R.layout.dialog_create_folder);

        DialogCreateFolderBinding dialogBinding = DialogCreateFolderBinding.inflate(LayoutInflater.from(this));

        dialog.show();

    }

    private void createGroupDialog() {
        final Dialog dialog = createDialog(this, R.layout.dialog_create_group);

        DialogCreateGroupBinding dialogBinding = DialogCreateGroupBinding.inflate(LayoutInflater.from(this));

        dialog.show();
    }
}
