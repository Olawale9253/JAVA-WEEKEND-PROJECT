package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.BackupConflictException;
import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.model.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BackupStorageTest {

    @TempDir
    Path temporaryRoot;

    private Path notesDirectory;
    private Path backupsDirectory;
    private FileNoteStorage fileNoteStorage;
    private BackupStorage backupStorage;

    @BeforeEach
    void setUp() {
        notesDirectory = temporaryRoot.resolve("notes");
        backupsDirectory = temporaryRoot.resolve("backups");
        fileNoteStorage = new FileNoteStorage(notesDirectory);
        backupStorage = new BackupStorage(notesDirectory, backupsDirectory);
    }

    @Test
    void constructor_missingDirectories_createsBoth() {
        assertTrue(Files.isDirectory(notesDirectory));
        assertTrue(Files.isDirectory(backupsDirectory));
    }

    @Test
    void exportNote_existingNote_copiesFileToBackupsDirectory() {
        fileNoteStorage.saveNote(new Note("greeting", "Hello"), false);
        backupStorage.exportNote("greeting");
        assertTrue(Files.exists(backupsDirectory.resolve("greeting.txt")));
    }

    @Test
    void exportNote_missingNote_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> backupStorage.exportNote("does-not-exist"));
    }

    @Test
    void exportNote_existingBackup_overwritesSilently() {
        fileNoteStorage.saveNote(new Note("greeting", "Version one"), false);
        backupStorage.exportNote("greeting");
        fileNoteStorage.saveNote(new Note("greeting", "Version two"), true);
        backupStorage.exportNote("greeting");
        assertTrue(Files.exists(backupsDirectory.resolve("greeting.txt")));
    }

    @Test
    void exportNote_preservesExactBytes() throws Exception {
        String content = "Kaffeepause ☕ Náměstí #5\n第二行";
        fileNoteStorage.saveNote(new Note("utf8-note", content), false);

        backupStorage.exportNote("utf8-note");

        byte[] sourceBytes = Files.readAllBytes(notesDirectory.resolve("utf8-note.txt"));
        byte[] backupBytes = Files.readAllBytes(backupsDirectory.resolve("utf8-note.txt"));
        assertArrayEquals(sourceBytes, backupBytes);
    }

    @Test
    void exportNote_largeFileLargerThanBuffer_copiesAllBytes() throws Exception {
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 10_000; i++) {
            largeContent.append("Line ").append(i).append('\n');
        }
        fileNoteStorage.saveNote(new Note("large", largeContent.toString()), false);

        backupStorage.exportNote("large");

        byte[] sourceBytes = Files.readAllBytes(notesDirectory.resolve("large.txt"));
        byte[] backupBytes = Files.readAllBytes(backupsDirectory.resolve("large.txt"));
        assertArrayEquals(sourceBytes, backupBytes);
        assertTrue(sourceBytes.length > BackupStorage.COPY_BUFFER_SIZE_BYTES,
                "Test file should be larger than the copy buffer");
    }

    @Test
    void restoreNote_targetMissing_copiesFromBackups() {
        fileNoteStorage.saveNote(new Note("greeting", "Hello"), false);
        backupStorage.exportNote("greeting");
        fileNoteStorage.deleteNote("greeting");

        backupStorage.restoreNote("greeting", false);

        assertEquals("Hello", fileNoteStorage.readNote("greeting").getContent());
    }

    @Test
    void restoreNote_targetExistsWithoutOverwrite_throwsBackupConflictException() {
        fileNoteStorage.saveNote(new Note("greeting", "Backup version"), false);
        backupStorage.exportNote("greeting");
        fileNoteStorage.saveNote(new Note("greeting", "Current version"), true);

        assertThrows(BackupConflictException.class,
                () -> backupStorage.restoreNote("greeting", false));
    }

    @Test
    void restoreNote_targetExistsWithOverwrite_replacesContent() {
        fileNoteStorage.saveNote(new Note("greeting", "Backup version"), false);
        backupStorage.exportNote("greeting");
        fileNoteStorage.saveNote(new Note("greeting", "Current version"), true);

        backupStorage.restoreNote("greeting", true);

        assertEquals("Backup version",
                fileNoteStorage.readNote("greeting").getContent());
    }

    @Test
    void restoreNote_missingBackup_throwsNoteNotFoundException() {
        assertThrows(NoteNotFoundException.class,
                () -> backupStorage.restoreNote("does-not-exist", false));
    }

    @Test
    void backupExists_afterExport_returnsTrue() {
        fileNoteStorage.saveNote(new Note("greeting", "Hello"), false);
        backupStorage.exportNote("greeting");
        assertTrue(backupStorage.backupExists("greeting"));
    }

    @Test
    void backupExists_noExport_returnsFalse() {
        assertFalse(backupStorage.backupExists("greeting"));
    }
}