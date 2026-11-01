package com.semicolon.textlab.analysis;


public class PalindromeChecker {

    public boolean isPalindrome(String text) {
        if (text == null) {
            return true;
        }
        StringBuilder normalized = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char currentCharacter = text.charAt(index);
            if (Character.isLetterOrDigit(currentCharacter)) {
                normalized.append(Character.toLowerCase(currentCharacter));
            }
        }
        int leftIndex = 0;
        int rightIndex = normalized.length() - 1;
        while (leftIndex < rightIndex) {
            if (normalized.charAt(leftIndex) != normalized.charAt(rightIndex)) {
                return false;
            }
            leftIndex++;
            rightIndex--;
        }
        return true;
    }
}