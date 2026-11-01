package com.semicolon.textlab.analysis;

public class WhitespaceCleaner {

    public String clean(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(text.length());
        boolean previousWasWhitespace = true;

        for (int index = 0; index < text.length(); index++) {
            char currentCharacter = text.charAt(index);
            if (Character.isWhitespace(currentCharacter)) {
                previousWasWhitespace = true;
            } else {
                if (previousWasWhitespace && builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(currentCharacter);
                previousWasWhitespace = false;
            }
        }
        return builder.toString();
    }
}