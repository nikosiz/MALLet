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

import com.agh.api.GroupDTO;
import com.example.mallet.backend.entity.group.contribution.ModelGroupMemberMapper;
import com.example.mallet.databinding.FragmentViewGroupMembersBinding;
import com.example.mallet.utils.AdapterFolder;
import com.example.mallet.utils.ModelFolder;
import com.example.mallet.utils.ModelGroupMember;
import com.example.mallet.utils.Utils;

import java.util.List;

public class FragmentViewGroup_Members extends Fragment implements AdapterFolder.OnFolderClickListener {

    private final GroupDTO chosenGroup;

    public FragmentViewGroup_Members(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentViewGroupMembersBinding binding = FragmentViewGroupMembersBinding.inflate(inflater, container, false);

        LinearLayout userLibraryMembersLl = binding.viewGroupMembersMemberListLl; // Change to LinearLayout

        for (ModelGroupMember member : getUserLibraryMemberList()) {
            final int[] clickCounter = {0}; // Define a final variable here

            final View memberItemView = inflater.inflate(R.layout.model_group_member, userLibraryMembersLl, false);

            TextView usernameTv = memberItemView.findViewById(R.id.groupMember_usernameTv);
            TextView memberTv = memberItemView.findViewById(R.id.groupMember_usernameTv);
            ImageView memberOptionsIv = memberItemView.findViewById(R.id.groupMember_optionsIv);
            LinearLayout managePermissionsLl = memberItemView.findViewById(R.id.groupMember_permissionsLl);
            TextView deleteUserTv = memberItemView.findViewById(R.id.groupMember_deleteTv);

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

            userLibraryMembersLl.addView(memberItemView);
        }


        return binding.getRoot();
    }

    private List<ModelGroupMember> getUserLibraryMemberList() {
        return ModelGroupMemberMapper.from(chosenGroup.contributions());
    }

    private void managePermissions() {

    }

    public void onFolderClick(ModelFolder folder) {
        Utils.showToast(getContext(), "ASDF");
    }
}
