package com.mallet.backend.client.group.control.mapper;

import com.agh.api.ContributionDTO;
import com.agh.api.GroupCreateDTO;
import com.mallet.backend.entity.group.create.GroupCreateContainer;

import java.util.List;

public class GroupCreateDTOMapper {

    private GroupCreateDTOMapper() {
    }

    public static GroupCreateDTO from(GroupCreateContainer groupCreateContainer) {
        List<ContributionDTO> contributions = ContributionDTOMapper.fromCreateContainer(groupCreateContainer.contributions());

        return GroupCreateDTO.builder()
                .name(groupCreateContainer.name())
                .contributions(contributions)
                .build();
    }

}
