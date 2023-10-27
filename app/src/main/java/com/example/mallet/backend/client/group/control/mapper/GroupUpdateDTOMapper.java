package com.example.mallet.backend.client.group.control.mapper;

import com.agh.api.ContributionDTO;
import com.agh.api.GroupUpdateDTO;
import com.example.mallet.backend.entity.group.update.GroupUpdateContainer;

import java.util.List;

public class GroupUpdateDTOMapper {

    private GroupUpdateDTOMapper() {}

    public static GroupUpdateDTO from(GroupUpdateContainer groupUpdateContainer) {
        List<ContributionDTO> contributions = ContributionDTOMapper.fromUpdateContainer(groupUpdateContainer.contributions());

        return GroupUpdateDTO.builder()
                .id(groupUpdateContainer.id())
                .name(groupUpdateContainer.name())
                .contributions(contributions)
                .build();
    }

}
