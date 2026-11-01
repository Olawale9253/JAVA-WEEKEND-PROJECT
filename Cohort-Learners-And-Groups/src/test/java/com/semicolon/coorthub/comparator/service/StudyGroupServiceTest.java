package com.semicolon.coorthub.comparator.service;

import com.semicolon.coorthub.exception.DuplicateGroupException;
import com.semicolon.coorthub.exception.GroupNotFoundException;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.service.StudyGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudyGroupServiceTest {

    private StudyGroupService studyGroupService;

    @BeforeEach
    void setUp() {
        studyGroupService = new StudyGroupService();
    }

    @Test
    void createGroup_newGroup_increasesGroupCount() {
        studyGroupService.createGroup("Java");
        assertEquals(1, studyGroupService.getGroupCount());
    }

    @Test
    void createGroup_duplicateName_throwsDuplicateGroupException() {
        studyGroupService.createGroup("Java");
        assertThrows(DuplicateGroupException.class,
                () -> studyGroupService.createGroup("Java"));
    }

    @Test
    void findGroup_missingGroup_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class,
                () -> studyGroupService.findGroup("Nonexistent"));
    }

    @Test
    void addLearnerToGroup_sameLearnerTwice_doesNotDuplicate() {
        studyGroupService.createGroup("Java");
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        assertTrue(studyGroupService.addLearnerToGroup(alice, "Java"));
        assertFalse(studyGroupService.addLearnerToGroup(alice, "Java"));
        assertEquals(1, studyGroupService.getMembershipCount("Java"));
    }

    @Test
    void getGroupNamesForLearner_memberOfTwoGroups_returnsBoth() {
        studyGroupService.createGroup("Java");
        studyGroupService.createGroup("Python");
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        studyGroupService.addLearnerToGroup(alice, "Java");
        studyGroupService.addLearnerToGroup(alice, "Python");

        List<String> groupNames = studyGroupService.getGroupNamesForLearner(alice);

        assertEquals(2, groupNames.size());
        assertTrue(groupNames.contains("Java"));
        assertTrue(groupNames.contains("Python"));
    }

    @Test
    void removeLearnerFromAllGroups_learnerInTwoGroups_leavesBothEmpty() {
        studyGroupService.createGroup("Java");
        studyGroupService.createGroup("Python");
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        studyGroupService.addLearnerToGroup(alice, "Java");
        studyGroupService.addLearnerToGroup(alice, "Python");

        studyGroupService.removeLearnerFromAllGroups(alice);

        assertEquals(0, studyGroupService.getMembershipCount("Java"));
        assertEquals(0, studyGroupService.getMembershipCount("Python"));
    }
}