package com.mallet.backend.client.group.control.mapper;

import com.agh.api.SetCreateDTO;
import com.agh.api.TermCreateDTO;
import com.mallet.backend.entity.set.SetCreateContainer;

import java.util.List;

public class SetCreateDTOMapper {

    private SetCreateDTOMapper() {}

    public static SetCreateDTO from(SetCreateContainer setCreateContainer) {
        List<TermCreateDTO> termsToCreate = TermCreateDTOMapper.from(setCreateContainer.termsToCreate());

        return SetCreateDTO.builder()
                .topic(setCreateContainer.topic())
                .description(setCreateContainer.description())
                .existingTermIds(setCreateContainer.existingTermIds())
                .termsToCreate(termsToCreate)
                .build();
    }

}
