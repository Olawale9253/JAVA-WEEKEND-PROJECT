package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.InvalidNoteNameException;
import com.semicolon.studyvault.exception.NoteAlreadyExistsException;
import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.model.Note;
import com.semicolon.studyvault.model.NoteSummary;
import com.semicolon.studyvault.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNoteStorageTest {

    @TempDir
    Path temporaryRoot;

    private Path notesDirectory;
    private FileNoteStorage storage;

    @BeforeEach
    void setUp() {
        notesDirectory = temporaryRoot.resolve("notes");
        storage = new FileNoteStorage(notesDirectory);
    }

    // ---- directory creation ----

    @Test
    void constructor_missingNotesDirectory_createsIt() {
        assertTrue(Files.isDirectory(notesDirectory));
    }

    // ---- save ----

    @Test
    void saveNote_newNote_createsFileWithTitleAsFilename() {
        storage.saveNote(new Note("greeting", "Hello"), false);
        assertTrue(Files.exists(notesDirectory.resolve("greeting.txt")));
    }

    @Test
    void saveNote_existingNoteWithoutOverwrite_throwsNoteAlreadyExistsException() {
        storage.saveNote(new Note("greeting", "Hello"), false);
        assertThrows(NoteAlreadyExistsException.class,
                () -> storage.saveNote(new Note("greeting", "New content"), false));
    }

    @Test
    void saveNote_existingNoteWithOverwrite_replacesContent() {
        storage.saveNote(new Note("greeting", "Old"), false);
        storage.saveNote(new Note("greeting", "New"), true);
        assertEquals("New", storage.readNote("greeting").getContent());
    }

    @Test
    void saveNote_invalidName_throwsInvalidNoteNameException() {
        assertThrows(InvalidNoteNameException.class,
                () -> storage.saveNote(new Note("notes/secret", "x"), false));
    }

    // ---- read ----

    @Test
    void readNote_missingNote_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> storage.readNote("does-not-exist"));
    }

    @Test
    void readNote_existingNote_returnsContent() {
        storage.saveNote(new Note("greeting", "Hello, world"), false);
        assertEquals("Hello, world", storage.readNote("greeting").getContent());
    }

    @Test
    void readNote_nonAsciiUtf8Content_preserved() {
        String content = "Kaffeepause ☕ Náměstí #5";
        storage.saveNote(new Note("utf8-note", content), false);
        assertEquals(content, storage.readNote("utf8-note").getContent());
    }

    // ---- append ----

    @Test
    void appendToNote_existingNote_preservesOldContentAndAddsNew() {
        storage.saveNote(new Note("greeting", "First line"), false);
        storage.appendToNote("greeting", "\nSecond line");
        assertEquals("First line\nSecond line",
                storage.readNote("greeting").getContent());
    }

    @Test
    void appendToNote_missingNote_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> storage.appendToNote("does-not-exist", "extra"));
    }

    // ---- list ----

    @Test
    void listNotes_emptyDirectory_returnsEmptyList() {
        assertTrue(storage.listNotes().isEmpty());
    }

    @Test
    void listNotes_multipleNotes_returnsSortedSummariesWithMetadata() {
        storage.saveNote(new Note("zebra", "line one\nline two"), false);
        storage.saveNote(new Note("apple", "single line"), false);

        List<NoteSummary> summaries = storage.listNotes();

        assertEquals(2, summaries.size());
        assertEquals("apple", summaries.get(0).getTitle());
        assertEquals("zebra", summaries.get(1).getTitle());
        assertEquals(2, summaries.get(1).getLineCount());
        assertTrue(summaries.get(1).getSizeInBytes() > 0);
    }

    // ---- delete ----

    @Test
    void deleteNote_existingNote_removesFile() {
        storage.saveNote(new Note("temp", "x"), false);
        storage.deleteNote("temp");
        assertFalse(storage.exists("temp"));
    }

    @Test
    void deleteNote_missingNote_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> storage.deleteNote("does-not-exist"));
    }

    // ---- search ----

    @Test
    void searchNotes_keywordPresent_returnsMatchingFilenamesAndLineNumbers() {
        storage.saveNote(new Note("first", "alpha\nbeta\ngamma"), false);
        storage.saveNote(new Note("second", "alpha again"), false);

        List<SearchResult> results = storage.searchNotes("alpha");

        assertEquals(2, results.size());
        assertEquals("first", results.get(0).getTitle());
        assertEquals(1, results.get(0).getLineNumber());
        assertEquals("second", results.get(1).getTitle());
        assertEquals(1, results.get(1).getLineNumber());
    }

    @Test
    void searchNotes_keywordAbsent_returnsEmptyList() {
        storage.saveNote(new Note("first", "alpha beta"), false);
        assertTrue(storage.searchNotes("nowhere").isEmpty());
    }

    @Test
    void searchNotes_emptyKeyword_returnsEmptyList() {
        storage.saveNote(new Note("first", "alpha beta"), false);
        assertTrue(storage.searchNotes("").isEmpty());
    }

    // ---- exists ----

    @Test
    void exists_presentNote_returnsTrue() {
        storage.saveNote(new Note("greeting", "hello"), false);
        assertTrue(storage.exists("greeting"));
    }

    @Test
    void exists_absentNote_returnsFalse() {
        assertFalse(storage.exists("does-not-exist"));
    }
}