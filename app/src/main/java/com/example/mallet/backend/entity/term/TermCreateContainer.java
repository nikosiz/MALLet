package com.example.mallet.backend.entity.term;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record TermCreateContainer(
        @NonNull
        String term,
        String definition,
        @NonNull
        Language language,
        TermCreateContainer translation) {
}
