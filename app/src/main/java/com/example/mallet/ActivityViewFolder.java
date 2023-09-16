package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewFolderBinding;
import com.example.mallet.databinding.DialogDeleteFolderBinding;
import com.example.mallet.databinding.DialogViewFolderToolbarOptionsBinding;
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
    }

    private void setupListeners() {
        binding.viewFolderToolbarOptionsIv.setOnClickListener(v -> showOptions());
    }

    private void showOptions() {
        final Dialog dialog = createDialog(R.layout.dialog_view_folder_toolbar_options);
        DialogViewFolderToolbarOptionsBinding dialogBinding = DialogViewFolderToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());

        TextView editTv = dialogBinding.viewFolderEditTv;
        TextView addSetsTv = dialogBinding.viewFolderAddSetsTv;
        TextView shareWGroupTv = dialogBinding.viewFolderAddToGroupTv;
        TextView deleteTv = dialogBinding.viewFolderDeleteTv;


        editTv.setOnClickListener(v -> editFolderDialog());

        addSetsTv.setOnClickListener(v -> addSetsDialog());

        shareWGroupTv.setOnClickListener(v -> {
            // TODO: Share folder via link of some kind / share all sets in a folder with a group 
            shareFolderDialog();
        });

        deleteTv.setOnClickListener(v -> {
            dialog.dismiss();
            deleteFolderDialog();
        });

        dialog.show();

    }

    private void shareFolderDialog() {
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
            TextView emptyNameErr = binding.deleteFolderEmptyError;
            TextView wrongNameErr = binding.deleteFolderWrongPassError;
            TextView checkboxErr = binding.deleteFolderCheckboxError;
            TextInputEditText nameEditText = binding.deleteFolderNameEt;

            String password = Objects.requireNonNull(nameEditText.getText()).toString();

            if (TextUtils.isEmpty(password) && !deleteFolderCheckBox.isChecked()) {
                emptyNameErr.setVisibility(View.VISIBLE);
                checkboxErr.setVisibility(View.VISIBLE);
                wrongNameErr.setVisibility(View.GONE);
            } else if (!TextUtils.isEmpty(password) && !deleteFolderCheckBox.isChecked()) {
                emptyNameErr.setVisibility(View.GONE);
                checkboxErr.setVisibility(View.VISIBLE);
                wrongNameErr.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(password) && deleteFolderCheckBox.isChecked()) {
                emptyNameErr.setVisibility(View.VISIBLE);
                checkboxErr.setVisibility(View.GONE);
                wrongNameErr.setVisibility(View.GONE);
            } else {
                // TODO: Add functionality for checking if the password is correct
                emptyNameErr.setVisibility(View.GONE);
                checkboxErr.setVisibility(View.GONE);
                Utils.showToast(this, "When we have backend, we will check if the name is correct. For now we just close this dialog");
                dialog.dismiss();
            }
        });

        dialog.show();
        //dialog.show();;
    }

    private void addSetsDialog() {
    }

    private void editFolderDialog() {
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private void getFolderData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the folder name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String folderName = intent.getStringExtra("folder_name");
            String folderCreator = intent.getStringExtra("folder_creator");
            String folderSets = intent.getStringExtra("folder_sets");

            TextView folderNameTV = binding.viewFolderNameTv;
            TextView folderCreatorTV = binding.viewFolderCreatorTv;
            TextView folderSetsTV = binding.viewFolderNumberOfSetsTv;

            if (folderName != null) {
                folderNameTV.setText(folderName);
                folderCreatorTV.setText(folderCreator);
                folderSetsTV.setText(folderSets + " sets");
            }
        }
    }
}
