package com.semicolon.safebank.console;

import com.semicolon.safebank.exception.BankingException;
import com.semicolon.safebank.model.BankAccount;
import com.semicolon.safebank.model.Transaction;
import com.semicolon.safebank.service.AccountService;

import java.util.HashMap;
import java.util.Map;

public class SafeBankMenu {

    private static final int MENU_OPTION_BALANCE = 1;
    private static final int MENU_OPTION_DEPOSIT = 2;
    private static final int MENU_OPTION_WITHDRAW = 3;
    private static final int MENU_OPTION_HISTORY = 4;
    private static final int MENU_OPTION_CREATE_OTHER_ACCOUNT = 5;
    private static final int MENU_OPTION_TRANSFER = 6;
    private static final int MENU_OPTION_EXIT = 7;

    private final AccountService accountService;
    private final InputReader inputReader;
    private final Map<String, BankAccount> otherAccounts = new HashMap<>();
    private BankAccount currentAccount;

    public SafeBankMenu(AccountService accountService, InputReader inputReader) {
        this.accountService = accountService;
        this.inputReader = inputReader;
    }

    public void start() {
        System.out.println("=== Welcome to SafeBank ===");
        openCurrentAccount();
        runMenuLoop();
    }

    private void openCurrentAccount() {
        while (currentAccount == null) {
            try {
                String accountHolderName = inputReader.readLine(
                        "Enter account holder name: ");
                double openingBalance = inputReader.readAmount(
                        "Enter opening balance: ");
                currentAccount = accountService.createAccount(
                        accountHolderName, openingBalance);
                System.out.println("Account created for "
                        + currentAccount.getAccountHolderName()
                        + " with balance "
                        + formatAmount(currentAccount.getBalance()));
            } catch (BankingException bankingException) {
                System.out.println("Account creation failed: "
                        + bankingException.getMessage());
            }
        }
    }

    private void runMenuLoop() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = inputReader.readMenuChoice(
                        "Enter your choice: ", 1, MENU_OPTION_EXIT);
                running = handleChoice(choice);
            } catch (BankingException bankingException) {
                System.out.println("Operation failed: "
                        + bankingException.getMessage());
            } catch (RuntimeException unexpectedException) {
                System.out.println(
                        "Something went wrong, but SafeBank is still running: "
                                + unexpectedException.getMessage());
            }
        }
        System.out.println("Thank you for using SafeBank. Goodbye.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- SafeBank Menu ---");
        System.out.println(MENU_OPTION_BALANCE + ". View balance");
        System.out.println(MENU_OPTION_DEPOSIT + ". Deposit money");
        System.out.println(MENU_OPTION_WITHDRAW + ". Withdraw money");
        System.out.println(MENU_OPTION_HISTORY + ". View transaction history");
        System.out.println(MENU_OPTION_CREATE_OTHER_ACCOUNT + ". Create another account");
        System.out.println(MENU_OPTION_TRANSFER + ". Transfer money to another account");
        System.out.println(MENU_OPTION_EXIT + ". Exit");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case MENU_OPTION_BALANCE:
                viewBalance();
                return true;
            case MENU_OPTION_DEPOSIT:
                depositMoney();
                return true;
            case MENU_OPTION_WITHDRAW:
                withdrawMoney();
                return true;
            case MENU_OPTION_HISTORY:
                viewTransactionHistory();
                return true;
            case MENU_OPTION_CREATE_OTHER_ACCOUNT:
                createOtherAccount();
                return true;
            case MENU_OPTION_TRANSFER:
                transferMoney();
                return true;
            case MENU_OPTION_EXIT:
                return false;
            default:
                System.out.println("Unknown choice.");
                return true;
        }
    }

    private void viewBalance() {
        System.out.println("Current balance: "
                + formatAmount(currentAccount.getBalance()));
    }

    private void depositMoney() {
        double amount = inputReader.readAmount("Enter deposit amount: ");
        currentAccount.depositMoney(amount);
        System.out.println("Deposit successful. New balance: "
                + formatAmount(currentAccount.getBalance()));
    }

    private void withdrawMoney() {
        double amount = inputReader.readAmount("Enter withdrawal amount: ");
        currentAccount.withdrawMoney(amount);
        System.out.println("Withdrawal successful. New balance: "
                + formatAmount(currentAccount.getBalance()));
    }

    private void viewTransactionHistory() {
        if (currentAccount.getTransactionHistory().isEmpty()) {
            System.out.println("No successful transactions yet.");
            return;
        }
        System.out.println("--- Transaction History for "
                + currentAccount.getAccountHolderName() + " ---");
        for (Transaction transaction : currentAccount.getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    private void createOtherAccount() {
        String accountHolderName = inputReader.readLine(
                "Enter new account holder name: ");
        if (otherAccounts.containsKey(accountHolderName)) {
            System.out.println("An account for '" + accountHolderName
                    + "' already exists in this session.");
            return;
        }
        BankAccount newAccount = accountService.createAccount(
                accountHolderName, 0);
        otherAccounts.put(newAccount.getAccountHolderName(), newAccount);
        System.out.println("Account created for "
                + newAccount.getAccountHolderName() + " with balance 0.00");
    }

    private void transferMoney() {
        if (otherAccounts.isEmpty()) {
            System.out.println(
                    "No other accounts exist. Create one first from the menu.");
            return;
        }
        String targetName = inputReader.readLine(
                "Enter target account holder name: ");
        BankAccount targetAccount = otherAccounts.get(targetName);
        if (targetAccount == null) {
            System.out.println("No account found for '" + targetName + "'.");
            return;
        }
        double amount = inputReader.readAmount("Enter transfer amount: ");
        accountService.transferMoney(currentAccount, targetAccount, amount);
        System.out.println("Transfer successful.");
        System.out.println("Your balance: "
                + formatAmount(currentAccount.getBalance()));
        System.out.println(targetAccount.getAccountHolderName()
                + "'s balance: " + formatAmount(targetAccount.getBalance()));
    }

    private static String formatAmount(double amount) {
        return String.format("%,.2f", amount);
    }
}