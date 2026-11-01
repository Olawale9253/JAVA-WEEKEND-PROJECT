package com.semicolon.coorthub.service;

import com.semicolon.coorthub.exception.DuplicateGroupException;
import com.semicolon.coorthub.exception.GroupNotFoundException;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.model.StudyGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyGroupService {

    private final Map<String, StudyGroup> groupsByName;

    public StudyGroupService() {
        this.groupsByName = new HashMap<>();
    }

    public void createGroup(String groupName) {
        if (groupsByName.containsKey(groupName)) {
            throw new DuplicateGroupException(
                    "Group '" + groupName + "' already exists.");
        }
        groupsByName.put(groupName, new StudyGroup(groupName));
    }

    public StudyGroup findGroup(String groupName) {
        StudyGroup group = groupsByName.get(groupName);
        if (group == null) {
            throw new GroupNotFoundException(
                    "No group named '" + groupName + "'.");
        }
        return group;
    }

    public boolean exists(String groupName) {
        return groupsByName.containsKey(groupName);
    }

    public boolean addLearnerToGroup(Learner learner, String groupName) {
        return findGroup(groupName).addMember(learner);
    }

    /** Used by the coordinator when a learner is removed from the cohort. */
    public void removeLearnerFromAllGroups(Learner learner) {
        for (StudyGroup group : groupsByName.values()) {
            group.removeMember(learner);
        }
    }

    public List<String> getGroupNamesForLearner(Learner learner) {
        List<String> groupNames = new ArrayList<>();
        for (StudyGroup group : groupsByName.values()) {
            if (group.hasMember(learner)) {
                groupNames.add(group.getGroupName());
            }
        }
        return groupNames;
    }

    public int getGroupCount() {
        return groupsByName.size();
    }

    public int getMembershipCount(String groupName) {
        return findGroup(groupName).getMemberCount();
    }

    public List<StudyGroup> getAllGroups() {
        return new ArrayList<>(groupsByName.values());
    }
}