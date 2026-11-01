package com.semicolon.textlab.model;

public class Passage {

    private final String originalText;
    private String currentText;

    public Passage(String originalText) {
        this.originalText = originalText == null ? "" : originalText;
        this.currentText = this.originalText;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText == null ? "" : currentText;
    }

    public void resetToOriginal() {
        this.currentText = originalText;
    }
}