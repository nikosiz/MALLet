package com.example.mallet;

public class ModelFolder {
    private String folderOwner;
    private String folderName;

    public ModelFolder(String folderOwner, String folderName) {
        this.folderOwner = folderOwner;
        this.folderName = folderName;
    }

    public String getFolderOwner() {
        return folderOwner;
    }

    public String getFolderName() {
        return folderName;
    }
}
