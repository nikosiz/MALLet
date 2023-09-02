package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogGroupToolbarOptionsBinding;

public class ActivityViewGroup extends AppCompatActivity {

    private ActivityViewGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupListeners();
        getGroupData();
    }

    private void setupListeners() {
        binding.groupManagementOptionsBtn.setOnClickListener(v -> showOptions());
    }

    private void showOptions() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogGroupToolbarOptionsBinding binding = DialogGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        LinearLayout inviteMembersBtn = binding.groupToolbarOptionsInviteLl;
        LinearLayout manageGroupBtn = binding.groupToolbarManageContentsLl;
        LinearLayout leaveGroupBtn = binding.groupToolbarOptionsLeaveLl;
        LinearLayout reportGroupBtn = binding.groupToolbarOptionsReportLl;


        inviteMembersBtn.setOnClickListener(v -> {
            // TODO: Maybe invite members via link of some kind ?
        });

        manageGroupBtn.setOnClickListener(v -> {
            // TODO: ActivityManageGroup
        });

        leaveGroupBtn.setOnClickListener(v -> {
            // TDDO: leaveGroup()
            dialog.dismiss();
            leaveGroup();
        });

        reportGroupBtn.setOnClickListener(v -> {
            // TODO: ActivityReport
        });

        FrontendUtils.showDialog(dialog);

    }

    private void leaveGroup() {
        // TODO
        FrontendUtils.showToast(this, "You left the group");

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.group_management_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); // Set the title to an empty string

        // Display back arrow on the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getGroupData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the group name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String groupName = intent.getStringExtra("group_name");
            String groupSets = intent.getStringExtra("group_sets");

            TextView groupNameTV = findViewById(R.id.group_management_name);
            TextView groupSetsTV = findViewById(R.id.group_management_sets);

            if (groupName != null) {
                groupNameTV.setText(groupName);
                groupSetsTV.setText(groupSets + " sets");
            }
        }
    }

    /*private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }*/
}