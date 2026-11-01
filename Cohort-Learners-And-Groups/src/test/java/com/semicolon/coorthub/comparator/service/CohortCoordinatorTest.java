package com.semicolon.coorthub.comparator.service;

import com.semicolon.coorthub.exception.LearnerNotFoundException;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.service.CohortCoordinator;
import com.semicolon.coorthub.service.LearnerService;
import com.semicolon.coorthub.service.MentorSupportService;
import com.semicolon.coorthub.service.StudyGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CohortCoordinatorTest {

    private LearnerService learnerService;
    private StudyGroupService studyGroupService;
    private MentorSupportService mentorSupportService;
    private CohortCoordinator cohortCoordinator;

    private Learner alice;
    private Learner bob;

    @BeforeEach
    void setUp() {
        learnerService = new LearnerService();
        studyGroupService = new StudyGroupService();
        mentorSupportService = new MentorSupportService();
        cohortCoordinator = new CohortCoordinator(
                learnerService, studyGroupService, mentorSupportService);

        alice = new Learner("L001", "Alice", "alice@example.com");
        bob = new Learner("L002", "Bob", "bob@example.com");
        learnerService.register(alice);
        learnerService.register(bob);
        studyGroupService.createGroup("Java");
        studyGroupService.createGroup("Python");
    }

    // ---- the acceptance-check test ----

    @Test
    void removeLearner_learnerInGroupsAndQueue_leavesNoStaleReferences() {
        cohortCoordinator.addLearnerToGroup("L001", "Java");
        cohortCoordinator.addLearnerToGroup("L001", "Python");
        cohortCoordinator.requestSupport("L001");
        cohortCoordinator.requestSupport("L002");

        cohortCoordinator.removeLearner("L001");

        assertFalse(learnerService.exists("L001"));
        assertEquals(0, studyGroupService.getMembershipCount("Java"));
        assertEquals(0, studyGroupService.getMembershipCount("Python"));
        assertFalse(mentorSupportService.isWaiting(alice));
        assertEquals(1, mentorSupportService.getWaitingCount());
        assertTrue(mentorSupportService.isWaiting(bob));
    }

    // ---- other coordinator operations ----

    @Test
    void removeLearner_missingLearner_throwsLearnerNotFoundException() {
        assertThrows(LearnerNotFoundException.class,
                () -> cohortCoordinator.removeLearner("L999"));
    }

    @Test
    void addLearnerToGroup_missingLearner_throwsLearnerNotFoundException() {
        assertThrows(LearnerNotFoundException.class,
                () -> cohortCoordinator.addLearnerToGroup("L999", "Java"));
    }

    @Test
    void getGroupNamesForLearner_memberOfTwoGroups_returnsBoth() {
        cohortCoordinator.addLearnerToGroup("L001", "Java");
        cohortCoordinator.addLearnerToGroup("L001", "Python");
        List<String> names = cohortCoordinator.getGroupNamesForLearner("L001");
        assertEquals(2, names.size());
    }

    @Test
    void getLearnersInBothGroups_intersection() {
        cohortCoordinator.addLearnerToGroup("L001", "Java");
        cohortCoordinator.addLearnerToGroup("L001", "Python");
        cohortCoordinator.addLearnerToGroup("L002", "Java");

        List<Learner> both = cohortCoordinator.getLearnersInBothGroups("Java", "Python");

        assertEquals(1, both.size());
        assertEquals(alice, both.get(0));
    }

    @Test
    void getLearnersInEitherButNotBothGroups_symmetricDifference() {
        cohortCoordinator.addLearnerToGroup("L001", "Java");
        cohortCoordinator.addLearnerToGroup("L001", "Python");
        cohortCoordinator.addLearnerToGroup("L002", "Java");

        List<Learner> eitherOr = cohortCoordinator.getLearnersInEitherButNotBothGroups(
                "Java", "Python");

        assertEquals(1, eitherOr.size());
        assertEquals(bob, eitherOr.get(0));
    }
}