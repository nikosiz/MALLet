package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogGroupToolbarOptionsBinding;
import com.example.mallet.utils.Utils;

import java.util.Objects;

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

    private void setupToolbar() {
        Toolbar toolbar = binding.viewGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(""); // Set the title to an empty string
    }

    private void setupListeners() {
        binding.viewGroupToolbarOptionsIv.setOnClickListener(v -> showOptions());
    }

    private void showOptions() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogGroupToolbarOptionsBinding binding = DialogGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());

        TextView inviteMembersBtn = binding.groupToolbarOptionsInvite;
        TextView manageGroupBtn = binding.groupToolbarOptionsManage;
        TextView leaveGroupBtn = binding.groupToolbarOptionsLeave;
        TextView reportGroupBtn = binding.groupToolbarOptionsReport;


        inviteMembersBtn.setOnClickListener(v -> {
            // TODO: Maybe invite members via link of some kind ?
        });

        manageGroupBtn.setOnClickListener(v -> {
            // TODO: ActivityManageGroup
        });

        leaveGroupBtn.setOnClickListener(v -> {
            // TODO: leaveGroup()
            dialog.dismiss();
            leaveGroup();
        });

        reportGroupBtn.setOnClickListener(v -> {
            // TODO: ActivityReport
        });

        dialog.show();

    }

    private void leaveGroup() {
        // TODO
        Utils.showToast(this, "You left the group");

    }

    private void getGroupData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the group name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String groupName = intent.getStringExtra("group_name");
            String groupSets = intent.getStringExtra("group_sets");

            TextView groupNameTV = binding.groupManagementName;
            TextView groupSetsTV = binding.groupManagementSets;

            if (groupName != null) {
                groupNameTV.setText(groupName);
                groupSetsTV.setText(groupSets + " sets");
            }
        }
    }

}