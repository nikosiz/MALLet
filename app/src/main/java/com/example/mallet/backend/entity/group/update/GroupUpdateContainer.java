package com.example.mallet.backend.entity.group.update;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Builder;

@Builder
public record GroupUpdateContainer(
        @NonNull
        Long id,
        String name,
        @NonNull
        List<ContributionUpdateContainer> contributions) {
}
