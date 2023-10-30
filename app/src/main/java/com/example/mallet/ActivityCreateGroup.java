package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.example.mallet.databinding.ActivityCreateGroupBinding;
import com.example.mallet.databinding.DialogAddMemberToGroupBinding;
import com.example.mallet.utils.ModelUser;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityCreateGroup extends AppCompatActivity {
    private ActivityCreateGroupBinding binding;
    private GroupServiceImpl groupService;
    private ResponseHandler responseHandler;

    // createGroup_toolbar
    private ImageView backIv, submitIv;

    // createGroupCl
    private TextInputEditText groupNameEt;
    private String groupName;
    private LinearLayout groupMembersLl;
    private FloatingActionButton addMembersFab;
    private View groupMemberView;
    private CheckBox memberCb;

    // addMemberToGroupDialog
    private ImageView addMemberBackIv, addMemberSubmitIv;
    private TextInputEditText searchUsersEt;
    private ListView userListLv;
    private ArrayAdapter userListAdapter;
    private ArrayList<String> allUsernames;
    private final ArrayList<String> selectedUsers = new ArrayList<>();
    private View selecteUserItemView;
    private CheckBox selectedUserCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();
    }

    private void setupContents() {
        backIv = binding.createGroupToolbarBackIv;
        submitIv = binding.createGroupSaveIv;
        submitIv.setOnClickListener(v -> handleGroupCreation());

        groupNameEt = binding.createGroupNameEt;

        groupMembersLl = binding.createGroupMembersLl;
        groupMemberView = getLayoutInflater().inflate(R.layout.model_add_member_to_group, groupMembersLl, false);

        addMembersFab = binding.createGroupAddMemberFab;
        addMembersFab.setOnClickListener(v -> addMembersDialog());

        memberCb = groupMemberView.findViewById(R.id.addMemberToGroupCb);
    }

    private void handleGroupCreation() {
        groupName = groupNameEt.getText().toString().trim();
        List<ContributionCreateContainer> members = new ArrayList<>();
    }

    private void addMembersDialog() {
        Dialog dialog = createDialog(R.layout.dialog_add_member_to_group);
        DialogAddMemberToGroupBinding dialogBinding = DialogAddMemberToGroupBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        addMemberBackIv = dialogBinding.addMembersToGroupToolbarBackIv;
        addMemberBackIv.setOnClickListener(v -> {
            dialog.dismiss();
            saveSelectedUsers();
        });

        searchUsersEt = dialogBinding.addMembersToGroupSearchEt;
        userListLv = dialogBinding.addMembersToGroupListLv;

        setupListView();
    }

    private void saveSelectedUsers() {
        displaySelectedUsers(selectedUsers, groupMembersLl, getLayoutInflater());
    }

    private void setupListView() {
        allUsernames = getUsernames();

        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allUsernames);
        userListLv.setAdapter(userListAdapter);

        userListLv.setOnItemClickListener((parent, view, position, id) -> {
            String clickedUser = allUsernames.get(position);

            if (!selectedUsers.contains(clickedUser)) {
                selectedUsers.add(clickedUser);
                System.out.println("Selected users: " + selectedUsers);
                Utils.showToast(getApplicationContext(), clickedUser + " added to group");
            } else {
                selectedUsers.remove(clickedUser);
                System.out.println("Selected users: " + selectedUsers);
                Utils.showToast(getApplicationContext(), clickedUser + " removed from group");
            }

            userListAdapter.notifyDataSetChanged();
        });
    }

    private void displaySelectedUsers(List<String> selectedUsernames, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (String selectedUser : selectedUsernames) {
            selecteUserItemView = inflater.inflate(R.layout.model_add_member_to_group, null);

            LinearLayout userLayout = selecteUserItemView.findViewById(R.id.modelAddMemberToGroup_mainLl);

            TextView selectedUserUsernameTv = userLayout.findViewById(R.id.modelAddMemberToGroup_usernameTv);
            selectedUserUsernameTv.setText(selectedUser);

            CheckBox selectedUserCb = userLayout.findViewById(R.id.addMemberToGroupCb);
            selectedUserCb.setChecked(true);

            selectedUserCb.setOnClickListener(view -> {
                if (!selectedUserCb.isChecked()) {
                    selectedUsers.remove(selectedUser);
                    System.out.println("SelectedUsersList: " + selectedUsers);
                    linearLayout.removeView(userLayout);
                }
            });

            linearLayout.addView(selecteUserItemView);
        }
    }


    private ArrayList<String> getUsernames() {
        ArrayList<String> allUsernames = new ArrayList<>();

        for (ModelUser user : getAllUsernames()) {
            allUsernames.add(user.getUsername() + user.getIdentifier());
        }

        return allUsernames;
    }


    private List<ModelUser> getAllUsernames() {
        // TODO: This list needs to come from server
        List<ModelUser> allUsers = new ArrayList<>();

        // TODO: When user finishes writing username of the user he wants to add
        //       display a list of users with this name (and their IDs). Then the user can check
        //       a checkbox and the selected user will be added to group

        allUsers.add(ModelUser.builder()
                .id(2137L)
                .username("Miszel")
                .identifier("#2137")
                .build());
        allUsers.add(ModelUser.builder()
                .id(3721L)
                .username("Miszel")
                .identifier("#3721")
                .build());
        allUsers.add(ModelUser.builder()
                .id(2137L)
                .username("PalaMichala")
                .identifier("#3265")
                .build());
        allUsers.add(ModelUser.builder()
                .id(2137L)
                .username("Lukasz")
                .identifier("#69420")
                .build());


        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, allUsers);

        userListLv.setAdapter(userListAdapter);

        searchUsersEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userListAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return allUsers;
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}