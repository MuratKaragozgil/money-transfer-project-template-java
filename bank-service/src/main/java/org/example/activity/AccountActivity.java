package org.example.activity;// @@@SNIPSTART money-transfer-project-template-java-activity-interface

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AccountActivity {

    @ActivityMethod(name = "deposit")
    void deposit(String accountId, String referenceId, double amount);

    @ActivityMethod(name = "rollbackDeposit")
    void rollbackDeposit(String accountId, String referenceId, double amount);

    @ActivityMethod(name = "withdraw")
    void withdraw(String accountId, String referenceId, double amount);

    @ActivityMethod(name = "rollbackWithdraw")
    void rollBackWithdraw(String accountId, String referenceId, double amount);

}
// @@@SNIPEND
