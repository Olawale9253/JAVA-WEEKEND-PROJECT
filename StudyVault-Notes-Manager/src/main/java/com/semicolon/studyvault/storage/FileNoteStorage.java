package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.NoteAlreadyExistsException;
import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.exception.StorageException;
import com.semicolon.studyvault.model.Note;
import com.semicolon.studyvault.model.NoteSummary;
import com.semicolon.studyvault.model.SearchResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileNoteStorage {

    private static final String FILE_EXTENSION = ".txt";

    private final Path notesDirectory;
    private final NoteNameValidator noteNameValidator;

    public FileNoteStorage(Path notesDirectory) {
        this.notesDirectory = notesDirectory;
        this.noteNameValidator = new NoteNameValidator();
        ensureDirectoryExists(notesDirectory);
    }

    public void saveNote(Note note, boolean overwriteExisting) {
        String title = noteNameValidator.validateAndNormalize(note.getTitle());
        Path noteFile = resolveNoteFile(title);

        if (Files.exists(noteFile) && !overwriteExisting) {
            throw new NoteAlreadyExistsException(
                    "A note titled '" + title + "' already exists.");
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(noteFile.toFile()), StandardCharsets.UTF_8))) {
            writer.write(note.getContent());
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to save note '" + title + "'.", ioException);
        }
    }

    public Note readNote(String title) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        Path noteFile = resolveNoteFile(validatedTitle);

        if (!Files.exists(noteFile)) {
            throw new NoteNotFoundException(
                    "No note titled '" + validatedTitle + "' was found.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(noteFile.toFile()), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[4096];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
            return new Note(validatedTitle, content.toString());
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to read note '" + validatedTitle + "'.", ioException);
        }
    }

    public void appendToNote(String title, String additionalContent) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        Path noteFile = resolveNoteFile(validatedTitle);

        if (!Files.exists(noteFile)) {
            throw new NoteNotFoundException(
                    "No note titled '" + validatedTitle + "' was found.");
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(noteFile.toFile(), true), StandardCharsets.UTF_8))) {
            writer.write(additionalContent);
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to append to note '" + validatedTitle + "'.", ioException);
        }
    }

    public List<NoteSummary> listNotes() {
        List<NoteSummary> summaries = new ArrayList<>();
        for (Path noteFile : listNoteFiles()) {
            String title = extractTitle(noteFile);
            try {
                long sizeInBytes = Files.size(noteFile);
                long lineCount = countLines(noteFile);
                Instant lastModified = Files.getLastModifiedTime(noteFile).toInstant();
                summaries.add(new NoteSummary(title, sizeInBytes, lineCount, lastModified));
            } catch (IOException ioException) {
                throw new StorageException(
                        "Failed to inspect note '" + title + "'.", ioException);
            }
        }
        summaries.sort(Comparator.comparing(NoteSummary::getTitle));
        return summaries;
    }

    public void deleteNote(String title) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        Path noteFile = resolveNoteFile(validatedTitle);

        if (!Files.exists(noteFile)) {
            throw new NoteNotFoundException(
                    "No note titled '" + validatedTitle + "' was found.");
        }

        try {
            Files.delete(noteFile);
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to delete note '" + validatedTitle + "'.", ioException);
        }
    }

    public boolean exists(String title) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        return Files.exists(resolveNoteFile(validatedTitle));
    }

    public List<SearchResult> searchNotes(String keyword) {
        List<SearchResult> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }
        String lowerKeyword = keyword.toLowerCase();
        for (Path noteFile : listNoteFiles()) {
            String title = extractTitle(noteFile);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(noteFile.toFile()), StandardCharsets.UTF_8))) {
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains(lowerKeyword)) {
                        results.add(new SearchResult(title, lineNumber, line));
                    }
                    lineNumber++;
                }
            } catch (IOException ioException) {
                throw new StorageException(
                        "Failed to search note '" + title + "'.", ioException);
            }
        }
        results.sort(Comparator
                .comparing(SearchResult::getTitle)
                .thenComparingInt(SearchResult::getLineNumber));
        return results;
    }

    // ---- helpers ----

    private Path resolveNoteFile(String title) {
        return notesDirectory.resolve(title + FILE_EXTENSION);
    }

    private List<Path> listNoteFiles() {
        List<Path> noteFiles = new ArrayList<>();
        try (var stream = Files.newDirectoryStream(notesDirectory, "*" + FILE_EXTENSION)) {
            stream.forEach(noteFiles::add);
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to list notes in " + notesDirectory + ".", ioException);
        }
        return noteFiles;
    }

    private String extractTitle(Path noteFile) {
        String fileName = noteFile.getFileName().toString();
        return fileName.endsWith(FILE_EXTENSION)
                ? fileName.substring(0, fileName.length() - FILE_EXTENSION.length())
                : fileName;
    }

    private long countLines(Path noteFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(noteFile.toFile()), StandardCharsets.UTF_8))) {
            long lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
            }
            return lineCount;
        }
    }

    private static void ensureDirectoryExists(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException ioException) {
                throw new StorageException(
                        "Failed to create directory " + directory + ".", ioException);
            }
        }
    }
}