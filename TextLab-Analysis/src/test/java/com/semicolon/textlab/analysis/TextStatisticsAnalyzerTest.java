package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextStatisticsAnalyzerTest {

    private final TextStatisticsAnalyzer analyzer = new TextStatisticsAnalyzer();

    @Test
    void countWords_emptyText_returnsZero() {
        assertEquals(0, analyzer.analyze("").getWordCount());
    }

    @Test
    void countWords_whitespaceOnlyText_returnsZero() {
        assertEquals(0, analyzer.analyze("   \t\n  ").getWordCount());
    }

    @Test
    void countWords_punctuationOnlyText_returnsZero() {
        assertEquals(0, analyzer.analyze("!@#$%^&*()").getWordCount());
    }

    @Test
    void countWords_simplePassage_returnsCorrectCount() {
        assertEquals(5, analyzer.analyze("The quick brown fox jumps").getWordCount());
    }

    @Test
    void countWords_javaAndJavaScript_countsEachAsOneWord() {
        assertEquals(3, analyzer.analyze("Java and JavaScript").getWordCount());
    }

    @Test
    void countSentences_emptyText_returnsZero() {
        assertEquals(0, analyzer.analyze("").getSentenceCount());
    }

    @Test
    void countSentences_whitespaceOnlyText_returnsZero() {
        assertEquals(0, analyzer.analyze("   ").getSentenceCount());
    }

    @Test
    void countSentences_singleSentenceWithoutPunctuation_returnsOne() {
        assertEquals(1, analyzer.analyze("Hello World").getSentenceCount());
    }

    @Test
    void countSentences_singleSentenceWithPeriod_returnsOne() {
        assertEquals(1, analyzer.analyze("Hello World.").getSentenceCount());
    }

    @Test
    void countSentences_twoSentences_returnsTwo() {
        assertEquals(2, analyzer.analyze("Hello. World!").getSentenceCount());
    }

    @Test
    void countSentences_multiplePunctuationOneBoundary_returnsOne() {
        assertEquals(1, analyzer.analyze("Hello...!?").getSentenceCount());
    }

    @Test
    void countSentences_trailingFragmentWithoutPunctuation_countsIt() {
        assertEquals(2, analyzer.analyze("Hello. World").getSentenceCount());
    }


    @Test
    void countCharacters_withAndWithoutWhitespace() {
        TextStatistics statistics = analyzer.analyze("Hello World");
        assertEquals(11, statistics.getCharacterCountWithWhitespace());
        assertEquals(10, statistics.getCharacterCountWithoutWhitespace());
    }

    @Test
    void countCharacters_tabsAndNewlinesCountAsWhitespace() {
        TextStatistics statistics = analyzer.analyze("A\tB\nC");
        assertEquals(5, statistics.getCharacterCountWithWhitespace());
        assertEquals(3, statistics.getCharacterCountWithoutWhitespace());
    }
}