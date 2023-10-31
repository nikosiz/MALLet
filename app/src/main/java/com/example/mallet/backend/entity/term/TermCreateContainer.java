package com.example.mallet.backend.entity.term;

import lombok.NonNull;

public record TermCreateContainer(
        @NonNull
        String term,
        String definition,
        @NonNull
        Language language,
        TermCreateContainer translation) {
}
