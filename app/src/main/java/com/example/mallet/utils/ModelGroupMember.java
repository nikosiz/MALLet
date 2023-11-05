package com.example.mallet.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Builder;

@Builder
public class ModelGroupMember implements Parcelable {
    private long userId;
    private String username;

    public ModelGroupMember(long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    protected ModelGroupMember(Parcel in) {
        userId = in.readLong();
        username = in.readString();
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