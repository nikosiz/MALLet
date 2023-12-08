package com.mallet.backend.entity.group.create;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.Builder;

@Builder
public record GroupCreateContainer(
        @NonNull
        String name,
        @NonNull
        List<ContributionCreateContainer> contributions) {
}
