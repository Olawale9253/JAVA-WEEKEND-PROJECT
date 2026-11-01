package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.InvalidNoteNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoteNameValidatorTest {

    private final NoteNameValidator validator = new NoteNameValidator();

    @Test
    void validate_nullName_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize(null));
    }

    @Test
    void validate_blankName_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize("   "));
    }

    @Test
    void validate_whitespaceOnlyName_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize("\t\n"));
    }

    @Test
    void validate_forwardSlash_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize("notes/secret"));
    }

    @Test
    void validate_backslash_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize("notes\\secret"));
    }

    @Test
    void validate_dotDotAlone_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize(".."));
    }

    @Test
    void validate_dotAlone_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> validator.validateAndNormalize("."));
    }

    @Test
    void validate_plainName_returnsTrimmedName() {
        assertEquals("My First Note",
                validator.validateAndNormalize("My First Note"));
    }

    @Test
    void validate_surroundingWhitespace_trimmed() {
        assertEquals("Shopping",
                validator.validateAndNormalize("   Shopping   "));
    }

    @Test
    void validate_nameWithSpaces_allowed() {
        assertEquals("Chapter One Notes",
                validator.validateAndNormalize("Chapter One Notes"));
    }
}