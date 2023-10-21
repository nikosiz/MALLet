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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogReportBinding;
import com.example.mallet.databinding.DialogViewGroupManageMembersBinding;
import com.example.mallet.databinding.DialogViewGroupManageSetsBinding;
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

        binding.viewGroupToolbarOptionsIv.setOnClickListener(v -> dialogGroupOptions());

        binding.viewGroupToolbarBackIv.setOnClickListener(v -> finish());
    }

    private void setupListeners() {

    }

    private void dialogGroupOptions() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_toolbar_options);
        DialogViewGroupToolbarOptionsBinding dialogBinding = DialogViewGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView backIv = dialogBinding.viewGroupOptionsBackIv;
        TextView manageMembersTv = dialogBinding.viewGroupOptionsManageMembersTv;
        TextView manageSetsTv = dialogBinding.viewGroupOptionsManageSetsTv;
        TextView leaveTv = dialogBinding.viewGroupOptionsLeaveTv;
        TextView reportTv = dialogBinding.viewGroupOptionsReportTv;

        backIv.setOnClickListener(v -> dialog.dismiss());

        manageMembersTv.setOnClickListener(v -> {
            dialog.dismiss();
            manageMembersDialog();
        });

        manageSetsTv.setOnClickListener(v -> {
            dialog.dismiss();
            manageSetsDialog();
        });

        leaveTv.setOnClickListener(v -> {
            // TODO: leaveGroup()
            dialog.dismiss();
            leaveGroup();
        });

        reportTv.setOnClickListener(v -> {
            dialog.dismiss();
            reportDialog();
            // TODO: ActivityReport
        });
    }

    private void manageMembersDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_manage_members);
        DialogViewGroupManageMembersBinding dialogBinding = DialogViewGroupManageMembersBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();
    }

    private void manageSetsDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_view_group_manage_sets);
        DialogViewGroupManageSetsBinding dialogBinding = DialogViewGroupManageSetsBinding.inflate(LayoutInflater.from(this));
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