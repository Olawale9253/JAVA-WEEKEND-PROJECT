package com.semicolon.coorthub.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudyGroupTest {

    @Test
    void addMember_newLearner_returnsTrueAndIncreasesCount() {
        StudyGroup group = new StudyGroup("Java Study Group");
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        assertTrue(group.addMember(alice));
        assertEquals(1, group.getMemberCount());
    }

    @Test
    void addMember_sameLearnerTwice_keepsOneMembership() {
        StudyGroup group = new StudyGroup("Java Study Group");
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        group.addMember(alice);
        assertFalse(group.addMember(alice));
        assertEquals(1, group.getMemberCount());
    }

    @Test
    void addMember_learnerWithSameIdButDifferentData_treatedAsSameMember() {
        StudyGroup group = new StudyGroup("Java Study Group");
        Learner original = new Learner("L001", "Alice", "alice@example.com");
        Learner duplicateId = new Learner("L001", "Alice M", "alice.m@example.com");
        group.addMember(original);
        assertFalse(group.addMember(duplicateId));
        assertEquals(1, group.getMemberCount());
    }
}