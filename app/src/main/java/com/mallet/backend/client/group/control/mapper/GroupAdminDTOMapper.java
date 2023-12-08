package com.mallet.backend.client.group.control.mapper;

import com.agh.api.GroupUpdateAdminDTO;

public class GroupAdminDTOMapper {

    private GroupAdminDTOMapper() {}

    public static GroupUpdateAdminDTO from(Long groupId, Long newGroupAdminId) {
        return GroupUpdateAdminDTO.builder()
                .groupId(groupId)
                .newAdminId(newGroupAdminId)
                .build();
    }
}
