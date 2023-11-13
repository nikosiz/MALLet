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
import com.example.mallet.backend.mapper.group.ContributionUpdateContainerMapper;
import com.example.mallet.databinding.FragmentViewGroupMembersBinding;
import com.example.mallet.utils.ContributionUpdateInfo;
import com.example.mallet.utils.ModelGroupMember;
import com.example.mallet.utils.Utils;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewGroup_Members extends Fragment {

    private final GroupDTO chosenGroup;
    private final List<ContributionUpdateInfo> contributionsToUpdate = new ArrayList<>();
    private Animation fadeInAnimation;
    private ActivityViewGroup activityViewGroup;
    private FragmentViewGroupMembersBinding binding;
    private MaterialSwitch editGroupMs;
    private MaterialSwitch editSetsMs;
    private LinearLayout userLibraryMembersLl;
    private boolean editGroups, editSets;
    private ModelGroupMember newContributor;
    private GroupServiceImpl groupService;
    private List<ContributionUpdateContainer> containers;
    private final PermissionType setPermissionType = PermissionType.READ;
    private final PermissionType groupPermissionType = PermissionType.READ;
    public FragmentViewGroup_Members(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewGroupMembersBinding.inflate(inflater, container, false);

        setupContents();

        displayMembers();


        return binding.getRoot();
    }

    private List<ModelGroupMember> getUserLibraryMemberList() {
        return ModelGroupMemberMapper.from(chosenGroup.contributions());
    }

    private void managePermissions() {

    }

    private void setupContents() {
        userLibraryMembersLl = binding.viewGroupMembersMemberListLl; // Change to LinearLayout
        fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
    }

    private void displayMembers() {
        contributionsToUpdate.clear(); // Clear the list before adding contributions

        for (ModelGroupMember member : getUserLibraryMemberList()) {
            final int[] clickCounter = {0}; // Define a final variable here

            final View memberItemView = getLayoutInflater().inflate(R.layout.model_group_member, userLibraryMembersLl, false);

            TextView usernameTv = memberItemView.findViewById(R.id.groupMember_usernameTv);
            ImageView memberOptionsIv = memberItemView.findViewById(R.id.groupMember_optionsIv);

            LinearLayout managePermissionsLl = memberItemView.findViewById(R.id.groupMember_permissionsLl);
            editGroupMs = memberItemView.findViewById(R.id.groupMember_editGroupMs);
            editSetsMs = memberItemView.findViewById(R.id.groupMember_editSetsMs);

            editSetsMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
                //handleSetPermissionChange(isChecked, member);
            });

            editGroupMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
                //handleGroupPermissionChange(isChecked, member);
            });

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

            contributionsToUpdate.add(new ContributionUpdateInfo(member, setPermissionType, groupPermissionType));
        }

        //containers = mapContributionsToUpdate();

        // Display the contents of contributionsToUpdate after the loop
        //displayContributionsToUpdate();

        //handleContributionUpdate(containers);
    }
/*
    private void handleSetPermissionChange(boolean isChecked, ModelGroupMember member) {
        // Handle set permission change and update contributionsToUpdate
        setPermissionType = isChecked ? PermissionType.READ_WRITE : PermissionType.READ;
        updateContributionsToUpdate(member);
    }

    private void handleGroupPermissionChange(boolean isChecked, ModelGroupMember member) {
        // Handle group permission change and update contributionsToUpdate
        groupPermissionType = isChecked ? PermissionType.READ_WRITE : PermissionType.READ;
        updateContributionsToUpdate(member);
    }

    private void updateContributionsToUpdate(ModelGroupMember member) {
        // Create com.example.mallet.utils.ContributionUpdateInfo object with default values
        contributionsToUpdate.add(new ContributionUpdateInfo(member, setPermissionType, groupPermissionType));

        // Optionally, you can display the updated list here
        displayContributionsToUpdate();
    }

    private void displayContributionsToUpdate() {
        for (ContributionUpdateInfo contributionInfo : contributionsToUpdate) {
            System.out.println("ContributionsToUpdate\n" + "User: " + contributionInfo.getMember().getUsername() +
                    ", Set Permission: " + contributionInfo.getSetPermissionType() +
                    ", Group Permission: " + contributionInfo.getGroupPermissionType());
        }
    }

    private List<ContributionUpdateContainer> mapContributionsToUpdate() {
        List<ContributionUpdateContainer> containers = new ArrayList<>();

        for (ContributionUpdateInfo updateInfo : contributionsToUpdate) {
            ContributionUpdateContainer container = ContributionUpdateContainerMapper.from(updateInfo);
            containers.add(container);
        }

        return containers;
    }

    public void saveContributions() {
        //newContributor = new ModelGroupMember()

        //handleContributionUpdate(ContributionUpdateContainerMapper.from(member, contribution.groupPermissionType));
    }

    private void handleContributionUpdate(GroupUpdateContainer gUC) {
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
                Utils.showToast(getApplicationContext(), "Added");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network error");
            }
        });
    }*/
}