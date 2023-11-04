package com.example.mallet.backend.entity.term;

import com.example.mallet.utils.ModelFlashcard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TermCreateContainerMapper {
    private TermCreateContainerMapper() {
    }

    public static TermCreateContainer from(ModelFlashcard flashcard) {
        return TermCreateContainer.builder()
                .term(flashcard.getTerm())
                .definition(flashcard.getDefinition())
                .translation(TermCreateContainerMapper.from(flashcard.getTranslation()))
                .language(Language.EN)
                .build();
    }

    public static TermCreateContainer from(String term) {
        return TermCreateContainer.builder()
                .term(term)
                .language(Language.PL)
                .build();
    }

    public static List<TermCreateContainer> from(Collection<ModelFlashcard> flashcards) {
        return flashcards.stream()
                .map(TermCreateContainerMapper::from)
                .collect(Collectors.toList());
    }
}
