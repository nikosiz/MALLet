package com.example.mallet.backend.mapper.group;

import com.agh.api.PermissionType;
import com.example.mallet.backend.entity.group.update.ContributionUpdateContainer;
import com.example.mallet.frontend.model.group.ModelGroupMember;
import com.example.mallet.frontend.model.user.ModelUser;

public class ContributionUpdateContainerMapper {
    private ContributionUpdateContainerMapper() {}

    public static ContributionUpdateContainer from(ModelGroupMember member){
        return ContributionUpdateContainer.builder()
                .contributorId(member.getUserId())
                .id(member.getContributionId())
                .setPermissionType(member.getSetPermissionType())
                .groupPermissionType(member.getGroupPermissionType())
                .build();
    }

    public static ContributionUpdateContainer from(Long userId,
                                                   PermissionType setPermissionType,
                                                   PermissionType groupPermissionType){
        return ContributionUpdateContainer.builder()
                .contributorId(userId)
                .setPermissionType(setPermissionType)
                .groupPermissionType(groupPermissionType)
                .build();
    }

    public static ContributionUpdateContainer from(ModelUser modelUser,
                                                   PermissionType setPermissionType,
                                                   PermissionType groupPermissionType){
        return ContributionUpdateContainer.builder()
                .contributorId(modelUser.getId())
                .setPermissionType(setPermissionType)
                .groupPermissionType(groupPermissionType)
                .build();
    }

}
