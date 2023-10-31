package com.example.mallet.backend.client.group.control.mapper;

import com.agh.api.GroupSetCreateDTO;
import com.agh.api.SetCreateDTO;
import com.example.mallet.backend.entity.set.SetCreateContainer;

public class GroupSetCreateDTOMapper {

    private GroupSetCreateDTOMapper() {}

    public static GroupSetCreateDTO from(Long groupId,
                                         SetCreateContainer setCreateContainer) {
        SetCreateDTO setCreateDTO = SetCreateDTOMapper.from(setCreateContainer);

        return GroupSetCreateDTO.builder()
                .groupId(groupId)
                .set(setCreateDTO)
                .build();
    }

}
