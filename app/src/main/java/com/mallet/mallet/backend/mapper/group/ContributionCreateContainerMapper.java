package com.mallet.mallet.backend.mapper.group;

import com.agh.api.PermissionType;
import com.mallet.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.mallet.mallet.frontend.model.user.ModelUser;

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
