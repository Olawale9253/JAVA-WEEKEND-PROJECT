package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WhitespaceCleanerTest {

    private final WhitespaceCleaner cleaner = new WhitespaceCleaner();

    @Test
    void clean_leadingAndTrailingWhitespace_removed() {
        assertEquals("Hello World", cleaner.clean("   Hello World   "));
    }

    @Test
    void clean_repeatedSpaces_collapseToOneSpace() {
        assertEquals("Hello World", cleaner.clean("Hello     World"));
    }

    @Test
    void clean_tabsAndNewlines_collapseToOneSpace() {
        assertEquals("Hello World Again",
                cleaner.clean("Hello\t\nWorld\n\nAgain"));
    }

    @Test
    void clean_whitespaceOnlyText_returnsEmptyString() {
        assertEquals("", cleaner.clean("   \t\n   "));
    }

    @Test
    void clean_alreadyCleanText_unchanged() {
        assertEquals("Hello World", cleaner.clean("Hello World"));
    }

    @Test
    void clean_nullText_returnsEmptyString() {
        assertEquals("", cleaner.clean(null));
    }
}