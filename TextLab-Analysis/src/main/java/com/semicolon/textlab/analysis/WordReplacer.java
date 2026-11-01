package com.semicolon.textlab.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordReplacer {

    public String replaceWholeWord(String text,
                                   String targetWord,
                                   String replacement) {
        if (text == null || targetWord == null || targetWord.isEmpty()) {
            return text;
        }
        String regex = "(?i)(?<![A-Za-z0-9])"
                + Pattern.quote(targetWord)
                + "(?![A-Za-z0-9])";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.replaceAll(Matcher.quoteReplacement(replacement));
    }
}