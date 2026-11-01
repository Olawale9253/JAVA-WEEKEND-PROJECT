package com.semicolon.textlab.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PalindromeCheckerTest {

    private final PalindromeChecker checker = new PalindromeChecker();

    @Test
    void isPalindrome_simpleWord_returnsTrue() {
        assertTrue(checker.isPalindrome("racecar"));
    }

    @Test
    void isPalindrome_aManAPlanACanalPanama_returnsTrue() {
        assertTrue(checker.isPalindrome("A man, a plan, a canal: Panama!"));
    }

    @Test
    void isPalindrome_mixedCase_returnsTrue() {
        assertTrue(checker.isPalindrome("RaceCar"));
    }

    @Test
    void isPalindrome_notPalindrome_returnsFalse() {
        assertFalse(checker.isPalindrome("Hello"));
    }

    @Test
    void isPalindrome_punctuationOnlyText_returnsTrue() {
        assertTrue(checker.isPalindrome("!!!"));
    }

    @Test
    void isPalindrome_nullText_returnsTrue() {
        assertTrue(checker.isPalindrome(null));
    }
}