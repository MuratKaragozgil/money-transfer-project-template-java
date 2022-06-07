package org.example.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import org.example.activity.AccountActivity;
import org.example.activity.BonusActivity;

import java.time.Duration;

import static org.example.constant.Constants.QUEUE_NAME_BONUS;

// @@@SNIPSTART money-transfer-project-template-java-workflow-implementation
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    // RetryOptions specify how to automatically handle retries when Activities fail.
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
        .setInitialInterval(Duration.ofSeconds(1))
        .setMaximumInterval(Duration.ofSeconds(100))
        .setBackoffCoefficient(2)
        .setMaximumAttempts(500)
        .build();
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
        // Timeout options specify when to automatically timeout Activities if the process is taking too long.
        .setStartToCloseTimeout(Duration.ofSeconds(5))
        // Optionally provide customized RetryOptions.
        // Temporal retries failures by default, this is simply an example.
        .setRetryOptions(retryoptions).build();

    private final ActivityOptions bonusActivityOptions = ActivityOptions.newBuilder()
        // Timeout options specify when to automatically timeout Activities if the process is taking too long.
        .setStartToCloseTimeout(Duration.ofSeconds(5))
        // Optionally provide customized RetryOptions.
        // Temporal retries failures by default, this is simply an example.
        .setTaskQueue(QUEUE_NAME_BONUS).setRetryOptions(retryoptions).build();

    private final AccountActivity account = Workflow.newActivityStub(AccountActivity.class, defaultActivityOptions);
    private final BonusActivity bonus = Workflow.newActivityStub(BonusActivity.class, bonusActivityOptions);

    // The transfer method is the entry point to the Workflow.
    // Activity method executions can be orchestrated here or from within other Activity methods.
    @Override
    public String transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {
        account.withdraw(fromAccountId, referenceId, amount);
        account.deposit(toAccountId, referenceId, amount);
        return bonus.checkBonus(toAccountId);
    }

}
