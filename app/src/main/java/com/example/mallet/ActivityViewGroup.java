package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.agh.api.GroupDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogAddSetToGroupBinding;
import com.example.mallet.databinding.DialogAddUserToGroupBinding;
import com.example.mallet.databinding.DialogReportBinding;
import com.example.mallet.databinding.DialogViewGroupToolbarOptionsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewGroup extends AppCompatActivity {
    private ActivityViewGroupBinding binding;
    // Toolbar
    private ImageView backIv;
    private TextView groupNameTv;
    private ImageView optionsIv;

    // Contents
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView usernameTv;
    private TextView setNameTv, setNrOfTermsTv, creatorNameTv;


    // Floating Action Button & its options
    private FloatingActionButton addFab;
    private TextView addSetTv;
    private TextView addUserTv;
    private static boolean areFabOptionsVisible = false;
    private LinearLayout fabOptionsLl;
    private Long groupId;
    private String groupName;
    private GroupServiceImpl groupService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo get groupId and fetch resources if paramter is passed
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Utils.closeActivity(ActivityViewGroup.this);
            }
        };

        // Register the callback with the onBackPressedDispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        this.groupId = getIntent().getLongExtra("groupId", 0L);
        this.groupName = getIntent().getStringExtra("groupName");

        String credential = AuthenticationUtils.get(getApplicationContext());
        groupService = new GroupServiceImpl(credential);

        binding = ActivityViewGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupContents();

        getGroupData();
    }

    private void setupContents() {
        setupToolbar();

        groupNameTv = binding.viewGroupNameTv;
        groupNameTv.setText(groupName);

        addFab = binding.viewGroupAddFab;
        setupFloatingActionButton();

        fabOptionsLl = binding.viewGroupFabOptionsLl;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        optionsIv = binding.viewGroupToolbarOptionsIv;
        optionsIv.setOnClickListener(v -> viewGroupOptionsDialog());

        backIv = binding.viewGroupToolbarBackIv;
        backIv.setOnClickListener(v -> finish());
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

    private void setupTabLayout(GroupDTO chosenGroup) {
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
                    result = new FragmentViewGroup_Sets(chosenGroup);
                } else if (position == 1) {
                    result = new FragmentViewGroup_Members(chosenGroup);
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
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAddSetToGroupBinding dialogBinding = DialogAddSetToGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();


    }

    private void saveSelectedSets() {
        // TODO
    }


    private void addUsersDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_user_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
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
        groupService.getGroup(groupId, new Callback<GroupDTO>() {
            @Override
            public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {
                GroupDTO groupDTO = ResponseHandler.handleResponse(response);

                setupTabLayout(groupDTO);


            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network failure");
            }
        });
    }
}