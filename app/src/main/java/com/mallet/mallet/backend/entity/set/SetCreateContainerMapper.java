package com.mallet.mallet.backend.entity.set;

import com.mallet.mallet.backend.entity.term.TermCreateContainerMapper;
import com.mallet.mallet.frontend.model.set.ModelLearningSet;

import java.util.Collections;

public class SetCreateContainerMapper {
    private SetCreateContainerMapper() {
    }

    public static SetCreateContainer from(ModelLearningSet set) {
        return SetCreateContainer.builder()
                .topic(set.getName())
                .description(set.getDescription())
                .existingTermIds(Collections.emptyList())
                .termsToCreate(TermCreateContainerMapper.from(set.getTerms()))
                .build();
    }
}
