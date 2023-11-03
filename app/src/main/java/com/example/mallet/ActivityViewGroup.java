package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogAddSetToGroupBinding;
import com.example.mallet.databinding.DialogAddUserToGroupBinding;
import com.example.mallet.databinding.DialogReportBinding;
import com.example.mallet.databinding.DialogViewGroupToolbarOptionsBinding;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivityViewGroup extends AppCompatActivity {
    private ActivityViewGroupBinding binding;
    private ModelGroup group;

    // Toolbar
    private ImageView backIv;
    private TextView groupNameTv;
    private ImageView optionsIv;

    // Contents
    private TabLayout tabLayout;
    private ViewPager2 viewPager;


    // Floating Action Button & its options
    private FloatingActionButton addFab;
    private TextView addSetTv;
    private TextView addUserTv;
    private static boolean areFabOptionsVisible = false;
    private LinearLayout fabOptionsLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo get groupId and fetch resources if paramter is passed
        super.onCreate(savedInstanceState);
        binding = ActivityViewGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupContents();

        getGroupData();
    }

    private void setupContents() {
        group = getIntent().getParcelableExtra("group");

        setupToolbar();

        setupTabLayout();

        addFab = binding.viewGroupAddFab;
        setupFloatingActionButton();

        fabOptionsLl = binding.viewGroupFabOptionsLl;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        backIv = binding.viewGroupToolbarOptionsIv;
        backIv.setOnClickListener(v -> viewGroupOptionsDialog());

        groupNameTv = binding.viewGroupNameTv;
        groupNameTv.setText(group.getGroupName());

        optionsIv = binding.viewGroupToolbarBackIv;
        optionsIv.setOnClickListener(v -> finish());
    }

    private void viewGroupOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_group_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogViewGroupToolbarOptionsBinding dialogBinding = DialogViewGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView backIv = dialogBinding.viewGroupOptionsBackIv;
        TextView leaveTv = dialogBinding.viewGroupOptionsLeaveTv;
        TextView reportTv = dialogBinding.viewGroupOptionsReportTv;
        TextView cancelTv = dialogBinding.viewGroupOptionsCancelTv;

        backIv.setOnClickListener(v -> dialog.dismiss());

        leaveTv.setOnClickListener(v -> {
            // TODO: leaveGroup()
            dialog.dismiss();
            leaveGroup();
        });

        reportTv.setOnClickListener(v -> {
            // TODO: Dialog / Activity "Report"
            dialog.dismiss();
            reportDialog();
        });

        cancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void leaveGroup() {
        // TODO
        Utils.showToast(this, "You left the group");
        finish();
    }

    private void reportDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_report, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogReportBinding dialogBinding = DialogReportBinding.inflate(LayoutInflater.from(this));
        dialog.show();
    }

    private void setupTabLayout() {
        viewPager = binding.viewGroupViewPager;
        tabLayout = binding.viewGroupTabLayout;
        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 2;
            }

            @Override
            public Fragment createFragment(int position) {
                Fragment result = null;
                if (position == 0) {
                    result = new FragmentViewGroup_Sets();
                } else if (position == 1) {
                    result = new FragmentViewGroup_Members();
                }
                return result;
            }
        };

        viewPager.setAdapter(adapter);

        // Wait for the adapter to set up the tabs before setting tab titles
        viewPager.post(() -> {
            tabLayout.getTabAt(0).setText("Sets");
            tabLayout.getTabAt(1).setText("Members");

            // Customize the text size for each tab
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TextView tabTv = (TextView) LayoutInflater.from(this)
                        .inflate(R.layout.tab_text, tabLayout, false);
                tabTv.setText(tabLayout.getTabAt(i).getText());
                tabTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tabTv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

                tabLayout.getTabAt(i).setCustomView(tabTv);

                ViewGroup.LayoutParams layoutParams = tabTv.getLayoutParams();
                layoutParams.width = 500;
                tabTv.setLayoutParams(layoutParams);
            }
        });

        // Connect the TabLayout with the ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Nothing needed here since tab titles are set beforehand
        }).attach();
    }

    private void setupFloatingActionButton() {
        addFab.setOnClickListener(v -> {
            if (!areFabOptionsVisible) {
                Utils.showItems(fabOptionsLl);
                areFabOptionsVisible = true;
            } else {
                Utils.hideItems(fabOptionsLl);
                areFabOptionsVisible = false;
            }
        });

        addSetTv = binding.viewGroupAddSetTv;
        addSetTv.setOnClickListener(v -> {
            addSetsDialog();
            Utils.hideItems(fabOptionsLl);
            areFabOptionsVisible = false;
        });

        addUserTv = binding.viewGroupAddUserTv;
        addUserTv.setOnClickListener(v -> {
            addUsersDialog();
            Utils.hideItems(fabOptionsLl);
            areFabOptionsVisible = false;
        });
    }

    private void addSetsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogAddSetToGroupBinding dialogBinding = DialogAddSetToGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView backIv = dialogBinding.addSetToGroupTitleBackIv;
        backIv.setOnClickListener(v -> {
            dialog.dismiss();
            saveSelectedSets();
        });

    }

    private void saveSelectedSets() {
        // TODO
    }


    private void addUsersDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_user_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogAddUserToGroupBinding dialogBinding = DialogAddUserToGroupBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialog.setOnDismissListener(d -> {
            d.dismiss();
            saveSelectedUsers();
        });

        TextInputEditText searchUsersEt = dialogBinding.addUsersToGroupSearchEt;
        ListView userListLv = dialogBinding.addUsersToGroupListLv;

        setupListView();
    }

    private void saveSelectedUsers() {
        // TODO
    }

    private void setupListView() {
        // TODO
    }

    private void getGroupData() {
        // TODO: Update PROFILE FRAGMENT to pass more than just name X D
        Intent intent = getIntent();
        if (intent != null) {
            String groupName = intent.getStringExtra("group_name");
            String nrOfSets = intent.getStringExtra("group_sets");

            TextView groupNameTv = binding.viewGroupNameTv;

            if (groupName != null) {
                groupNameTv.setText(groupName);
            }
        }
    }
}