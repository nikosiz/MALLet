package com.mallet.backend.entity.group.update;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Builder;
import lombok.Singular;

@Builder
public record GroupUpdateContainer(
        @NonNull
        Long id,
        String name,
        @NonNull
                @Singular(value = "contribution")
        List<ContributionUpdateContainer> contributions) {
}
