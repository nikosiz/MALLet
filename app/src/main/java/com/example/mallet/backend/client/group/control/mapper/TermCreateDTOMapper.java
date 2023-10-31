package com.example.mallet.backend.client.group.control.mapper;

import com.agh.api.TermCreateDTO;
import com.example.mallet.backend.entity.term.TermCreateContainer;
import com.example.mallet.backend.utils.LanguageConverter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TermCreateDTOMapper {

    private TermCreateDTOMapper() {}

    public static List<TermCreateDTO> from(Collection<TermCreateContainer> termCreateContainers){
        return termCreateContainers.stream()
                .map(TermCreateDTOMapper::from)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    private static Optional<TermCreateDTO> from(TermCreateContainer termCreateContainer) {
        return Optional.ofNullable(termCreateContainer.translation())
                .flatMap(TermCreateDTOMapper::from)
                .map(translation -> from(termCreateContainer, translation));
    }

    private static TermCreateDTO from(TermCreateContainer termCreateContainer, TermCreateDTO translation) {
        return TermCreateDTO.builder()
                .term(termCreateContainer.term())
                .definition(termCreateContainer.definition())
                .language(LanguageConverter.convert(termCreateContainer.language()))
                .translation(translation)
                .build();
    }

}
