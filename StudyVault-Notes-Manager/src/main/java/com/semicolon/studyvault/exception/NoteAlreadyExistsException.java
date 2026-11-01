package com.semicolon.studyvault.exception;

public class NoteAlreadyExistsException extends StorageException {

    public NoteAlreadyExistsException(String message) {
        super(message);
    }
}