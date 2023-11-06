package com.example.mallet.backend.entity.term;

import com.agh.api.TermDTO;
import com.example.mallet.utils.ModelFlashcard;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelFlashcardMapper {
    private ModelFlashcardMapper() {}

    public static List<ModelFlashcard> from(Collection<TermDTO> termDTOS){
        return termDTOS.stream()
                .map(ModelFlashcardMapper::from)
                .collect(Collectors.toList());
    }

    public static ModelFlashcard from(TermDTO termDTO){
        ModelFlashcard.ModelFlashcardBuilder builder = ModelFlashcard.builder();

        Optional.ofNullable(termDTO.definition())
                .ifPresent(builder::definition);

        return builder
                .term(termDTO.term())
                .translation(termDTO.translation().term())
                .build();
    }
}
