package com.example.mallet.backend.entity.group.create;

import com.agh.api.PermissionType;

import lombok.NonNull;

public record ContributionCreateContainer(
        @NonNull
        PermissionType setPermissionType,
        @NonNull
        PermissionType groupPermissionType,
        @NonNull
        Long contributorId) {
}
