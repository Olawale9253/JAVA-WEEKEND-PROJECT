package com.semicolon.safebank.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionTest {

    @Test
    void toString_containsTypeAmountAndBalance() {
        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT, 250.00, 1250.00);
        String rendered = transaction.toString();
        assertTrue(rendered.contains("DEPOSIT"));
        assertTrue(rendered.contains("250.00"));
        assertTrue(rendered.contains("1250.00"));
    }
}