package com.mallet.backend.client.group.control.mapper;

import com.agh.api.GroupSetDTO;

public class GroupSetDTOMapper {

    private GroupSetDTOMapper() {}

    public static GroupSetDTO from(Long groupId, Long setId) {
        return GroupSetDTO.builder()
                .groupId(groupId)
                .setId(setId)
                .build();
    }

}
