package com.semicolon.studyvault.service;

import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoteServiceTest {

    @TempDir
    Path temporaryRoot;

    private Path notesDirectory;
    private Path backupsDirectory;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        notesDirectory = temporaryRoot.resolve("notes");
        backupsDirectory = temporaryRoot.resolve("backups");
        noteService = new NoteService(notesDirectory, backupsDirectory);
    }

    @Test
    void createNote_newNote_persistsToFile() {
        noteService.createNote(new Note("greeting", "Hello"), false);
        assertTrue(Files.exists(notesDirectory.resolve("greeting.txt")));
    }

    @Test
    void exportNote_missingNote_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> noteService.exportNote("does-not-exist"));
    }

    @Test
    void exportNote_existingNote_copiesToBackups() {
        noteService.createNote(new Note("greeting", "Hello"), false);
        noteService.exportNote("greeting");
        assertTrue(Files.exists(backupsDirectory.resolve("greeting.txt")));
    }

    @Test
    void restoreNote_afterDelete_restoresContent() {
        noteService.createNote(new Note("greeting", "Hello"), false);
        noteService.exportNote("greeting");
        noteService.deleteNote("greeting");

        noteService.restoreNote("greeting", false);

        assertEquals("Hello", noteService.readNote("greeting").getContent());
    }
}