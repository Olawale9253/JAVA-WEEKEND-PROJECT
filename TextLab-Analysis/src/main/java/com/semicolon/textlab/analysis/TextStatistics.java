package com.semicolon.textlab.analysis;

public class TextStatistics {

    private final int characterCountWithWhitespace;
    private final int characterCountWithoutWhitespace;
    private final int wordCount;
    private final int sentenceCount;

    public TextStatistics(int characterCountWithWhitespace,
                          int characterCountWithoutWhitespace,
                          int wordCount,
                          int sentenceCount) {
        this.characterCountWithWhitespace = characterCountWithWhitespace;
        this.characterCountWithoutWhitespace = characterCountWithoutWhitespace;
        this.wordCount = wordCount;
        this.sentenceCount = sentenceCount;
    }

    public int getCharacterCountWithWhitespace() {
        return characterCountWithWhitespace;
    }

    public int getCharacterCountWithoutWhitespace() {
        return characterCountWithoutWhitespace;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getSentenceCount() {
        return sentenceCount;
    }
}