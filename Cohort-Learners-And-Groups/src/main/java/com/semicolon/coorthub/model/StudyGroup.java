package com.semicolon.coorthub.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class StudyGroup {

    private final String groupName;
    private final Set<Learner> members;

    public StudyGroup(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name must not be blank.");
        }
        this.groupName = groupName.trim();
        this.members = new LinkedHashSet<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean addMember(Learner learner) {
        return members.add(learner);
    }

    public boolean removeMember(Learner learner) {
        return members.remove(learner);
    }

    public boolean hasMember(Learner learner) {
        return members.contains(learner);
    }

    public int getMemberCount() {
        return members.size();
    }

    public Set<Learner> getMembers() {
        return Collections.unmodifiableSet(members);
    }
}