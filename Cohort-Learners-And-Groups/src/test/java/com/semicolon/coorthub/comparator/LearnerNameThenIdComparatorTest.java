package com.semicolon.coorthub.comparator;

import com.semicolon.coorthub.model.Learner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LearnerNameThenIdComparatorTest {

    private final LearnerNameThenIdComparator comparator =
            new LearnerNameThenIdComparator();

    @Test
    void compare_differentNames_sortsAlphabetically() {
        Learner alice = new Learner("L001", "Alice", "alice@example.com");
        Learner bob = new Learner("L002", "Bob", "bob@example.com");
        assertEquals(-1, comparator.compare(alice, bob));
    }

    @Test
    void compare_sameNameDifferentIds_sortsById() {
        Learner first = new Learner("L001", "Alice", "alice@example.com");
        Learner second = new Learner("L002", "Alice", "alice2@example.com");
        assertEquals(-1, comparator.compare(first, second));
        assertEquals(1, comparator.compare(second, first));
    }

    @Test
    void sort_unsortedList_producesNameThenIdOrder() {
        List<Learner> learners = new ArrayList<>();
        learners.add(new Learner("L003", "Brenda", "brenda@example.com"));
        learners.add(new Learner("L002", "Alice", "alice2@example.com"));
        learners.add(new Learner("L001", "Alice", "alice@example.com"));
        learners.add(new Learner("L004", "Charlie", "charlie@example.com"));

        learners.sort(comparator);

        assertEquals("L001", learners.get(0).getLearnerId());
        assertEquals("L002", learners.get(1).getLearnerId());
        assertEquals("L003", learners.get(2).getLearnerId());
        assertEquals("L004", learners.get(3).getLearnerId());
    }
}