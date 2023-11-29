package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.agh.api.ContributionDTO;
import com.agh.api.GroupDTO;
import com.agh.api.PermissionType;
import com.agh.api.SetBasicDTO;
import com.agh.api.UserDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.set.boundary.SetServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.update.ContributionUpdateContainer;
import com.example.mallet.backend.entity.group.update.GroupUpdateContainer;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivityViewGroupBinding;
import com.example.mallet.databinding.DialogAddSetToGroupBinding;
import com.example.mallet.databinding.DialogAddUserToGroupBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.databinding.DialogDeleteAreYouSureBinding;
import com.example.mallet.databinding.DialogViewGroupToolbarOptionsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelUser;
import com.example.mallet.utils.ModelUserMapper;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewGroup extends AppCompatActivity {
    public static Long groupId;
    public static String groupName;
    public static String credential;
    public static boolean canUserEditSet = true;
    private static boolean areFabOptionsVisible = false;
    private final List<ModelUser> allUsernames = new ArrayList<>();
    private final List<ModelLearningSet> allSets = new ArrayList<>();
    private final boolean isSetInGroup = true;
    private final boolean isSetNew = true;
    private UserServiceImpl userService;
    private SetServiceImpl setService;
    private ActivityViewGroupBinding binding;
    private ImageView backIv;
    private TextView groupNameTv;
    private ImageView optionsIv;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ExtendedFloatingActionButton addEfab;
    private TextView addSetTv;
    private TextView addUserTv;
    private LinearLayout fabOptionsLl;
    private GroupServiceImpl groupService;
    private ListView userListLv;
    private ListView setListLv;
    private ArrayAdapter userListAdapter;
    private ArrayAdapter setListAdapter;
    private GroupDTO chosenGroup;
    private boolean isUserAdmin;
    private ImageView toolbarOptionsBackIv;
    private TextView toolbarOptionsLeaveTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv;
    private SharedPreferences sharedPreferences;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo get groupId and fetch resources if parameter is passed
        super.onCreate(savedInstanceState);

        credential = AuthenticationUtils.get(getApplicationContext());
        this.setService = new SetServiceImpl(credential);
        this.userService = new UserServiceImpl(credential);
        this.groupService = new GroupServiceImpl(credential);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Utils.closeActivity(ActivityViewGroup.this);
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);

        groupId = getIntent().getLongExtra("groupId", 0L);
        groupName = getIntent().getStringExtra("groupName");

        binding = ActivityViewGroupBinding.inflate(getLayoutInflater());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(binding.getRoot());

        setupContents();

        getGroupData();
    }

    private void setupContents() {
        setupToolbar();

        groupNameTv = binding.viewGroupNameTv;
        groupNameTv.setText(groupName);

        addEfab = binding.viewGroupAddFab;
        setupFloatingActionButton();

        fabOptionsLl = binding.viewGroupFabOptionsLl;
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        backIv = binding.viewGroupToolbarBackIv;
        backIv.setOnClickListener(v -> finish());

        optionsIv = binding.viewGroupToolbarOptionsIv;
        optionsIv.setOnClickListener(v -> viewGroupToolbarOptionsDialog());
    }

    private void viewGroupToolbarOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_group_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogViewGroupToolbarOptionsBinding dialogBinding = DialogViewGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        toolbarOptionsBackIv = dialogBinding.viewGroupOptionsBackIv;
        toolbarOptionsLeaveTv = dialogBinding.viewGroupOptionsLeaveTv;
        toolbarOptionsDeleteTv = dialogBinding.viewGroupOptionsDeleteTv;
        toolbarOptionsCancelTv = dialogBinding.viewGroupOptionsCancelTv;

        toolbarOptionsBackIv.setOnClickListener(v -> dialog.dismiss());

        toolbarOptionsLeaveTv.setOnClickListener(v -> {
            if (!isUserAdmin) {
                leaveGroupDialog();
            } else {
                confirmGroupDeletion(groupId);
            }
        });

        toolbarOptionsDeleteTv.setOnClickListener(v -> confirmGroupDeletion(groupId));

        toolbarOptionsCancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void leaveGroupDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_exit, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.confirmExitCancelTv;
        cancelTv.setOnClickListener(v -> dialog.dismiss());

        TextView confirmTv = dialogBinding.confirmExitConfirmTv;
        confirmTv.setText("Continue");
        confirmTv.setOnClickListener(v -> leaveGroup(groupId));
    }

    private void leaveGroup(Long groupId) {
        // todo michałek napraw
        /*groupService.deleteGroupContributions(groupId, userId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });*/
    }

    private void setupTabLayout(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
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
        addEfab.setOnClickListener(v -> {
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
        addSetsDialogWithRestart(0);
    }

    private void addSetsDialogWithRestart(int attemptCount) {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_set_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAddSetToGroupBinding dialogBinding = DialogAddSetToGroupBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView createNewSetIv = dialogBinding.addSetToGroupCreateNewIv;
        if (canUserEditSet) {
            Utils.showItems(createNewSetIv);
            Utils.enableItems(createNewSetIv);
            createNewSetIv.setOnClickListener(v -> {
                dialog.dismiss();
                createNewSetInGroup();
            });
        } else {
            Utils.hideItems(createNewSetIv);
            Utils.disableItems(createNewSetIv);
        }

        setListLv = dialogBinding.addSetToGroupListLv;
        setListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allSets);
        setListLv.setAdapter(setListAdapter);
        setListLv.setOnItemClickListener((parent, view, position, id) -> {
            ModelLearningSet clickedSet = allSets.get(position);
            groupService.addSet(groupId, clickedSet.getId(), new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getGroupData();
                    //dialog.dismiss();
                    Utils.showToast(getApplicationContext(), "Set added");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (attemptCount < MAX_RETRY_ATTEMPTS) {
                        // System.out.println(attemptCount);
                        // Retry the operation
                        addSetsDialogWithRestart(attemptCount + 1);
                    } else {
                        Utils.showToast(getApplicationContext(), "Network error");
                    }
                }
            });
        });

        TextInputEditText searchUsersEt = dialogBinding.addSetToGroupSearchEt;

        RxTextView.textChanges(searchUsersEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (text.length() < 2) {
                        return;
                    }
                    if (text.length() == 0) {
                        handleSetEmptyInput();
                        return;
                    }

                    fetchSets(text);
                });

        TextView confirmTv = dialogBinding.addSetToGroupConfirmTv;
        confirmTv.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void createNewSetInGroup() {
        Intent intent = new Intent(this, ActivityEditLearningSet.class);

        intent.putExtra("groupName", groupName);
        intent.putExtra("groupId", groupId);

        intent.putExtra("isUserSet", false);
        intent.putExtra("isSetInGroup", isSetInGroup);
        intent.putExtra("canUserEditSet", canUserEditSet);
        intent.putExtra("isSetNew", isSetNew);

        startActivity(intent);

        this.finish();
    }

    private void handleSetEmptyInput() {
        allSets.clear();
        notifySetListAdapterDataChanged();
    }

    private void fetchSets(CharSequence text) {
        setService.getBasicSet(text.toString(), new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                handleFetchSetsResponse(response);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network error");
            }
        });
    }

    private void handleFetchSetsResponse(Response<SetBasicDTO> response) {
        SetBasicDTO sets = ResponseHandler.handleResponse(response);

        allSets.clear();
        sets.sets().stream()
                .map(ModelLearningSetMapper::from)
                .forEach(allSets::add);

        List<ModelLearningSet> existingSets = ModelLearningSetMapper.from(chosenGroup.sets());
        allSets.removeAll(existingSets);

        notifySetListAdapterDataChanged();
    }

    private void notifySetListAdapterDataChanged() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addUsersDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_user_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAddUserToGroupBinding dialogBinding = DialogAddUserToGroupBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        dialog.setOnDismissListener(d -> {
            d.dismiss();
        });

        TextInputEditText searchUsersEt = dialogBinding.addUsersToGroupSearchEt;
        TextView addUsersToGroupConfirmTv = dialogBinding.addUsersToGroupConfirmTv;
        this.userListLv = dialogBinding.addUsersToGroupListLv;
        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allUsernames);
        userListLv.setAdapter(userListAdapter);

        RxTextView.textChanges(searchUsersEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (text.length() < 2) {
                        return;
                    }
                    if (text.length() == 0) {
                        handleUserEmptyInput();
                        return;
                    }

                    fetchUsers(text);
                });

        addUsersToGroupConfirmTv.setOnClickListener(v -> {
            dialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), ActivityViewGroup.class);

            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", groupName);

            startActivity(intent);

            closeActivity();
        });

        userListLv.setOnItemClickListener((parent, view, position, id) -> {
            ModelUser clickedUser = allUsernames.get(position);

            //todo handle add contribution
            ContributionUpdateContainer contribution = ContributionUpdateContainer.builder()
                    .groupPermissionType(PermissionType.READ)
                    .setPermissionType(PermissionType.READ)
                    .contributorId(clickedUser.getId())
                    .build();

            GroupUpdateContainer groupUpdateContainer = GroupUpdateContainer.builder()
                    .id(groupId)
                    .contribution(contribution)
                    .build();
            groupService.updateGroupContribution(groupUpdateContainer, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Utils.showToast(getApplicationContext(), "User added to group");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Utils.showToast(getApplicationContext(), "Network error");
                }
            });


        });
    }

    private void handleUserEmptyInput() {
        allUsernames.clear();
        notifyUserListAdapterDataChanged();
    }

    private void fetchUsers(CharSequence text) {
        userService.get(text.toString(), new Callback<>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                handleFetchUsersResponse(response);
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network error");
            }
        });
    }

    private void handleFetchUsersResponse(Response<List<UserDTO>> response) {
        List<UserDTO> userDTOS = ResponseHandler.handleResponse(response);

        allUsernames.clear();
        userDTOS.stream()
                .map(ModelUserMapper::from)
                .forEach(allUsernames::add);

        notifyUserListAdapterDataChanged();
    }

    private void notifyUserListAdapterDataChanged() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                userListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getGroupData() {
        groupService.getGroup(groupId, new Callback<GroupDTO>() {
            @Override
            public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {
                GroupDTO groupDTO = ResponseHandler.handleResponse(response);

                ContributionDTO contributionDTO1 = groupDTO.contributions().stream()
                        .filter(contributionDTO -> PermissionType.ADMIN.equals(contributionDTO.groupPermissionType()) && PermissionType.ADMIN.equals(contributionDTO.setPermissionType()))
                        .findAny().get();

                userId = sharedPreferences.getLong("userId", 0L);

                isUserAdmin = contributionDTO1.contributor().id().equals(userId);

                setupTabLayout(groupDTO);
            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network error");
            }
        });
    }

    public void confirmGroupDeletionWithRestart(Long id, int attemptCount) {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_are_you_sure, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAreYouSureBinding dialogBinding = DialogDeleteAreYouSureBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.deleteCancelTv;
        TextView confirmTv = dialogBinding.deleteConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());
        confirmTv.setOnClickListener(v -> {
            Utils.disableItems(cancelTv, confirmTv, toolbarOptionsLeaveTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv, backIv);
            groupService.deleteGroup(id, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        ResponseHandler.handleResponse(response);
                        Utils.showToast(getApplicationContext(), "Group was deleted");

                        Utils.enableItems(cancelTv, confirmTv, toolbarOptionsLeaveTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv, backIv);

                        closeActivity();
                    } catch (MalletException e) {
                        // System.out.println(e.getMessage());
                        Utils.enableItems(cancelTv, confirmTv, toolbarOptionsLeaveTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv, backIv);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (attemptCount < MAX_RETRY_ATTEMPTS) {
                        // System.out.println(attemptCount);
                        // Retry the network call
                        confirmGroupDeletionWithRestart(id, attemptCount + 1);
                    } else {
                        Utils.showToast(getApplicationContext(), "Group was not deleted due to an error");
                        Utils.enableItems(cancelTv, confirmTv, toolbarOptionsLeaveTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv, backIv);
                    }
                }

            });
            dialog.dismiss();
        });
    }

    private void confirmGroupDeletion(Long id) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        confirmGroupDeletionWithRestart(id, attemptCount);
    }

    private void closeActivity() {
        finish();
    }
}