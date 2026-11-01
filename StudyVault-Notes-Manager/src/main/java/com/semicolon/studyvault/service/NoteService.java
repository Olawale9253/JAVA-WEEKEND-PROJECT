package com.semicolon.studyvault.service;

import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.model.Note;
import com.semicolon.studyvault.model.NoteSummary;
import com.semicolon.studyvault.model.SearchResult;
import com.semicolon.studyvault.storage.BackupStorage;
import com.semicolon.studyvault.storage.FileNoteStorage;

import java.nio.file.Path;
import java.util.List;

public class NoteService {

    private final FileNoteStorage fileNoteStorage;
    private final BackupStorage backupStorage;

    public NoteService(Path notesDirectory, Path backupsDirectory) {
        this(new FileNoteStorage(notesDirectory),
                new BackupStorage(notesDirectory, backupsDirectory));
    }

    public NoteService(FileNoteStorage fileNoteStorage, BackupStorage backupStorage) {
        this.fileNoteStorage = fileNoteStorage;
        this.backupStorage = backupStorage;
    }

    public void createNote(Note note, boolean overwriteExisting) {
        fileNoteStorage.saveNote(note, overwriteExisting);
    }

    public boolean noteExists(String title) {
        return fileNoteStorage.exists(title);
    }

    public Note readNote(String title) {
        return fileNoteStorage.readNote(title);
    }

    public void appendToNote(String title, String additionalContent) {
        fileNoteStorage.appendToNote(title, additionalContent);
    }

    public List<NoteSummary> listNotes() {
        return fileNoteStorage.listNotes();
    }

    public List<SearchResult> searchNotes(String keyword) {
        return fileNoteStorage.searchNotes(keyword);
    }

    public void deleteNote(String title) {
        fileNoteStorage.deleteNote(title);
    }

    public void exportNote(String title) {
        if (!fileNoteStorage.exists(title)) {
            throw new NoteNotFoundException(
                    "No note titled '" + title + "' to export.");
        }
        backupStorage.exportNote(title);
    }

    public boolean backupExists(String title) {
        return backupStorage.backupExists(title);
    }

    public void restoreNote(String title, boolean overwriteExisting) {
        backupStorage.restoreNote(title, overwriteExisting);
    }
}