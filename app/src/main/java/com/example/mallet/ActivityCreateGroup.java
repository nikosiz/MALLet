package com.example.mallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityCreateGroupBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ActivityCreateGroup extends AppCompatActivity {
    private ActivityCreateGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.createGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupListeners() {
        binding.createGroupSaveBtn.setOnClickListener(v -> saveGroup());
        binding.createGroupSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> allowOthersToManageGroup());
        binding.createGroupAddDescriptionLl.setOnClickListener(v -> addDescription());

    }

    private void allowOthersToManageGroup() {
        SwitchCompat allowOthers = binding.createGroupSwitch;

        // TODO: Is this correct?
        allowOthers.setChecked(true);
        FrontendUtils.showToast(this, "Other users will have the capability to manage this group. When we implement backend.");

        allowOthers.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // TODO: Add functionality, fix the switch
                FrontendUtils.showToast(this, "Other users will have the capability to manage this group. When we implement backend.");
            } else {
                // TODO: Add functionality, fix the switch
                FrontendUtils.showToast(this, "Other users WILL NOT have the capability to manage this group. When we implement backend.");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO
    private void addDescription() {
        TextInputLayout groupDescriptionTextInputLayout = binding.createGroupDescriptionTil;
        TextInputEditText groupDescriptionEditText = binding.createGroupDescriptionEt;
        View aboveDescriptionTil = binding.createGroupV1;
        String folderDescription = Objects.requireNonNull(groupDescriptionEditText.getText()).toString();

        FrontendUtils.showItem(groupDescriptionTextInputLayout);
        FrontendUtils.showItem(aboveDescriptionTil);

    }

    // TODO
    private void saveGroup() {
        TextInputEditText groupNameEditText = binding.createGroupNameEt;
        TextInputLayout createSetNameTil = binding.createGroupNameTil;
        TextView emptyError = binding.createGroupEmptyNameError;
        String groupName = Objects.requireNonNull(groupNameEditText.getText()).toString();

        if (TextUtils.isEmpty(groupName)) {
            FrontendUtils.showItem(emptyError);
        } else {
            FrontendUtils.hideitem(emptyError);
            FrontendUtils.showToast(this, "The group will be created... In the future... With backend...");
            finish();
        }
    }
}
