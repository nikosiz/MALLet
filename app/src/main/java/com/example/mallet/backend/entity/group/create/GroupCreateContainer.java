package com.example.mallet.backend.entity.group.create;

import androidx.annotation.NonNull;

import java.util.List;

public record GroupCreateContainer(
        @NonNull
        String name,
        @NonNull
        List<ContributionCreateContainer> contributions) {
}
