package com.semicolon.textlab.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextStatisticsAnalyzer {

    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z0-9]+");

    private static final Pattern SENTENCE_BOUNDARY = Pattern.compile("[.!?]+");

    public TextStatistics analyze(String text) {
        String safeText = text == null ? "" : text;
        int characterCountWithWhitespace = safeText.length();
        int characterCountWithoutWhitespace = countCharactersWithoutWhitespace(safeText);
        int wordCount = countWords(safeText);
        int sentenceCount = countSentences(safeText);
        return new TextStatistics(characterCountWithWhitespace,
                characterCountWithoutWhitespace,
                wordCount,
                sentenceCount);
    }

    private int countCharactersWithoutWhitespace(String text) {
        int count = 0;
        for (int index = 0; index < text.length(); index++) {
            if (!Character.isWhitespace(text.charAt(index))) {
                count++;
            }
        }
        return count;
    }

    private int countWords(String text) {
        Matcher matcher = WORD_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private int countSentences(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }
        String[] fragments = SENTENCE_BOUNDARY.split(text);
        int count = 0;
        for (String fragment : fragments) {
            if (!fragment.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }
}