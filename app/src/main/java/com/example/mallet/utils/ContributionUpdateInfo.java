package com.example.mallet.utils;

import com.agh.api.PermissionType;

public class ContributionUpdateInfo {
    private final ModelGroupMember member;
    private final PermissionType setPermissionType;
    private final PermissionType groupPermissionType;

    public ContributionUpdateInfo(ModelGroupMember member, PermissionType setPermissionType, PermissionType groupPermissionType) {
        this.member = member;
        this.setPermissionType = setPermissionType;
        this.groupPermissionType = groupPermissionType;
    }

    public ModelGroupMember getMember() {
        return member;
    }

    public PermissionType getSetPermissionType() {
        return setPermissionType;
    }

    public PermissionType getGroupPermissionType() {
        return groupPermissionType;
    }
}
