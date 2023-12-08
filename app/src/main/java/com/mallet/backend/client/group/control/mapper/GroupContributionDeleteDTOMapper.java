package com.mallet.backend.client.group.control.mapper;

import com.agh.api.GroupContributionDeleteDTO;

import java.util.Set;

public class GroupContributionDeleteDTOMapper {

    private GroupContributionDeleteDTOMapper() {}

    public static GroupContributionDeleteDTO from(Long groupId,
                                                  Set<Long> contributionsToDeleteIds) {
        return GroupContributionDeleteDTO.builder()
                .groupId(groupId)
                .contributionsToDeleteIds(contributionsToDeleteIds)
                .build();
    }

}
