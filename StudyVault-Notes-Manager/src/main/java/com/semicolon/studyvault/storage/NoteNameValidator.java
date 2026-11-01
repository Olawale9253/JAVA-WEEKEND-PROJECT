package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.InvalidNoteNameException;

public class NoteNameValidator {

    public String validateAndNormalize(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidNoteNameException("Note title must not be blank.");
        }
        String trimmed = title.trim();
        if (trimmed.contains("/") || trimmed.contains("\\")) {
            throw new InvalidNoteNameException(
                    "Note title must not contain path separators.");
        }
        if (trimmed.equals(".") || trimmed.equals("..")) {
            throw new InvalidNoteNameException(
                    "Note title must not be a directory reference.");
        }
        return trimmed;
    }
}