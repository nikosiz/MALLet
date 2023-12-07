package com.mallet.mallet.backend.mapper.group;

import com.agh.api.PermissionType;
import com.mallet.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.mallet.mallet.backend.entity.group.create.GroupCreateContainer;
import com.mallet.mallet.frontend.model.user.ModelUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GroupCreateContainerMapper {

    private GroupCreateContainerMapper() {}

    public static GroupCreateContainer from(String groupName, Collection<ModelUser> users){
        List<ContributionCreateContainer> contributionCreateContainers = users.stream()
                .map(user -> ContributionCreateContainerMapper.from(user, PermissionType.READ, PermissionType.READ))
                .collect(Collectors.toList());

        return GroupCreateContainer.builder()
                .name(groupName)
                .contributions(contributionCreateContainers)
                .build();
    }

}
