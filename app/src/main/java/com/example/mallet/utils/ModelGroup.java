package com.example.mallet.utils;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private String groupName; // Name of the group
    private final List<String> members; // List of group members
    private final List<String> sets; // List of sets associated with the group (TODO: Change to IDs or some other identification method)
    private String numberOfSets; // Amount of sets in the group

    // Constructor to initialize group data
    public ModelGroup(String groupName, String numberOfSets) {
        this.groupName = groupName;
        this.members = new ArrayList<>(); // Initialize the list of members
        this.sets = new ArrayList<>(); // Initialize the list of sets
        this.numberOfSets = numberOfSets;
    }

    // Getter for group name
    public String getGroupName() {
        return groupName;
    }

    // Setter for group name
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // Getter for the list of members
    public List<String> getMembers() {
        return members;
    }

    // Method to add a member to the group
    public void addMember(String username) {
        members.add(username);
    }

    // Getter for the list of sets associated with the group
    public List<String> getSets() {
        return sets;
    }

    // Method to add a set to the group
    public void addSet(String setNameOrId) {
        sets.add(setNameOrId);
    }

    // Getter for the set amount
    public String getNumberOfSets() {
        return numberOfSets;
    }

    // Setter for the set amount
    public void setNumberOfSets(String numberOfSets) {
        this.numberOfSets = numberOfSets;
    }
}