package com.example.mallet;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
import com.example.mallet.databinding.DialogDeleteAreYouSureBinding;
import com.example.mallet.databinding.DialogViewGroupToolbarOptionsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.ModelUser;
import com.example.mallet.utils.ModelUserMapper;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private UserServiceImpl userService;
    private SetServiceImpl setService;
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
    private FragmentViewGroup_Sets setFragment;
    private FragmentViewGroup_Members memberFragment;

    private ListView userListLv;
    private ListView setListLv;
    private ArrayAdapter userListAdapter;
    private final List<ModelUser> allUsernames = new ArrayList<>();
    private ArrayAdapter setListAdapter;
    private final List<ModelLearningSet> allSets = new ArrayList<>();

    private GroupDTO chosenGroup;
    private ImageView saveGroupIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo get groupId and fetch resources if parameter is passed
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(getApplicationContext());
        this.setService = new SetServiceImpl(credential);
        this.userService = new UserServiceImpl(credential);
        this.groupService = new GroupServiceImpl(credential);


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

        backIv = binding.viewGroupToolbarBackIv;
        backIv.setOnClickListener(v -> finish());

        optionsIv = binding.viewGroupToolbarOptionsIv;
        optionsIv.setOnClickListener(v -> viewGroupOptionsDialog());

        saveGroupIv = binding.viewGroupToolbarSaveGroupIv;
        saveGroupIv.setOnClickListener(v -> saveGroup());
    }

    private void saveGroup() {
        // TODO MICHAŁEK
    }

    private void viewGroupOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_group_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT), Gravity.BOTTOM);
        DialogViewGroupToolbarOptionsBinding dialogBinding = DialogViewGroupToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        ImageView backIv = dialogBinding.viewGroupOptionsBackIv;
        TextView leaveTv = dialogBinding.viewGroupOptionsLeaveTv;
        TextView deleteTv = dialogBinding.viewGroupOptionsDeleteTv;
        TextView cancelTv = dialogBinding.viewGroupOptionsCancelTv;

        backIv.setOnClickListener(v -> dialog.dismiss());

        leaveTv.setOnClickListener(v -> {
            // TODO: leaveGroup()

            leaveGroup();
        });

        deleteTv.setOnClickListener(v -> confirmDelete(this.groupId));

        cancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void leaveGroup() {
        // TODO
        Utils.showToast(this, "You left the group");

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

        setListLv = dialogBinding.addSetToGroupListLv;
        setListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allSets);
        setListLv.setAdapter(setListAdapter);
        setListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelLearningSet clickedSet = allSets.get(position);
                groupService.addSet(groupId, clickedSet.getId(), new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        dialog.dismiss();
                        Utils.showToast(getApplicationContext(), "Set added");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Utils.showToast(getApplicationContext(), "Network failure");

                    }
                });
            }
        });

        TextInputEditText searchUsersEt = dialogBinding.addSetToGroupSearchEt;

        RxTextView.textChanges(searchUsersEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if(text.length() < 2){
                        return;
                    }
                    if (text.length() == 0) {
                        handleSetEmptyInput();
                        return;
                    }

                    fetchSets(text);
                });
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
                Utils.showToast(getApplicationContext(), "Network failure");
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
        this.userListLv = dialogBinding.addUsersToGroupListLv;
        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allUsernames);
        userListLv.setAdapter(userListAdapter);

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
                    Utils.showToast(getApplicationContext(),"Added");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Utils.showToast(getApplicationContext(),"Network failure");
                }
            });

        RxTextView.textChanges(searchUsersEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if(text.length() < 2){
                        return;
                    }
                    if (text.length() == 0) {
                        handleUserEmptyInput();
                        return;
                    }

                    fetchUsers(text);
                });

    });}

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
                Utils.showToast(getApplicationContext(), "Network failure");
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

                setupTabLayout(groupDTO);
            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network failure");
            }
        });
    }

    public void confirmDelete(Long id) {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_are_you_sure, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAreYouSureBinding dialogBinding = DialogDeleteAreYouSureBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.deleteCancelTv;
        TextView confirmTv = dialogBinding.deleteConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());
        confirmTv.setOnClickListener(v -> {
            Utils.disableItems(confirmTv);
            groupService.deleteGroup(id, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        ResponseHandler.handleResponse(response);
                        Utils.showToast(getApplicationContext(), "Group was deleted");
                        Utils.enableItems(confirmTv);
                    } catch (MalletException e) {
                        System.out.println(e.getMessage());
                        Utils.showToast(getApplicationContext(), "Error");
                        Utils.enableItems(confirmTv);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("error");
                }

            });

            dialog.dismiss();
        });
    }
}