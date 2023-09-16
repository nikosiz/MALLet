package com.example.mallet.utils;

import com.example.mallet.databinding.ModelFolderBinding;

public class ModelFolder {
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
}