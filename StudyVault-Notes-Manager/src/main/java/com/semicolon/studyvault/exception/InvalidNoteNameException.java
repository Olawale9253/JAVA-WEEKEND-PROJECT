package com.semicolon.studyvault.exception;

public class InvalidNoteNameException extends StorageException {

    public InvalidNoteNameException(String message) {
        super(message);
    }
}