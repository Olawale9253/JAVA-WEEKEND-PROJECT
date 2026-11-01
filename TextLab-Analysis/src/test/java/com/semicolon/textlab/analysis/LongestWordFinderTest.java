package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongestWordFinderTest {

    private final LongestWordFinder finder = new LongestWordFinder();

    @Test
    void findLongestWords_singleLongest_returnsOneWord() {
        List<String> result = finder.findLongestWords("a bb ccc d");
        assertEquals(List.of("ccc"), result);
    }

    @Test
    void findLongestWords_tiedLongest_returnsAll() {
        List<String> result = finder.findLongestWords(
                "The quick brown fox jumps over the lazy dog");
        assertEquals(3, result.size());
        assertTrue(result.contains("quick"));
        assertTrue(result.contains("brown"));
        assertTrue(result.contains("jumps"));
    }

    @Test
    void findLongestWords_duplicatesCaseInsensitive_returnsOnce() {
        List<String> result = finder.findLongestWords("Cat bat CAT");
        assertEquals(2, result.size());
        assertTrue(result.contains("Cat") || result.contains("CAT"));
        assertTrue(result.contains("bat"));
    }

    @Test
    void findLongestWords_emptyText_returnsEmptyList() {
        assertTrue(finder.findLongestWords("").isEmpty());
    }

    @Test
    void findLongestWords_punctuationOnly_returnsEmptyList() {
        assertTrue(finder.findLongestWords("!@#$%^&*()").isEmpty());
    }
}