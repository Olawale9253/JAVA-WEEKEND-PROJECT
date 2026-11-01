package com.semicolon.safebank.exception;

public class InvalidAccountHolderNameException extends BankingException {
    public InvalidAccountHolderNameException(String message) {
        super(message);
    }
}