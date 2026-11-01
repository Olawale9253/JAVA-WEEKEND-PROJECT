package com.semicolon.studyvault.storage;

import com.semicolon.studyvault.exception.BackupConflictException;
import com.semicolon.studyvault.exception.NoteNotFoundException;
import com.semicolon.studyvault.exception.StorageException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackupStorage {

    /** Buffer size for byte-stream copying. Larger than any single note in practice. */
    public static final int COPY_BUFFER_SIZE_BYTES = 8192;

    private static final String FILE_EXTENSION = ".txt";

    private final Path notesDirectory;
    private final Path backupsDirectory;
    private final NoteNameValidator noteNameValidator;

    public BackupStorage(Path notesDirectory, Path backupsDirectory) {
        this.notesDirectory = notesDirectory;
        this.backupsDirectory = backupsDirectory;
        this.noteNameValidator = new NoteNameValidator();
        ensureDirectoryExists(notesDirectory);
        ensureDirectoryExists(backupsDirectory);
    }

    /** Copies notes/title.txt to backups/title.txt. Always overwrites the backup. */
    public void exportNote(String title) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        Path sourceFile = notesDirectory.resolve(validatedTitle + FILE_EXTENSION);
        Path backupFile = backupsDirectory.resolve(validatedTitle + FILE_EXTENSION);

        if (!Files.exists(sourceFile)) {
            throw new NoteNotFoundException(
                    "No note titled '" + validatedTitle + "' to export.");
        }

        copyBytesWithBuffer(sourceFile, backupFile);
    }


    public void restoreNote(String title, boolean overwriteExisting) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        Path backupFile = backupsDirectory.resolve(validatedTitle + FILE_EXTENSION);
        Path targetFile = notesDirectory.resolve(validatedTitle + FILE_EXTENSION);

        if (!Files.exists(backupFile)) {
            throw new NoteNotFoundException(
                    "No backup for note titled '" + validatedTitle + "'.");
        }
        if (Files.exists(targetFile) && !overwriteExisting) {
            throw new BackupConflictException(
                    "A note titled '" + validatedTitle + "' already exists.");
        }

        copyBytesWithBuffer(backupFile, targetFile);
    }

    public boolean backupExists(String title) {
        String validatedTitle = noteNameValidator.validateAndNormalize(title);
        return Files.exists(backupsDirectory.resolve(validatedTitle + FILE_EXTENSION));
    }

    // ---- the byte-buffer copy, in one place so it can be inspected ----

    private static void copyBytesWithBuffer(Path sourceFile, Path targetFile) {
        try (FileInputStream inputStream = new FileInputStream(sourceFile.toFile());
             FileOutputStream outputStream = new FileOutputStream(targetFile.toFile())) {
            byte[] buffer = new byte[COPY_BUFFER_SIZE_BYTES];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException ioException) {
            throw new StorageException(
                    "Failed to copy " + sourceFile + " to " + targetFile + ".",
                    ioException);
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