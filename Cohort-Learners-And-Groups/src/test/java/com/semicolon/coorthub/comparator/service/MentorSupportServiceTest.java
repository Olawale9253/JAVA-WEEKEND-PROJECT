package com.semicolon.coorthub.comparator.service;

import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.service.MentorSupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MentorSupportServiceTest {

    private MentorSupportService mentorSupportService;

    @BeforeEach
    void setUp() {
        mentorSupportService = new MentorSupportService();
    }

    @Test
    void requestSupport_increasesWaitingCount() {
        mentorSupportService.requestSupport(
                new Learner("L001", "Alice", "alice@example.com"));
        assertEquals(1, mentorSupportService.getWaitingCount());
    }

    @Test
    void attendNextLearner_followsArrivalOrder() {
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        Learner bob = new Learner("L002", "Bob", "bob@example.com");
        Learner charlie = new Learner("L003", "Charlie", "charlie@example.com");
        mentorSupportService.requestSupport(alice);
        mentorSupportService.requestSupport(bob);
        mentorSupportService.requestSupport(charlie);

        assertEquals(alice, mentorSupportService.attendNextLearner());
        assertEquals(bob, mentorSupportService.attendNextLearner());
        assertEquals(charlie, mentorSupportService.attendNextLearner());
    }

    @Test
    void attendNextLearner_emptyQueue_returnsNull() {
        assertNull(mentorSupportService.attendNextLearner());
    }

    @Test
    void removeLearnerFromQueue_learnerQueuedOnce_removesIt() {
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        mentorSupportService.requestSupport(alice);
        mentorSupportService.removeLearnerFromQueue(alice);
        assertEquals(0, mentorSupportService.getWaitingCount());
        assertFalse(mentorSupportService.isWaiting(alice));
    }

    @Test
    void removeLearnerFromQueue_learnerQueuedMultipleTimes_removesAllOccurrences() {
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        Learner bob = new Learner("L002", "Bob", "bob@example.com");
        mentorSupportService.requestSupport(alice);
        mentorSupportService.requestSupport(bob);
        mentorSupportService.requestSupport(alice);

        mentorSupportService.removeLearnerFromQueue(alice);

        assertEquals(1, mentorSupportService.getWaitingCount());
        assertTrue(mentorSupportService.isWaiting(bob));
    }
}