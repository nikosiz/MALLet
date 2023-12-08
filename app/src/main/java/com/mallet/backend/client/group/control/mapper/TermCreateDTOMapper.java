package com.mallet.backend.client.group.control.mapper;

import com.agh.api.TermCreateDTO;
import com.mallet.backend.entity.term.TermCreateContainer;
import com.mallet.backend.utils.LanguageConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TermCreateDTOMapper {

    private TermCreateDTOMapper() {}


    public static List<TermCreateDTO> from(Collection<TermCreateContainer> termCreateContainers) {
        return termCreateContainers.stream()
                .map(TermCreateDTOMapper::from)
                .collect(Collectors.toList());
    }

    private static TermCreateDTO fromTranslation(TermCreateContainer createContainer) {
        return TermCreateDTO.builder()
                .term(createContainer.term())
                .definition(createContainer.definition())
                .language(LanguageConverter.convert(createContainer.language()))
                .build();
    }


    private static TermCreateDTO from(TermCreateContainer createContainer) {
        TermCreateDTO translation =  TermCreateDTOMapper.fromTranslation(createContainer.translation());
        TermCreateDTO.TermCreateDTOBuilder builder = TermCreateDTO.builder();
        return builder
                .term(createContainer.term())
                .definition(createContainer.definition())
                .language(LanguageConverter.convert(createContainer.language()))
                .translation(translation)
                .build();
    }

}
