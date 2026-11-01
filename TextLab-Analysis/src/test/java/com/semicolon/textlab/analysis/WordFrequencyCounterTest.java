package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordFrequencyCounterTest {

    private final WordFrequencyCounter counter = new WordFrequencyCounter();

    @Test
    void countWholeWord_exactMatch_returnsOne() {
        assertEquals(1, counter.countWholeWord("Java is fun", "Java"));
    }

    @Test
    void countWholeWord_caseInsensitiveVariants_allMatch() {
        assertEquals(3, counter.countWholeWord("Java JAVA java", "java"));
    }

    @Test
    void countWholeWord_javaInJavaScript_notCounted() {
        assertEquals(1, counter.countWholeWord("Java and JavaScript", "Java"));
    }

    @Test
    void countWholeWord_wordNotPresent_returnsZero() {
        assertEquals(0, counter.countWholeWord("Hello World", "Java"));
    }

    @Test
    void countWholeWord_emptySearchWord_returnsZero() {
        assertEquals(0, counter.countWholeWord("Hello World", ""));
    }

    @Test
    void countWholeWord_nullText_returnsZero() {
        assertEquals(0, counter.countWholeWord(null, "Java"));
    }
}