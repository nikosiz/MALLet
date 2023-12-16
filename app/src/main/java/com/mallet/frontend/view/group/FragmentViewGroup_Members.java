package com.mallet.frontend.view.group;

import android.content.Intent;
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

import com.agh.api.ContributionDTO;
import com.agh.api.GroupDTO;
import com.agh.api.PermissionType;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.mallet.R;
import com.mallet.backend.client.configuration.ResponseHandler;
import com.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.mallet.backend.mapper.group.ModelGroupMemberMapper;
import com.mallet.backend.entity.group.update.ContributionUpdateContainer;
import com.mallet.backend.entity.group.update.GroupUpdateContainer;
import com.mallet.backend.exception.MalletException;
import com.mallet.backend.mapper.group.ContributionUpdateContainerMapper;
import com.mallet.databinding.FragmentViewGroupMembersBinding;
import com.mallet.frontend.model.group.ModelGroupMember;
import com.mallet.frontend.security.CredentialsHandler;
import com.mallet.frontend.utils.ViewUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewGroup_Members extends Fragment {

    private final GroupDTO chosenGroup;
    private final Map<Long, ContributionUpdateContainer> contributionsToUpdateByUserId = new HashMap<>();
    private Animation fadeInAnimation;
    private FragmentViewGroupMembersBinding binding;
    private MaterialSwitch editGroupMs;
    private MaterialSwitch editSetsMs;
    private LinearLayout userLibraryMembersLl;
    private boolean editGroups, editSets;
    private ModelGroupMember newContributor;
    private GroupServiceImpl groupService;
    private List<ContributionUpdateContainer> containers;
    private List<ModelGroupMember> contributions;
    private ContributionDTO selfContribution;

    public FragmentViewGroup_Members(GroupDTO chosenGroup, ContributionDTO selfContribution ) {
        this.chosenGroup = chosenGroup;
        this.selfContribution = selfContribution;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewGroupMembersBinding.inflate(inflater, container, false);
        String credential = CredentialsHandler.get(getContext());
        this.groupService = new GroupServiceImpl(credential);
        contributions = ModelGroupMemberMapper.from(chosenGroup.contributions());
        setupContents();

        displayMembers();

        return binding.getRoot();
    }

    private void setupContents() {
        userLibraryMembersLl = binding.viewGroupMembersMemberListLl; // Change to LinearLayout
        fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
    }

    private void displayMembers() {
        contributionsToUpdateByUserId.clear(); // Clear the list before adding contributions

        for (ModelGroupMember member : contributions) {
            final int[] clickCounter = {0}; // Define a final variable here

            final View memberItemView = getLayoutInflater().inflate(R.layout.model_group_member, userLibraryMembersLl, false);

            TextView usernameTv = memberItemView.findViewById(R.id.groupMember_usernameTv);
            ImageView memberOptionsIv = memberItemView.findViewById(R.id.groupMember_optionsIv);

            LinearLayout managePermissionsLl = memberItemView.findViewById(R.id.groupMember_permissionsLl);
            editGroupMs = memberItemView.findViewById(R.id.groupMember_editGroupMs);
            editSetsMs = memberItemView.findViewById(R.id.groupMember_editSetsMs);
            TextView deleteUserTv = memberItemView.findViewById(R.id.groupMember_deleteTv);

            editGroupMs.setChecked(determineIsChecked(member.getGroupPermissionType()));
            editSetsMs.setChecked(determineIsChecked(member.getSetPermissionType()));
            editSetsMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
                handleSetPermissionChange(isChecked, member);
            });

            editGroupMs.setOnCheckedChangeListener((buttonView, isChecked) -> {
                handleGroupPermissionChange(isChecked, member);
            });

            deleteUserTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeContribution(member);
                }
            });

            // Initially hide the TextViews
            ViewUtils.hideItems(deleteUserTv, managePermissionsLl);
            ViewUtils.disableItems(deleteUserTv, managePermissionsLl);

            usernameTv.setText(member.getUsername());

            usernameTv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    if (isAdminOrSelf(member)) {
                        return;
                    }
                    ViewUtils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    ViewUtils.enableItems(deleteUserTv);
                } else {
                    ViewUtils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    ViewUtils.disableItems(deleteUserTv);
                }
            });

            if (isAdminOrSelf(member)) {
                ViewUtils.hideItems(memberOptionsIv);
            }

            memberOptionsIv.setOnClickListener(v -> {
                clickCounter[0]++;
                // Use the local clickCounter here
                if (clickCounter[0] % 2 != 0) {
                    if (isAdminOrSelf(member)) {
                        return;
                    }
                    ViewUtils.showItems(deleteUserTv, managePermissionsLl); // Show on odd clicks
                    ViewUtils.enableItems(deleteUserTv, managePermissionsLl);
                } else {
                    ViewUtils.hideItems(deleteUserTv, managePermissionsLl); // Hide on even clicks
                    ViewUtils.disableItems(deleteUserTv);

                }
            });

            memberItemView.setAnimation(fadeInAnimation);

            userLibraryMembersLl.addView(memberItemView);

            contributionsToUpdateByUserId.put(member.getUserId(), ContributionUpdateContainerMapper.from(member));
        }
        TextView savePermissionTv = binding.viewGroupMembersSavePermissionsTv;

        savePermissionTv.setOnClickListener(v -> saveContributions());
    }

    private  boolean isAdminOrSelf(ModelGroupMember member) {
        return (PermissionType.ADMIN.equals(member.getGroupPermissionType()) && PermissionType.ADMIN.equals(member.getSetPermissionType())) ||
                selfContribution.id().equals(member.getContributionId());
    }

    private void removeContribution(ModelGroupMember member) {
        groupService.deleteGroupContributions(chosenGroup.id(), Collections.singleton(member.getContributionId()), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (Objects.isNull(getView())) {
                    return;
                }
                try {
                    ResponseHandler.handleResponse(response);
                    //todo check if refreshing
                    contributions.remove(member);
                    ViewUtils.showToast(getContext(), "Member removed");

                    Intent intent = new Intent(getContext(), ActivityViewGroup.class);

                    intent.putExtra("groupId", ActivityViewGroup.groupId);
                    intent.putExtra("groupName", ActivityViewGroup.groupName);

                    startActivity(intent);

                    requireActivity().finish();
                } catch (MalletException exception) {
                    ViewUtils.showToast(getContext(), exception.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ViewUtils.showToast(getContext(), "Network error");
            }
        });
    }

    private void handleSetPermissionChange(boolean isChecked, ModelGroupMember member) {
        updateContributionSetPermissionToUpdate(member, determinePermissionType(isChecked));
    }

    private void handleGroupPermissionChange(boolean isChecked, ModelGroupMember member) {
        updateContributionGroupPermissionToUpdate(member, determinePermissionType(isChecked));
    }

    private PermissionType determinePermissionType(boolean isChecked) {
        if (isChecked) {
            return PermissionType.READ_WRITE;
        }
        return PermissionType.READ;
    }

    private boolean determineIsChecked(PermissionType permissionType) {
        return PermissionType.READ_WRITE.equals(permissionType);
    }

    private void updateContributionGroupPermissionToUpdate(ModelGroupMember member, PermissionType permissionType) {
        long userId = member.getUserId();
        if (contributionsToUpdateByUserId.containsKey(userId)) {
            ContributionUpdateContainer contribution = contributionsToUpdateByUserId.get(userId);
            ContributionUpdateContainer newContribution = contribution.toBuilder()
                    .groupPermissionType(permissionType)
                    .build();
            contributionsToUpdateByUserId.replace(userId, newContribution);
            return;
        }
        ContributionUpdateContainer contribution = ContributionUpdateContainerMapper.from(member).toBuilder()
                .groupPermissionType(permissionType)
                .build();
        contributionsToUpdateByUserId.put(userId, contribution);
    }

    private void updateContributionSetPermissionToUpdate(ModelGroupMember member, PermissionType permissionType) {
        long userId = member.getUserId();
        if (contributionsToUpdateByUserId.containsKey(userId)) {
            ContributionUpdateContainer contribution = contributionsToUpdateByUserId.get(userId);
            ContributionUpdateContainer newContribution = contribution.toBuilder()
                    .setPermissionType(permissionType)
                    .build();
            contributionsToUpdateByUserId.replace(userId, newContribution);
            return;
        }
        ContributionUpdateContainer contribution = ContributionUpdateContainerMapper.from(member).toBuilder()
                .setPermissionType(permissionType)
                .build();
        contributionsToUpdateByUserId.put(userId, contribution);
    }

    public void saveContributions() {
        GroupUpdateContainer groupUpdateContainer = GroupUpdateContainer.builder()
                .id(chosenGroup.id())
                .name(chosenGroup.name())
                .contributions(contributionsToUpdateByUserId.values())
                .build();
        handleContributionUpdate(groupUpdateContainer);
    }

    private void handleContributionUpdate(GroupUpdateContainer groupUpdateContainer) {
        groupService.updateGroupContribution(groupUpdateContainer, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    ResponseHandler.handleResponse(response);
                    ViewUtils.showToast(getContext(), "Permissions updated");
                } catch (MalletException exception) {
                    ViewUtils.showToast(getContext(), exception.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ViewUtils.showToast(getContext(), "Network error");
            }
        });
    }
}