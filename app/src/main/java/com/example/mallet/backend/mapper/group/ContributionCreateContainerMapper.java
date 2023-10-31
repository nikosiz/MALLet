package com.example.mallet.backend.mapper.group;

import com.agh.api.PermissionType;
import com.example.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.example.mallet.utils.ModelUser;

public class ContributionCreateContainerMapper {
    private ContributionCreateContainerMapper() {}

    public static ContributionCreateContainer from(ModelUser modelUser,
                                                   PermissionType setPermissionType,
                                                   PermissionType groupPermissionType){
        return ContributionCreateContainer.builder()
                .contributorId(modelUser.getId())
                .setPermissionType(setPermissionType)
                .groupPermissionType(groupPermissionType)
                .build();
    }

}
