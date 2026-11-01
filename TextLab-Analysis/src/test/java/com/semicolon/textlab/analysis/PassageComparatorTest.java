package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PassageComparatorTest {

    private final PassageComparator comparator = new PassageComparator();

    @Test
    void findFirstDifferencePosition_identicalPassages_returnsMinusOne() {
        assertEquals(-1, comparator.findFirstDifferencePosition("Hello", "Hello"));
    }

    @Test
    void findFirstDifferencePosition_caseDiffersAtPosition6_returnsSix() {
        assertEquals(6,
                comparator.findFirstDifferencePosition("Hello World", "Hello world"));
    }

    @Test
    void findFirstDifferencePosition_oneIsPrefixOfOther_returnsShorterLength() {
        assertEquals(5,
                comparator.findFirstDifferencePosition("Hello", "Hello World"));
    }
}