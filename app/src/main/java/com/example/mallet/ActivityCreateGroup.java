package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.agh.api.UserDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.create.GroupCreateContainer;
import com.example.mallet.backend.mapper.group.GroupCreateContainerMapper;
import com.example.mallet.databinding.ActivityCreateGroupBinding;
import com.example.mallet.databinding.DialogAddUserToGroupBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelUser;
import com.example.mallet.utils.ModelUserMapper;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCreateGroup extends AppCompatActivity {
    private ActivityCreateGroupBinding binding;
    private GroupServiceImpl groupService;

    // Toolbar
    private ImageView toolbarBackIv, toolbarSaveIv;

    // createGroupCl
    private TextInputEditText groupNameEt;
    private TextView groupNameErrTv;
    private LinearLayout groupMembersLl;
    private ExtendedFloatingActionButton addUsersEfab;
    private View groupMemberView;
    private CheckBox memberCb;

    // addMemberToGroupDialog
    private TextInputEditText searchUsersEt;
    private ListView userListLv;
    private ArrayAdapter userListAdapter; // chosen users
    private final List<ModelUser> allUsernames = new ArrayList<>();
    private final List<ModelUser> selectedUsers = new ArrayList<>();
    private View selecteUserItemView;
    private CheckBox selectedUserCb;
    private UserServiceImpl userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        String credential = AuthenticationUtils.get(getApplicationContext());
        this.userService = new UserServiceImpl(credential);
        this.groupService = new GroupServiceImpl(credential);

        setupContents();
    }

    private void setupContents() {
        setupToolbar();

        groupNameEt = binding.createGroupNameEt;
        groupNameErrTv = binding.createGroupErrorTv;

        groupMembersLl = binding.createGroupMembersLl;
        groupMemberView = getLayoutInflater().inflate(R.layout.model_add_member_to_group, groupMembersLl, false);

        addUsersEfab = binding.createGroupAddUsersEfab;
        addUsersEfab.setOnClickListener(v -> addUsersDialog());

        memberCb = groupMemberView.findViewById(R.id.addMemberToGroupCb);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.createGroupToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        toolbarBackIv = binding.createGroupToolbarBackIv;
        toolbarBackIv.setOnClickListener(v -> confirmExitDialog());

        toolbarSaveIv = binding.createGroupSaveIv;
        toolbarSaveIv.setOnClickListener(v -> handleGroupCreation());
    }

    public void confirmExitDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_confirm_exit, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            dialog.dismiss();
            close();
        });
    }

    private void handleGroupCreation() {
        Editable text = groupNameEt.getText();
        if (Objects.isNull(text) || text.toString().isEmpty()) {
            Utils.showToast(getApplicationContext(), "Group name cannot be empty");
            Utils.showItems(groupNameErrTv);
            groupNameErrTv.setText(R.string.field_cannot_be_empty);
            return;
        }

        GroupCreateContainer groupCreateContainer = GroupCreateContainerMapper.from(text.toString().trim(), selectedUsers);
        groupService.createGroup(groupCreateContainer, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long groupId = ResponseHandler.handleResponse(response);

                Intent intent = new Intent(getApplicationContext(), ActivityViewGroup.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupCreateContainer.name());

                close();

                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network failure");
            }
        });
    }

    private void addUsersDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_add_user_to_group, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogAddUserToGroupBinding dialogBinding = DialogAddUserToGroupBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView confirmTv = dialogBinding.addUsersToGroupConfirmTv;
        confirmTv.setOnClickListener(v -> dialog.dismiss());

        dialog.setOnDismissListener(d -> {
            d.dismiss();
            allUsernames.clear();
            userListAdapter.notifyDataSetChanged();
            //displaySelectedUsers(selectedUsers, groupMembersLl, getLayoutInflater());
        });

        searchUsersEt = dialogBinding.addUsersToGroupSearchEt;
        userListLv = dialogBinding.addUsersToGroupListLv;

        handleSearchUserInputChanged();
        handleAddingRemovingUser();
    }

    private void displaySelectedUsers(List<ModelUser> selectedUsernames, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelUser selectedUser : selectedUsernames) {
            selecteUserItemView = inflater.inflate(R.layout.model_add_member_to_group, null);

            LinearLayout userLayout = selecteUserItemView.findViewById(R.id.modelAddMemberToGroup_mainLl);

            TextView selectedUserUsernameTv = userLayout.findViewById(R.id.modelAddMemberToGroup_usernameTv);
            selectedUserUsernameTv.setText(selectedUser.getIdentifier());

            CheckBox selectedUserCb = userLayout.findViewById(R.id.addMemberToGroupCb);
            selectedUserCb.setChecked(true);

            selectedUserCb.setOnClickListener(view -> {
                if (!selectedUserCb.isChecked()) {
                    selectedUsers.remove(selectedUser);
                    linearLayout.removeView(userLayout);
                }
            });

            linearLayout.addView(selecteUserItemView);
        }
    }

    private void handleSearchUserInputChanged() {
        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allUsernames);
        userListLv.setAdapter(userListAdapter);

        RxTextView.textChanges(searchUsersEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (text.length() == 0) {
                        handleEmptyInput();
                        return;
                    }

                    fetchUsers(text);
                });
    }

    private void handleEmptyInput() {
        allUsernames.clear();
        allUsernames.addAll(selectedUsers);
        notifyListAdapterDataChanged();
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

        notifyListAdapterDataChanged();
    }

    private void handleAddingRemovingUser() {
        userListLv.setOnItemClickListener((parent, view, position, id) -> {
            ModelUser clickedUser = allUsernames.get(position);

            if (!selectedUsers.contains(clickedUser)) {
                selectedUsers.add(clickedUser);

                Utils.showToast(getApplicationContext(), clickedUser.getUsername() + clickedUser.getIdentifier() + " added to group");
            } else {
                selectedUsers.remove(clickedUser);
                if (searchUsersEt.getText().toString().trim().isEmpty()) {
                    allUsernames.remove(clickedUser);
                }
                Utils.showToast(getApplicationContext(), clickedUser.getUsername() + clickedUser.getIdentifier() + " removed from group");
            }

            displaySelectedUsers(selectedUsers, groupMembersLl, getLayoutInflater());

            userListAdapter.notifyDataSetChanged();
        });
    }

    private void close() {
        finish();
    }

    private void notifyListAdapterDataChanged() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                userListAdapter.notifyDataSetChanged();
            }
        });
    }
}