package com.mallet.mallet.frontend.model.group;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.agh.api.PermissionType;

public class ModelGroupMember implements Parcelable {
    private long userId;
    private String username;
    private Long contributionId;
   private PermissionType setPermissionType, groupPermissionType;

    public ModelGroupMember(long userId, String username, Long contributionId, PermissionType setPermissionType, PermissionType groupPermissionType) {
        this.userId = userId;
        this.username = username;
        this.contributionId = contributionId;
        this.setPermissionType = setPermissionType;
        this.groupPermissionType = groupPermissionType;
    }

    protected ModelGroupMember(Parcel in) {
        userId = in.readLong();
        username = in.readString();
    }

    public PermissionType getSetPermissionType() {
        return setPermissionType;
    }

    public PermissionType getGroupPermissionType() {
        return groupPermissionType;
    }

    public static final Creator<ModelGroupMember> CREATOR = new Creator<ModelGroupMember>() {
        @Override
        public ModelGroupMember createFromParcel(Parcel in) {
            return new ModelGroupMember(in);
        }

        @Override
        public ModelGroupMember[] newArray(int size) {
            return new ModelGroupMember[size];
        }
    };

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    // Setter for the creator of the learning set
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getContributionId() {
        return contributionId;
    }

    public void setContributionId(Long contributionId) {
        this.contributionId = contributionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(userId);
        parcel.writeString(username);
    }
}