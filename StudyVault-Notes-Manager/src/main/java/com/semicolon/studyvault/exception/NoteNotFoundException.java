package com.semicolon.studyvault.exception;

public class NoteNotFoundException extends StorageException {

    public NoteNotFoundException(String message) {
        super(message);
    }
}