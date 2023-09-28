package com.example.mallet.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.mallet.databinding.ModelFolderBinding;

public class ModelFolder implements Parcelable {
    private ModelFolderBinding binding; // Data binding for the folder model (if needed)
    private final String folderCreator; // Creator of the folder
    private final String folderName; // Name of the folder
    private String sets; // Information about sets within the folder

    // Constructor to initialize folder data
    public ModelFolder(String folderName, String folderCreator, String sets) {
        this.folderCreator = folderCreator;
        this.folderName = folderName;
        this.sets = sets;
    }

    protected ModelFolder(Parcel in) {
        folderCreator = in.readString();
        folderName = in.readString();
        sets = in.readString();
    }

    public static final Creator<ModelFolder> CREATOR = new Creator<ModelFolder>() {
        @Override
        public ModelFolder createFromParcel(Parcel in) {
            return new ModelFolder(in);
        }

        @Override
        public ModelFolder[] newArray(int size) {
            return new ModelFolder[size];
        }
    };

    // Getter for folder creator
    public String getFolderCreator() {
        return folderCreator;
    }

    // Getter for folder name
    public String getFolderName() {
        return folderName;
    }

    // Getter for sets information
    public String getSets() {
        return sets;
    }

    // Setter for sets information
    public void setSets(String sets) {
        this.sets = sets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(folderCreator);
        parcel.writeString(folderName);
        parcel.writeString(sets);
    }
}