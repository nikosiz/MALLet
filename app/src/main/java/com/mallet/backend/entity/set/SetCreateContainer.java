package com.mallet.backend.entity.set;

import com.mallet.backend.entity.term.TermCreateContainer;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SetCreateContainer(
        @NonNull String topic,
        String description,
        @NonNull List<Long> existingTermIds,
        @NonNull List<TermCreateContainer> termsToCreate) {
}
