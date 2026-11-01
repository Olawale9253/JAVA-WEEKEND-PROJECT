package com.semicolon.studyvault.exception;

public class BackupConflictException extends StorageException {

    public BackupConflictException(String message) {
        super(message);
    }
}