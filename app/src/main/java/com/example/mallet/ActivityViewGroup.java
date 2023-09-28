package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogAddFolderBinding;
import com.example.mallet.databinding.DialogAddMembersBinding;
import com.example.mallet.databinding.DialogAddSetBinding;
import com.example.mallet.databinding.DialogReportBinding;
import com.example.mallet.databinding.DialogViewGroupToolbarOptionsBinding;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
    }

    private void setupListeners() {
        binding.viewGroupToolbarOptionsIv.setOnClickListener(v -> groupOptionsDialog());
    }

    private void groupOptionsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_toolbar_options);
        DialogViewGroupToolbarOptionsBinding dialogBinding = DialogViewGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView addMembersTv = dialogBinding.viewGroupOptionsAddMembersTv;
        TextView addSetsTv = dialogBinding.viewGroupOptionsAddSetsTv;
        TextView addFoldersTv = dialogBinding.viewGroupOptionsAddFoldersTv;
        TextView leaveTv = dialogBinding.viewGroupOptionsLeaveTv;
        TextView reportTv = dialogBinding.viewGroupOptionsReportTv;

        addMembersTv.setOnClickListener(v -> {
            dialog.dismiss();
            addMembersDialog();
        });

        addSetsTv.setOnClickListener(v -> {
            dialog.dismiss();
            addSetsDialog();
        });

        addFoldersTv.setOnClickListener(v -> {
            dialog.dismiss();
            addFoldersDialog();
        });

        leaveTv.setOnClickListener(v -> {
            // TODO: leaveGroup()
            dialog.dismiss();
            leaveGroup();
        });

        reportTv.setOnClickListener(v -> {
            // TODO: ActivityReport
        });


    }

    private void addMembersDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_add_members);
        DialogAddMembersBinding dialogBinding = DialogAddMembersBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void addSetsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_add_set);
        DialogAddSetBinding dialogBinding = DialogAddSetBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void addFoldersDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_add_folder);
        DialogAddFolderBinding dialogBinding = DialogAddFolderBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void reportDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_report);
        DialogReportBinding dialogBinding = DialogReportBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private void leaveGroup() {
        // TODO
        Utils.showToast(this, "You left the group");
        finish();
    }

    private void getGroupData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        // Get the group name from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String groupName = intent.getStringExtra("group_name");
            String nrOfSets = intent.getStringExtra("group_sets");

            TextView groupNameTv = binding.viewGroupNameTv;
            TextView groupSetsTv = binding.viewGroupNrOfSetsTv;

            if (groupName != null) {
                groupNameTv.setText(groupName);
                groupSetsTv.setText(getString(R.string.string_sets, nrOfSets));
            }
        }
    }

}