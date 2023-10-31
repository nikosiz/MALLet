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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewFolderBinding;
import com.example.mallet.databinding.DialogDeleteFolderBinding;
import com.example.mallet.databinding.DialogViewFolderToolbarOptionsBinding;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivityViewFolder extends AppCompatActivity {
    private ActivityViewFolderBinding binding;
    private String folderName, folderDescription;
    private ModelFolder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView folderNameTv = binding.viewFolderToolbarNameTv;

        setupToolbar();
        setupContents();

        folder = getIntent().getParcelableExtra("folder");

        if (folder != null) {
            folderNameTv.setText(folder.getFolderName());
            //folderDescriptionTv.setText(folder.getFolderDescription());
        }

        /*getFolderData();

        folderName = getIntent().getStringExtra("folderName");
        folderDescription = getIntent().getStringExtra("folderDescription");

        if (folderName != null) {
            folderNameTv.setText(folderName);
        }
        if (folderDescription != null && !folderDescription.equals("")) {
            Utils.showItems(aboveDescription, folderDescriptionTv);
            folderDescriptionTv.setText(folderDescription);
        }*/

    }

    private void setupContents() {
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewFolderToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.viewFolderToolbarBackIv.setOnClickListener(v -> finish());

        binding.viewFolderToolbarOptionsIv.setOnClickListener(v -> folderOptionsDialog());
    }

    private void folderOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_folder_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogViewFolderToolbarOptionsBinding dialogBinding = DialogViewFolderToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        dialog.show();

        TextView editTv = dialogBinding.viewFolderOptionsEditTv;
        TextView addSetsTv = dialogBinding.viewFolderOptionsAddSetsTv;
        TextView deleteTv = dialogBinding.viewFolderOptionsDeleteTv;


        editTv.setOnClickListener(v -> editFolderDialog());

        addSetsTv.setOnClickListener(v -> addSetsDialog());

        deleteTv.setOnClickListener(v -> {
            dialog.dismiss();
            deleteFolderDialog();
        });
    }

    private void deleteFolderDialog() {
        // TODO: Fix because it does not show the dialog
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_folder, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteFolderBinding dialogBinding = DialogDeleteFolderBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelBtn = dialogBinding.deleteFolderCancelBtn;
        TextView confirmBtn = dialogBinding.deleteFolderConfirmBtn;
        CheckBox deleteFolderCheckBox = dialogBinding.deleteFolderCb;

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        confirmBtn.setOnClickListener(v -> {
            TextView emptyNameErr = dialogBinding.deleteFolderEmptyError;
            TextView wrongNameErr = dialogBinding.deleteFolderWrongPassError;
            TextView checkboxErr = dialogBinding.deleteFolderCheckboxError;
            TextInputEditText nameEditText = dialogBinding.deleteFolderNameEt;

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

            TextView folderNameTv = binding.viewFolderToolbarNameTv;

            if (folderName != null) {
                folderNameTv.setText(folderName);
            }
        }
    }
}
