package com.mallet.backend.mapper.group;

import com.agh.api.GroupBasicInformationDTO;
import com.mallet.frontend.model.group.ModelGroup;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelGroupMapper {

    private ModelGroupMapper() {}


    public static List<ModelGroup> from(Collection<GroupBasicInformationDTO> groupInformationDTOS){
                return  groupInformationDTOS.stream()
                        .map(ModelGroupMapper::from)
                        .collect(Collectors.toList());
    }

    public static ModelGroup from(GroupBasicInformationDTO groupInformationDTO){
        return ModelGroup.builder()
                .id(groupInformationDTO.id())
                .groupName(groupInformationDTO.name())
                .identifier(groupInformationDTO.identifier())
                .nrOfMembers(String.valueOf(groupInformationDTO.numberOfMembers()))
                .nrOfSets(String.valueOf(groupInformationDTO.numberOfSets()))
                .build();
    }
}
