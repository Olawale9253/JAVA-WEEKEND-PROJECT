package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordReplacerTest {

    private final WordReplacer replacer = new WordReplacer();

    @Test
    void replaceWholeWord_caseInsensitive_allReplaced() {
        assertEquals("Kotlin Kotlin Kotlin",
                replacer.replaceWholeWord("Java JAVA java", "java", "Kotlin"));
    }

    @Test
    void replaceWholeWord_javaDoesNotReplaceInsideJavaScript() {
        assertEquals("Python and JavaScript",
                replacer.replaceWholeWord("Java and JavaScript", "Java", "Python"));
    }

    @Test
    void replaceWholeWord_noMatch_returnsUnchanged() {
        assertEquals("Hello World",
                replacer.replaceWholeWord("Hello World", "Java", "Python"));
    }

    @Test
    void replaceWholeWord_replacementWithDollarSign_isTreatedLiterally() {
        assertEquals("price $5", replacer.replaceWholeWord("price 5", "5", "$5"));
    }
}