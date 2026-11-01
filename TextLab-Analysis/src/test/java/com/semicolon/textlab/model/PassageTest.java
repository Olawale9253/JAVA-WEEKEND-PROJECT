package com.semicolon.textlab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PassageTest {

    @Test
    void getOriginalText_remainsUnchangedWhenCurrentTextEdited() {
        Passage passage = new Passage("Original");
        passage.setCurrentText("Modified");
        assertEquals("Original", passage.getOriginalText());
        assertEquals("Modified", passage.getCurrentText());
    }

    @Test
    void resetToOriginal_restoresCurrentText() {
        Passage passage = new Passage("Original");
        passage.setCurrentText("Modified");
        passage.resetToOriginal();
        assertEquals("Original", passage.getCurrentText());
    }

    @Test
    void constructor_nullText_treatedAsEmptyString() {
        Passage passage = new Passage(null);
        assertEquals("", passage.getOriginalText());
        assertEquals("", passage.getCurrentText());
    }
}