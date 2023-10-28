package com.example.mallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.agh.api.UserDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.example.mallet.databinding.ActivityCreateGroupBinding;
import com.example.mallet.databinding.DialogAddMemberToGroupBinding;
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
    private ArrayList<UserDTO> userList;
    private final ArrayList<String> selectedUsersList = new ArrayList<>();

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

        searchUsersEt = dialogBinding.addMembersToGroupSearchEt;
        userListLv = dialogBinding.addMembersToGroupListLv;

        setupListView();

        System.out.println(selectedUsersList);


    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }

    private void setupListView() {
        userList = getUserList(); // Initialize userList here

        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);
        userListLv.setAdapter(userListAdapter);

        userListLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = userList.get(position);

                // Check if the user is already in the selected list
                if (!selectedUsersList.contains(selectedUser)) {
                    // Add the user to the selected list
                    selectedUsersList.add(selectedUser);
                    // Log the content of selectedUsersList
                    Log.d("SelectedUsersList", selectedUsersList.toString());
                    Utils.showToast(getApplicationContext(), selectedUser + " added to group.");
                }
            }
        });
    }


    private ArrayList getUserList() {
        ArrayList userList = new ArrayList();

        // TODO: When user finishes writing username of the user he wants to add
        //       display a list of users with this name (and their IDs). Then the user can check
        //       a checkbox and the selected user will be added to group

        userList.add("Anas");
        userList.add("Aman");
        userList.add("Shruti");
        userList.add("Palki");
        userList.add("Nikhil");
        userList.add("Varun");
        userList.add("Avinash");
        userList.add("Subham");
        userList.add("Abhishek");
        userList.add("Sayantan");
        userList.add("Siddharth");
        userList.add("Abhinav");
        userList.add("Viplav");
        userList.add("Puneet");
        userList.add("Tarzan");
        userList.add("Badshah");
        userList.add("Jake");

        userListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, userList);

        userListLv.setAdapter(userListAdapter);

        searchUsersEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed before text changes.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the adapter as text changes
                userListAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed after text changes.
            }
        });

        return userList;
    }
}