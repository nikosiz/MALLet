package com.mallet.backend.mapper.set;

import com.mallet.backend.entity.set.SetCreateContainer;
import com.mallet.backend.mapper.term.TermCreateContainerMapper;
import com.mallet.frontend.model.set.ModelLearningSet;

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
