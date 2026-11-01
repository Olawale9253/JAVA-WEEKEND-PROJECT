# SafeBank Acceptance Checklist

| Check (from challenge PDF)                              | Expected                                              | Actual                                                        | Pass |
|---------------------------------------------------------|-------------------------------------------------------|---------------------------------------------------------------|------|
| Deposit of -500                                         | InvalidAmountException, balance unchanged             | Test: deposit_negativeAmount_throwsInvalidAmountException     | [x]  |
| Withdrawal exceeding balance (e.g. 5000 from 1000)      | InsufficientFundsException, balance unchanged         | Test: withdraw_amountAboveBalance_throwsInsufficientFunds     | [x]  |
| Withdrawal of 120,000 from 200,000                      | WithdrawalLimitExceededException, balance unchanged   | Test: withdraw_amountAboveLimit_throwsWithdrawalLimitExceeded | [x]  |
| Non-numeric menu input                                  | Helpful message, menu re-prompts, app keeps running   | Test: readMenuChoice_nonNumericInput_rePromptsUntilValid      | [x]  |
| Success immediately after failure                       | Next valid operation succeeds normally                | Test: deposit_afterRejectedWithdrawal_succeedsImmediately     | [x]  |
| Rejected transaction leaves history unchanged           | History size unchanged after failure                  | Test: withdraw_rejectedTransaction_keepsBalanceAndHistory     | [x]  |
| Stretch: transfer validates entire transfer first       | Both accounts untouched on any rejection              | Tests in AccountServiceTest (four rejection scenarios)        | [x]  |