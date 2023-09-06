package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewFolderBinding;
import com.example.mallet.databinding.DialogDeleteFolderBinding;
import com.example.mallet.databinding.DialogFolderToolbarOptionsBinding;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivityViewFolder extends AppCompatActivity {

    private ActivityViewFolderBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupListeners();
        getFolderData();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewFolderToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void setupListeners() {
        binding.viewFolderOptionsBtn.setOnClickListener(v -> showOptions());
    }

    private void showOptions() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogFolderToolbarOptionsBinding binding = DialogFolderToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        TextView folderEditBtn = binding.folderToolbarOptionsEdit;
        TextView folderAddBtn = binding.folderToolbarOptionsAdd;
        TextView folderShareBtn = binding.folderToolbarOptionsShare;
        TextView folderDeleteBtn = binding.folderToolbarOptionsDelete;


        folderEditBtn.setOnClickListener(v -> editFolderName());

        folderAddBtn.setOnClickListener(v -> addSets());

        folderShareBtn.setOnClickListener(v -> {
            // TODO: Maybe share folder via link of some kind ?

        });

        folderDeleteBtn.setOnClickListener(v -> {
            dialog.dismiss();
            deleteFolderDialog();

        });

        Utils.showDialog(dialog);

    }

    private void deleteFolderDialog() {
        // TODO: Fix because it does not show the dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogDeleteFolderBinding binding = DialogDeleteFolderBinding.inflate(LayoutInflater.from(this));

        TextView cancelBtn = binding.deleteFolderCancelBtn;
        TextView confirmBtn = binding.deleteFolderConfirmBtn;
        CheckBox deleteFolderCheckBox = binding.deleteFolderCb;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyNameError = binding.deleteFolderEmptyError;
            TextView wrongNameError = binding.deleteFolderWrongPassError;
            TextView checkboxError = binding.deleteFolderCheckboxError;
            TextInputEditText nameEditText = binding.deleteFolderNameEt;

            String password = Objects.requireNonNull(nameEditText.getText()).toString();

            if (TextUtils.isEmpty(password) && !deleteFolderCheckBox.isChecked()) {
                emptyNameError.setVisibility(View.VISIBLE);
                checkboxError.setVisibility(View.VISIBLE);
                wrongNameError.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(password) && !deleteFolderCheckBox.isChecked()) {
                emptyNameError.setVisibility(View.GONE);
                checkboxError.setVisibility(View.VISIBLE);
                wrongNameError.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(password) && deleteFolderCheckBox.isChecked()) {
                emptyNameError.setVisibility(View.VISIBLE);
                checkboxError.setVisibility(View.GONE);
                wrongNameError.setVisibility(View.GONE);
            } else {
                // TODO: Add functionality for checking if the password is correct
                emptyNameError.setVisibility(View.GONE);
                checkboxError.setVisibility(View.GONE);
                Utils.showToast(this, "When we have backend, we will check if the name is correct. For now we just close this dialog");
                dialog.dismiss();
            }
        });

        dialog.show();
        //FrontendUtils.showDialog(dialog);
    }

    private void addSets() {
    }

    private void editFolderName() {
    }

    private void getFolderData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the folder name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String folderName = intent.getStringExtra("folder_name");
            String folderCreator = intent.getStringExtra("folder_creator");
            String folderSets = intent.getStringExtra("folder_sets");

            TextView folderNameTV = binding.viewFolderName;
            TextView folderCreatorTV = binding.viewFolderCreator;
            TextView folderSetsTV = binding.viewFolderSets;

            if (folderName != null) {
                folderNameTV.setText(folderName);
                folderCreatorTV.setText(folderCreator);
                folderSetsTV.setText(folderSets + " sets");
            }
        }
    }
}
