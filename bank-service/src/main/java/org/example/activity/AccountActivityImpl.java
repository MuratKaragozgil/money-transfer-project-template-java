package org.example.activity;

public class AccountActivityImpl implements AccountActivity {

    @Override
    public void withdraw(String accountId, String referenceId, double amount) {
        System.out.printf("\nWithdrawing $%f from account %s. ReferenceId: %s\n", amount, accountId, referenceId);
//        throw new RuntimeException("simulated");
    }

    @Override
    public void rollBackWithdraw(String accountId, String referenceId, double amount) {
        System.out.printf("\nRollback withdraw $%f into account %s. ReferenceId: %s\n", amount, accountId, referenceId);
    }

    @Override
    public void deposit(String accountId, String referenceId, double amount) {
        System.out.printf("\nDepositing $%f into account %s. ReferenceId: %s\n", amount, accountId, referenceId);
        // Uncomment the following line to simulate an Activity error.
//        throw new RuntimeException("simulated");
    }

    @Override
    public void rollbackDeposit(String accountId, String referenceId, double amount) {
        System.out.printf("\nRollbackDeposit $%f into account %s. ReferenceId: %s\n", amount, accountId, referenceId);
    }

}
