package com.mallet.backend.entity.group.update;

import com.agh.api.PermissionType;

import lombok.Builder;
import lombok.NonNull;

@Builder(toBuilder = true)
public record ContributionUpdateContainer(
        Long id,
        @NonNull
        PermissionType setPermissionType,
        @NonNull
        PermissionType groupPermissionType,
        @NonNull
        Long contributorId) {
}
