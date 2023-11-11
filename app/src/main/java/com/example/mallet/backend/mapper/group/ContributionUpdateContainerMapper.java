package com.example.mallet.backend.mapper.group;

import com.agh.api.PermissionType;
import com.example.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.example.mallet.backend.entity.group.update.ContributionUpdateContainer;
import com.example.mallet.utils.ModelUser;

public class ContributionUpdateContainerMapper {
    private ContributionUpdateContainerMapper() {}

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
