package com.example.mallet;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private String groupName;
    private final List<String> members;

    // TODO: Change to IDs or some other identification method
    private final List<String> sets;
    private String setAmount;

    public ModelGroup(String groupName,String setAmount) {
        this.groupName = groupName;
        this.members = new ArrayList<>();
        this.sets = new ArrayList<>();
        this.setAmount = setAmount;
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

    public String getSetAmount() {
        return setAmount;
    }

    public void setSetAmount(String setAmount) {
        this.setAmount = setAmount;
    }
}
