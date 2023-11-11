package com.example.mallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agh.api.GroupDTO;
import com.agh.api.PermissionType;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.entity.group.contribution.ModelGroupMemberMapper;
import com.example.mallet.backend.entity.group.update.ContributionUpdateContainer;
import com.example.mallet.backend.entity.group.update.GroupUpdateContainer;
import com.example.mallet.databinding.FragmentViewGroupMembersBinding;
import com.example.mallet.utils.ModelGroupMember;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewGroup_Members extends Fragment {
    private final GroupDTO chosenGroup;
    private Animation fadeInAnimation;

    private MaterialSwitch editGroupMs;
    private MaterialSwitch editSetsMs;

    public FragmentViewGroup_Members(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentViewGroupMembersBinding binding = FragmentViewGroupMembersBinding.inflate(inflater, container, false);

        LinearLayout userLibraryMembersLl = binding.viewGroupMembersMemberListLl; // Change to LinearLayout
        fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);


        for (ModelGroupMember member : getUserLibraryMemberList()) {
            final int[] clickCounter = {0}; // Define a final variable here

            final View memberItemView = inflater.inflate(R.layout.model_group_member, userLibraryMembersLl, false);

            TextView usernameTv = memberItemView.findViewById(R.id.groupMember_usernameTv);
            ImageView memberOptionsIv = memberItemView.findViewById(R.id.groupMember_optionsIv);

            LinearLayout managePermissionsLl = memberItemView.findViewById(R.id.groupMember_permissionsLl);
            editGroupMs = memberItemView.findViewById(R.id.groupMember_editGroupMs);
            editSetsMs = memberItemView.findViewById(R.id.groupMember_editSetsMs);

            TextView deleteUserTv = memberItemView.findViewById(R.id.groupMember_deleteTv);

            // Initially hide the TextViews
            Utils.hideItems(deleteUserTv, managePermissionsLl);
            Utils.disableItems(deleteUserTv, managePermissionsLl);

            usernameTv.setText(member.getUsername());

            usernameTv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    Utils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    Utils.enableItems(deleteUserTv);
                } else {
                    Utils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    Utils.enableItems(deleteUserTv);
                }
            });

            memberOptionsIv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    Utils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    Utils.enableItems(deleteUserTv, managePermissionsLl);
                } else {
                    Utils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    Utils.enableItems(deleteUserTv, managePermissionsLl);
                }
            });

            memberItemView.setAnimation(fadeInAnimation);

            userLibraryMembersLl.addView(memberItemView);
        }


        return binding.getRoot();
    }

    private boolean editGroups, editSets;

    private void setContributions() {
        editGroups = editGroupMs.isChecked();

        editSets = editSetsMs.isChecked();
    }

    private ModelGroupMember member;

    private List<ModelGroupMember> getUserLibraryMemberList() {
        return ModelGroupMemberMapper.from(chosenGroup.contributions());
    }

    private GroupServiceImpl groupService;

    private void updateContributions(GroupUpdateContainer gUC) {
        ContributionUpdateContainer contribution = ContributionUpdateContainer.builder()
                .groupPermissionType(PermissionType.READ)
                .setPermissionType(PermissionType.READ)
                .contributorId(member.getUserId())
                .build();

        GroupUpdateContainer groupUpdateContainer = GroupUpdateContainer.builder()
                .id(chosenGroup.id())
                .contribution(contribution)
                .build();

        groupService.updateGroupContribution(groupUpdateContainer, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
