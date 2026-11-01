package com.semicolon.coorthub.comparator.service;

import com.semicolon.coorthub.exception.DuplicateEmailException;
import com.semicolon.coorthub.exception.DuplicateLearnerIdException;
import com.semicolon.coorthub.exception.LearnerNotFoundException;
import com.semicolon.coorthub.model.Learner;
import com.semicolon.coorthub.service.LearnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LearnerServiceTest {

    private LearnerService learnerService;

    @BeforeEach
    void setUp() {
        learnerService = new LearnerService();
    }

    @Test
    void register_newLearner_findByIdReturnsIt() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        assertEquals("Alice", learnerService.findById("L001").getFullName());
    }

    @Test
    void register_duplicateId_throwsDuplicateLearnerIdException() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        assertThrows(DuplicateLearnerIdException.class,
                () -> learnerService.register(
                        new Learner("L001", "Bob", "bob@example.com")));
    }

    @Test
    void register_duplicateEmailDifferentCase_throwsDuplicateEmailException() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        assertThrows(DuplicateEmailException.class,
                () -> learnerService.register(
                        new Learner("L002", "Bob", "ALICE@example.com")));
    }

    @Test
    void findById_missingLearner_throwsLearnerNotFoundException() {
        assertThrows(LearnerNotFoundException.class,
                () -> learnerService.findById("L999"));
    }

    @Test
    void updateLearner_newEmail_allowsReusingOldEmailForAnotherLearner() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        learnerService.updateLearner("L001", "Alice M", "alice.m@example.com");
        learnerService.register(new Learner("L002", "Bob", "alice@example.com"));
        assertEquals(2, learnerService.getTotalLearners());
    }

    @Test
    void updateLearner_emailTakenByAnother_throwsDuplicateEmailException() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        learnerService.register(new Learner("L002", "Bob", "bob@example.com"));
        assertThrows(DuplicateEmailException.class,
                () -> learnerService.updateLearner("L001", "Alice", "bob@example.com"));
    }

    @Test
    void getLearnersSortedBy_returnsNameThenIdOrder() {
        learnerService.register(new Learner("L003", "Brenda", "brenda@example.com"));
        learnerService.register(new Learner("L002", "Alice", "alice2@example.com"));
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));

        List<Learner> sorted = learnerService.getLearnersSortedBy();

        assertEquals("L001", sorted.get(0).getLearnerId());
        assertEquals("L002", sorted.get(1).getLearnerId());
        assertEquals("L003", sorted.get(2).getLearnerId());
    }

    @Test
    void getLearnersSortedBy_doesNotCorruptIdLookup() {
        learnerService.register(new Learner("L003", "Brenda", "brenda@example.com"));
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));

        learnerService.getLearnersSortedBy();

        assertEquals("Brenda", learnerService.findById("L003").getFullName());
        assertEquals("Alice", learnerService.findById("L001").getFullName());
    }

    @Test
    void removeLearner_existing_removesFromMapAndFreesEmail() {
        learnerService.register(new Learner("L001", "Alice", "alice@example.com"));
        learnerService.removeLearner("L001");
        assertEquals(0, learnerService.getTotalLearners());
        learnerService.register(new Learner("L002", "Bob", "alice@example.com"));
    }
}