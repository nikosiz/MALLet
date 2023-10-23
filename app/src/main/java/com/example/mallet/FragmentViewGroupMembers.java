package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.FragmentViewGroupMembersBinding;
import com.example.mallet.utils.AdapterFolder;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroupMember;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewGroupMembers extends Fragment implements AdapterFolder.OnFolderClickListener {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentViewGroupMembersBinding binding = FragmentViewGroupMembersBinding.inflate(inflater, container, false);

        LinearLayout userLibraryMembersLl = binding.viewGroupMembersMemberListLl; // Change to LinearLayout
        List<ModelGroupMember> userLibraryMembersList = getUserLibraryMemberList();

        for (ModelGroupMember member : userLibraryMembersList) {
            final int[] clickCounter = {0}; // Define a final variable here

            final View[] memberItemView = {inflater.inflate(R.layout.model_group_member, userLibraryMembersLl, false)};

            TextView usernameTv = memberItemView[0].findViewById(R.id.groupMember_usernameTv);
            TextView memberTv = memberItemView[0].findViewById(R.id.groupMember_usernameTv);
            ImageView memberOptionsIv = memberItemView[0].findViewById(R.id.groupMember_optionsIv);
            LinearLayout managePermissionsLl = memberItemView[0].findViewById(R.id.groupMember_permissionsLl);
            TextView deleteUserTv = memberItemView[0].findViewById(R.id.groupMember_deleteTv);

            // Initially hide the TextViews
            Utils.hideItems(deleteUserTv, managePermissionsLl);
            Utils.makeItemsUnclickable(deleteUserTv, managePermissionsLl);

            usernameTv.setText(member.getUsername());

            memberTv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    Utils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    Utils.makeItemsClickable(deleteUserTv);
                } else {
                    Utils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    Utils.makeItemsClickable(deleteUserTv);
                }
            });

            memberOptionsIv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    Utils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    Utils.makeItemsClickable(deleteUserTv, managePermissionsLl);
                } else {
                    Utils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    Utils.makeItemsClickable(deleteUserTv, managePermissionsLl);
                }
            });

            userLibraryMembersLl.addView(memberItemView[0]);
        }


        return binding.getRoot();
    }

    private List<ModelGroupMember> getUserLibraryMemberList() {
        List<ModelGroupMember> memberList = new ArrayList<>();
        memberList.add(new ModelGroupMember(0, "user0"));
        memberList.add(new ModelGroupMember(1, "user1"));
        memberList.add(new ModelGroupMember(2, "user2"));
        memberList.add(new ModelGroupMember(3, "user3"));
        memberList.add(new ModelGroupMember(4, "user4"));
        return memberList;
    }

    private void managePermissions() {

    }

    public void onFolderClick(ModelFolder folder) {
        Utils.showToast(getContext(), "ASDF");
    }
}
