package com.semicolon.textlab.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrequencyCounter {

    public int countWholeWord(String text, String word) {
        if (text == null || word == null || word.isEmpty()) {
            return 0;
        }
        String regex = "(?i)(?<![A-Za-z0-9])"
                + Pattern.quote(word)
                + "(?![A-Za-z0-9])";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}