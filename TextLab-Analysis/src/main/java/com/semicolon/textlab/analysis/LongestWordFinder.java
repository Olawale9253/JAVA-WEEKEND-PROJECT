package com.semicolon.textlab.analysis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LongestWordFinder {

    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z0-9]+");

    public List<String> findLongestWords(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        Matcher matcher = WORD_PATTERN.matcher(text);
        List<String> allWords = new ArrayList<>();
        while (matcher.find()) {
            allWords.add(matcher.group());
        }
        if (allWords.isEmpty()) {
            return new ArrayList<>();
        }

        int maximumLength = 0;
        for (String word : allWords) {
            if (word.length() > maximumLength) {
                maximumLength = word.length();
            }
        }

        Map<String, String> uniqueByLowercase = new LinkedHashMap<>();
        for (String word : allWords) {
            if (word.length() == maximumLength) {
                uniqueByLowercase.putIfAbsent(word.toLowerCase(), word);
            }
        }
        return new ArrayList<>(uniqueByLowercase.values());
    }
}