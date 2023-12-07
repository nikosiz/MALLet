package com.mallet.mallet.backend.entity.group.create;

import com.agh.api.PermissionType;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ContributionCreateContainer(
        @NonNull
        PermissionType setPermissionType,
        @NonNull
        PermissionType groupPermissionType,
        @NonNull
        Long contributorId) {
}
