package com.example.mallet.utils;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private String groupName;
    private final List<String> members;
    private final List<String> sets; // TODO: Change to IDs or some other identification method
    private String nrOfSets, nrOfMembers;

    public ModelGroup(String groupName, String nrOfSets, String nrOfMembers) {
        this.groupName = groupName;
        this.members = new ArrayList<>(); // Initialize the list of members
        this.sets = new ArrayList<>(); // Initialize the list of sets
        this.nrOfSets = nrOfSets;
        this.nrOfMembers = nrOfMembers;
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

    public String getNrOfSets() {
        return nrOfSets;
    }

    public void setNrOfSets(String nrOfSets) {
        this.nrOfSets = nrOfSets;
    }

    public String getNrOfMembers() {
        return nrOfMembers;
    }

    public void setNrOfMembers(String nrOfMembers) {
        this.nrOfMembers = nrOfMembers;
    }
}