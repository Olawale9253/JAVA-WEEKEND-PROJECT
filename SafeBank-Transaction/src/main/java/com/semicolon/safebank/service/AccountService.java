package com.semicolon.safebank.service;

import com.semicolon.safebank.model.BankAccount;

public class AccountService {

    public BankAccount createAccount(String accountHolderName, double openingBalance) {
        return new BankAccount(accountHolderName, openingBalance);
    }

    public void transferMoney(BankAccount sourceAccount,
                              BankAccount targetAccount,
                              double amount) {
        if (sourceAccount == null || targetAccount == null) {
            throw new IllegalArgumentException(
                    "Source and target accounts are required.");
        }
        if (sourceAccount == targetAccount) {
            throw new IllegalArgumentException(
                    "Cannot transfer to the same account.");
        }

        // Validate the entire transfer before changing either balance.
        sourceAccount.validateWithdrawal(amount);
        targetAccount.validateDeposit(amount);

        // Both sides are valid, so now apply.
        sourceAccount.transferOut(amount);
        targetAccount.transferIn(amount);
    }
}