package com.example.mallet.backend.entity.group.create;

import java.util.List;

import lombok.NonNull;

public record GroupCreateContainer(
        @NonNull
        String name,
        @NonNull
        List<ContributionCreateContainer> contributions) {
}
