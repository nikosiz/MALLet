package com.example.mallet.backend.entity.group.contribution;

import com.agh.api.ContributionDTO;
import com.agh.api.UserDTO;
import com.example.mallet.frontend.model.group.ModelGroupMember;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelGroupMemberMapper {

    private ModelGroupMemberMapper() {}


    public static List<ModelGroupMember> from(Collection<ContributionDTO> contributionDTOS){
        return  contributionDTOS.stream()
                .map(ModelGroupMemberMapper::from)
                .collect(Collectors.toList());
    }

    public static ModelGroupMember from(ContributionDTO contributionDTO){
        UserDTO contributor = contributionDTO.contributor();

        return new ModelGroupMember(contributor.id(),contributor.name(), contributionDTO.id(),contributionDTO.setPermissionType(), contributionDTO.groupPermissionType());
    }
}
