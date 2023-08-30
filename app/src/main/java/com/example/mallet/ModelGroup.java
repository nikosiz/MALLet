package com.example.mallet;

import java.util.ArrayList;
import java.util.List;

public class ModelGroup {
    private String groupName;
    private List<String> memberNicks;

    // TODO: Change to IDs or some other identification method
    private List<String> assignedSetNames;

    public ModelGroup(String groupName) {
        this.groupName = groupName;
        this.memberNicks = new ArrayList<>();
        this.assignedSetNames = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMemberNicks() {
        return memberNicks;
    }

    public void addMember(String nickname) {
        memberNicks.add(nickname);
    }

    public List<String> getAssignedSets() {
        return assignedSetNames;
    }

    public void assignSet(String setNameOrId) {
        assignedSetNames.add(setNameOrId);
    }
}
