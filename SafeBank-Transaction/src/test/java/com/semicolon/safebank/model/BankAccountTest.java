package com.semicolon.safebank.model;

import com.semicolon.safebank.exception.InsufficientFundsException;
import com.semicolon.safebank.exception.InvalidAccountHolderNameException;
import com.semicolon.safebank.exception.InvalidAmountException;
import com.semicolon.safebank.exception.WithdrawalLimitExceededException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountTest {

    @Test
    void constructor_blankName_throwsInvalidAccountHolderNameException() {
        assertThrows(InvalidAccountHolderNameException.class,
                () -> new BankAccount("   ", 100));
    }

    @Test
    void constructor_nullName_throwsInvalidAccountHolderNameException() {
        assertThrows(InvalidAccountHolderNameException.class,
                () -> new BankAccount(null, 100));
    }

    @Test
    void constructor_negativeOpeningBalance_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class,
                () -> new BankAccount("Taofeek Olawale", -1));
    }

    @Test
    void constructor_validInput_setsNameAndBalance() {
        BankAccount account = new BankAccount("Taofeek Olawale", 500);
        assertEquals("Taofeek Olawale", account.getAccountHolderName());
        assertEquals(500.00, account.getBalance(), 0.001);
        assertEquals(0, account.getTransactionHistory().size());
    }


    @Test
    void deposit_negativeAmount_throwsInvalidAmountException() {
        BankAccount account = new BankAccount("Thabo Nkosi", 1000);
        assertThrows(InvalidAmountException.class,
                () -> account.depositMoney(-500));
        assertEquals(1000.00, account.getBalance(), 0.001,
                "Balance must be unchanged after a rejected deposit");
        assertEquals(0, account.getTransactionHistory().size());
    }

    @Test
    void deposit_zeroAmount_throwsInvalidAmountException() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        assertThrows(InvalidAmountException.class,
                () -> account.depositMoney(0));
    }

    @Test
    void deposit_validAmount_increasesBalanceAndRecordsTransaction() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        account.depositMoney(250.50);
        assertEquals(1250.50, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactionHistory().size());
        assertEquals(TransactionType.DEPOSIT,
                account.getTransactionHistory().get(0).getTransactionType());
    }

    // ---- withdrawals ----

    @Test
    void withdraw_negativeAmount_throwsInvalidAmountException() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        assertThrows(InvalidAmountException.class,
                () -> account.withdrawMoney(-1));
    }

    @Test
    void withdraw_amountAboveBalance_throwsInsufficientFundsException() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        assertThrows(InsufficientFundsException.class,
                () -> account.withdrawMoney(5000));
        assertEquals(1000.00, account.getBalance(), 0.001,
                "Balance must be unchanged after a rejected withdrawal");
        assertEquals(0, account.getTransactionHistory().size());
    }

    @Test
    void withdraw_amountAboveLimit_throwsWithdrawalLimitExceededException() {
        BankAccount account = new BankAccount("Bola Olawale", 200_000);
        assertThrows(WithdrawalLimitExceededException.class,
                () -> account.withdrawMoney(120_000));
        assertEquals(200_000.00, account.getBalance(), 0.001,
                "Balance must be unchanged after a rejected withdrawal");
        assertEquals(0, account.getTransactionHistory().size());
    }

    @Test
    void withdraw_rejectedTransaction_keepsBalanceAndHistoryUnchanged() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        account.depositMoney(200);
        int historySizeBeforeRejection = account.getTransactionHistory().size();
        double balanceBeforeRejection = account.getBalance();

        assertThrows(InsufficientFundsException.class,
                () -> account.withdrawMoney(5000));

        assertEquals(balanceBeforeRejection, account.getBalance(), 0.001);
        assertEquals(historySizeBeforeRejection,
                account.getTransactionHistory().size());
    }

    @Test
    void deposit_afterRejectedWithdrawal_succeedsImmediately() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        assertThrows(InsufficientFundsException.class,
                () -> account.withdrawMoney(5000));

        account.depositMoney(100);

        assertEquals(1100.00, account.getBalance(), 0.001);
        assertEquals(1, account.getTransactionHistory().size());
    }

    @Test
    void withdraw_validAmount_decreasesBalanceAndRecordsTransaction() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        account.withdrawMoney(250);
        assertEquals(750.00, account.getBalance(), 0.001);
        assertEquals(TransactionType.WITHDRAWAL,
                account.getTransactionHistory().get(0).getTransactionType());
    }

    @Test
    void getTransactionHistory_returnedListIsUnmodifiable() {
        BankAccount account = new BankAccount("Taofeek Olawale", 1000);
        account.depositMoney(100);
        assertThrows(UnsupportedOperationException.class,
                () -> account.getTransactionHistory().clear());
    }
}