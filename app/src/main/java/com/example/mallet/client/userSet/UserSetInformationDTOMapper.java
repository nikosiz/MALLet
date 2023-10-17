package com.example.mallet.client.userSet;

import com.agh.api.UserSetInformationDTO;

public class UserSetInformationDTOMapper {

    private UserSetInformationDTOMapper() {
    }

    public static UserSetInformationDTO from(long id, String name, String description,
                                             String creator,
                                             UserSetInformationDTO.PermissionType permissionType, String nextChunkUri) {
        return UserSetInformationDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .creator(creator)
                .permissionType(permissionType)
                .nextChunkUri(nextChunkUri)
                .build();
    }

}
