package com.semicolon.safebank.exception;

public class WithdrawalLimitExceededException extends BankingException {
    public WithdrawalLimitExceededException(String message) {
        super(message);
    }
}