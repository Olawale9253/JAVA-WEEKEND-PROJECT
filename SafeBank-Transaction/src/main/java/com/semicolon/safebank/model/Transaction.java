package com.semicolon.safebank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TransactionType transactionType;
    private final double amount;
    private final double balanceAfterTransaction;
    private final LocalDateTime timestamp;

    public Transaction(TransactionType transactionType,
                       double amount,
                       double balanceAfterTransaction) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.timestamp = LocalDateTime.now();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "%-12s | Amount: %12.2f | Balance after: %12.2f | %s",
                transactionType,
                amount,
                balanceAfterTransaction,
                timestamp.format(DISPLAY_FORMAT));
    }
}