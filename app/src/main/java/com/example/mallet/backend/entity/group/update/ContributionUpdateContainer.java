package com.example.mallet.backend.entity.group.update;

import com.agh.api.PermissionType;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ContributionUpdateContainer(
        @NonNull
        Long id,
        @NonNull
        PermissionType setPermissionType,
        @NonNull
        PermissionType groupPermissionType,
        @NonNull
        Long contributorId) {
}
