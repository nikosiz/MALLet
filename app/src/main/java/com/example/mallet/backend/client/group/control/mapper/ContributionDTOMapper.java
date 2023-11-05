package com.example.mallet.backend.client.group.control.mapper;

import com.agh.api.ContributionDTO;
import com.agh.api.UserDTO;
import com.example.mallet.backend.client.user.control.mapper.UserDTOMapper;
import com.example.mallet.backend.entity.group.create.ContributionCreateContainer;
import com.example.mallet.backend.entity.group.update.ContributionUpdateContainer;

import java.util.List;
import java.util.stream.Collectors;

public class ContributionDTOMapper {

    private ContributionDTOMapper() {}

    public static List<ContributionDTO> fromUpdateContainer(List<ContributionUpdateContainer> contributions) {
        return contributions.stream()
                .map(ContributionDTOMapper::from)
                .collect(Collectors.toList());
    }

    private static ContributionDTO from(ContributionUpdateContainer contribution) {
        UserDTO contributor = UserDTOMapper.from(contribution.contributorId());

        return ContributionDTO.builder()
                .id(contribution.id())
                .groupPermissionType(contribution.groupPermissionType())
                .setPermissionType(contribution.setPermissionType())
                .contributor(contributor)
                .build();
    }

    public static List<ContributionDTO> fromCreateContainer(List<ContributionCreateContainer> contributions) {
        return contributions.stream()
                .map(ContributionDTOMapper::from)
                .collect(Collectors.toList());
    }

    private static ContributionDTO from(ContributionCreateContainer contribution) {
        UserDTO contributor = UserDTOMapper.from(contribution.contributorId());

        return ContributionDTO.builder()
                .groupPermissionType(contribution.groupPermissionType())
                .setPermissionType(contribution.setPermissionType())
                .contributor(contributor)
                .build();
    }

}
