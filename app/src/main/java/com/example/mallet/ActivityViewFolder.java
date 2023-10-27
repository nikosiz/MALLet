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

        TextView folderNameTv = binding.viewFolderNameTv;
        TextView folderCreatorTv = binding.viewFolderCreatorTv;
        TextView folderNrOfSets = binding.viewFolderNrOfSetsTv;
        View aboveDescription = binding.viewFolderAboveDescriptionV;
        TextView folderDescriptionTv = binding.viewFolderDescriptionTv;
        Utils.hideItems(aboveDescription);
        Utils.hideItems(folderDescriptionTv);

        setupToolbar();
        setupContents();

        folder = getIntent().getParcelableExtra("folder");

        if (folder != null) {
            folderNameTv.setText(folder.getFolderName());
            folderCreatorTv.setText(folder.getFolderCreator());
            folderNrOfSets.setText(folder.getSets());
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
        final Dialog dialog = createDialog(R.layout.dialog_view_folder_toolbar_options);
        DialogViewFolderToolbarOptionsBinding dialogBinding = DialogViewFolderToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView editTv = dialogBinding.viewFolderOptionsEditTv;
        TextView addSetsTv = dialogBinding.viewFolderOptionsAddSetsTv;
        TextView shareWGroupTv = dialogBinding.viewFolderOptionsAddToGroupTv;
        TextView deleteTv = dialogBinding.viewFolderOptionsDeleteTv;


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
    }

    private void shareFolderDialog() {
    }

    private void deleteFolderDialog() {
        // TODO: Fix because it does not show the dialog
        Dialog dialog = createDialog(R.layout.dialog_delete_folder);
        DialogDeleteFolderBinding binding = DialogDeleteFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

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

            TextView folderNameTv = binding.viewFolderNameTv;
            TextView folderCreatorTv = binding.viewFolderCreatorTv;
            TextView folderSetsTv = binding.viewFolderNrOfSetsTv;

            if (folderName != null) {
                folderNameTv.setText(folderName);
                folderCreatorTv.setText(folderCreator);
                String nrOfSets = folder.getSets();
                folderSetsTv.setText(getString(R.string.string_sets, nrOfSets));
            }
        }
    }
}
