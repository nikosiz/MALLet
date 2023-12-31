package com.mallet.backend.mapper.term;

import com.mallet.backend.entity.term.Language;
import com.mallet.backend.entity.term.TermCreateContainer;
import com.mallet.frontend.model.flashcard.ModelFlashcard;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TermCreateContainerMapper {
    private TermCreateContainerMapper() {
    }

    public static TermCreateContainer from(ModelFlashcard flashcard) {
        String definition;
        if(flashcard.getDefinition().isEmpty()){
            definition = null;
        }
        else{
            definition = flashcard.getDefinition();
        }
        return TermCreateContainer.builder()
                .term(flashcard.getTerm())
                .definition(definition)
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
                .filter(flashcard -> !flashcard.getTerm().isEmpty())
                .map(TermCreateContainerMapper::from)
                .collect(Collectors.toList());
    }
}
