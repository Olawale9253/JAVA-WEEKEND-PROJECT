package com.semicolon.safebank.model;

import com.semicolon.safebank.exception.InsufficientFundsException;
import com.semicolon.safebank.exception.InvalidAccountHolderNameException;
import com.semicolon.safebank.exception.InvalidAmountException;
import com.semicolon.safebank.exception.WithdrawalLimitExceededException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {

    public static final double MAXIMUM_WITHDRAWAL_AMOUNT = 100_000.00;

    private final String accountHolderName;
    private double balance;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountHolderName, double openingBalance) {
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new InvalidAccountHolderNameException(
                    "Account holder name must not be blank.");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException(
                    "Opening balance must not be negative.");
        }
        this.accountHolderName = accountHolderName.trim();
        this.balance = openingBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public void depositMoney(double amount) {
        validateDeposit(amount);
        applyCredit(amount, TransactionType.DEPOSIT);
    }

    public void withdrawMoney(double amount) {
        validateWithdrawal(amount);
        applyDebit(amount, TransactionType.WITHDRAWAL);
    }

    public void transferOut(double amount) {
        validateWithdrawal(amount);
        applyDebit(amount, TransactionType.TRANSFER_OUT);
    }

    public void transferIn(double amount) {
        validateDeposit(amount);
        applyCredit(amount, TransactionType.TRANSFER_IN);
    }


    public void validateDeposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero.");
        }
    }

    public void validateWithdrawal(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero.");
        }
        if (amount > MAXIMUM_WITHDRAWAL_AMOUNT) {
            throw new WithdrawalLimitExceededException(
                    "Withdrawal exceeds single-withdrawal limit of "
                            + String.format("%.2f", MAXIMUM_WITHDRAWAL_AMOUNT) + ".");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available balance: "
                            + String.format("%.2f", balance) + ".");
        }
    }

    private void applyDebit(double amount, TransactionType transactionType) {
        balance -= amount;
        transactionHistory.add(
                new Transaction(transactionType, amount, balance));
    }

    private void applyCredit(double amount, TransactionType transactionType) {
        balance += amount;
        transactionHistory.add(
                new Transaction(transactionType, amount, balance));
    }


    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }
}