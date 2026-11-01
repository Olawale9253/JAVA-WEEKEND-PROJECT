package com.semicolon.coorthub.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LearnerTest {

    @Test
    void equals_sameIdDifferentNameAndEmail_treatedAsEqual() {
        Learner first = new Learner("L001", "Alice Dlamini", "alice@example.com");
        Learner second = new Learner("L001", "Alice Mokoena", "alice.m@example.com");
        assertEquals(first, second);
    }

    @Test
    void equals_differentIds_notEqual() {
        Learner first = new Learner("L001", "Alice Dlamini", "alice@example.com");
        Learner second = new Learner("L002", "Alice Dlamini", "alice@example.com");
        assertNotEquals(first, second);
    }

    @Test
    void hashCode_sameId_sameHashCode() {
        Learner first = new Learner("L001", "Alice Dlamini", "alice@example.com");
        Learner second = new Learner("L001", "Alice Mokoena", "alice.m@example.com");
        assertEquals(first.hashCode(), second.hashCode());
    }
}