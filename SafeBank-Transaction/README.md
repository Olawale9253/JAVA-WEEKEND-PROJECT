# SafeBank Transaction Simulator

## Purpose
Console banking simulator that protects account balances from invalid transactions
using custom exceptions and pre-validation.

## How to run
cd 01-safebank-transaction-simulator
mvn test
mvn exec:java

## Project structure
- model/BankAccount        : enforces every business rule, throws custom exceptions
- model/Transaction        : immutable record of one successful transaction
- service/AccountService   : account creation and the transfer stretch goal
- console/InputReader      : Scanner wrapper, handles non-numeric and out-of-range input
- console/SafeBankMenu     : menu loop; catches BankingException once per loop
- exception/*              : one custom exception per business rule

## Design decisions
- Business rules live in BankAccount, not in the menu.
- Validation always runs before any state change, so a rejected transaction
  leaves balance and successful history unchanged.
- Withdrawal order of checks: positive amount -> withdrawal limit -> available balance.
  The limit is a fixed rule independent of the account, so its message is predictable.
- BankingException is unchecked so the menu only needs one catch clause
  for all business errors; RuntimeExceptions from bugs are also caught so the
  app never terminates unexpectedly.
- Transaction history is returned as an unmodifiable view.

## Known limitations
- Uses double for money. Production code would use BigDecimal or long cents.
- Transfer history records TRANSFER_OUT and TRANSFER_IN, which is the correct
  type, but the transaction display does not show the counterparty account name.
- Accounts other than the current one live only in memory for the demo.

## Stretch goals completed
- Transfers between two accounts, with the entire transfer validated
  before either balance changes.