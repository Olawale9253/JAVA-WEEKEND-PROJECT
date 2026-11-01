package com.semicolon.studyvault.model;

public class SearchResult {

    private final String title;
    private final int lineNumber;
    private final String lineText;

    public SearchResult(String title, int lineNumber, String lineText) {
        this.title = title;
        this.lineNumber = lineNumber;
        this.lineText = lineText;
    }

    public String getTitle() {
        return title;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineText() {
        return lineText;
    }
}