package com.example.mallet.utils;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private String groupName;
    private final List<String> members;
    private final List<String> sets; // TODO: Change to IDs or some other identification method
    private String numberOfSets, numberOfMembers;

    public ModelGroup(String groupName, String numberOfSets, String numberOfMembers) {
        this.groupName = groupName;
        this.members = new ArrayList<>(); // Initialize the list of members
        this.sets = new ArrayList<>(); // Initialize the list of sets
        this.numberOfSets = numberOfSets;
        this.numberOfMembers = numberOfMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void addMember(String username) {
        members.add(username);
    }

    public List<String> getSets() {
        return sets;
    }

    public void addSet(String setNameOrId) {
        sets.add(setNameOrId);
    }

    public String getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(String numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public String getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(String numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }
}