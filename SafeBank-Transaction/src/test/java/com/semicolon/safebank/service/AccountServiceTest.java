package com.semicolon.safebank.service;

import com.semicolon.safebank.exception.InsufficientFundsException;
import com.semicolon.safebank.exception.InvalidAmountException;
import com.semicolon.safebank.exception.WithdrawalLimitExceededException;
import com.semicolon.safebank.model.BankAccount;
import com.semicolon.safebank.model.TransactionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    private final AccountService accountService = new AccountService();

    @Test
    void createAccount_validInput_returnsAccountWithGivenBalance() {
        BankAccount account = accountService.createAccount("Olu Kayode", 500);
        assertEquals("Olu Kayode", account.getAccountHolderName());
        assertEquals(500.00, account.getBalance(), 0.001);
    }

    @Test
    void transferMoney_validTransfer_debitsSourceAndCreditsTarget() {
        BankAccount sourceAccount = accountService.createAccount("Olu Kayode", 500);
        BankAccount targetAccount = accountService.createAccount("Massive Chukwu", 100);

        accountService.transferMoney(sourceAccount, targetAccount, 200);

        assertEquals(300.00, sourceAccount.getBalance(), 0.001);
        assertEquals(300.00, targetAccount.getBalance(), 0.001);
        assertEquals(TransactionType.TRANSFER_OUT,
                sourceAccount.getTransactionHistory().get(0).getTransactionType());
        assertEquals(TransactionType.TRANSFER_IN,
                targetAccount.getTransactionHistory().get(0).getTransactionType());
    }

    @Test
    void transferMoney_negativeAmount_throwsInvalidAmountExceptionAndLeavesBothUntouched() {
        BankAccount sourceAccount = accountService.createAccount("Olu Kayode", 500);
        BankAccount targetAccount = accountService.createAccount("Massive Chukwu", 100);

        assertThrows(InvalidAmountException.class,
                () -> accountService.transferMoney(sourceAccount, targetAccount, -50));

        assertEquals(500.00, sourceAccount.getBalance(), 0.001);
        assertEquals(100.00, targetAccount.getBalance(), 0.001);
        assertEquals(0, sourceAccount.getTransactionHistory().size());
        assertEquals(0, targetAccount.getTransactionHistory().size());
    }

    @Test
    void transferMoney_amountAboveSourceBalance_throwsInsufficientFundsAndLeavesBothUntouched() {
        BankAccount sourceAccount = accountService.createAccount("Olu Kayode", 300);
        BankAccount targetAccount = accountService.createAccount("Massive Chukwu", 100);

        assertThrows(InsufficientFundsException.class,
                () -> accountService.transferMoney(sourceAccount, targetAccount, 500));

        assertEquals(300.00, sourceAccount.getBalance(), 0.001);
        assertEquals(100.00, targetAccount.getBalance(), 0.001);
    }

    @Test
    void transferMoney_amountAboveLimit_throwsWithdrawalLimitExceededAndLeavesBothUntouched() {
        BankAccount sourceAccount = accountService.createAccount("Olu Kayode", 500_000);
        BankAccount targetAccount = accountService.createAccount("Massive Chukwu", 100);

        assertThrows(WithdrawalLimitExceededException.class,
                () -> accountService.transferMoney(
                        sourceAccount, targetAccount, 150_000));

        assertEquals(500_000.00, sourceAccount.getBalance(), 0.001);
        assertEquals(100.00, targetAccount.getBalance(), 0.001);
    }

    @Test
    void transferMoney_sameAccount_throwsIllegalArgumentException() {
        BankAccount sourceAccount = accountService.createAccount("Olu Kayode", 500);
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transferMoney(
                        sourceAccount, sourceAccount, 100));
    }
}