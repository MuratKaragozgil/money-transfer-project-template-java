package org.example;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

// @@@SNIPSTART money-transfer-project-template-java-workflow-implementation
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    private static final String WITHDRAW = "Withdraw";
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
        .setTaskQueue("BONUS_CHECK_TASK_QUEUE").setRetryOptions(retryoptions).build();

    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<>() {{
        put(WITHDRAW, ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
    }};

    private final AccountActivity account = Workflow.newActivityStub(AccountActivity.class, defaultActivityOptions, perActivityMethodOptions);
    private final BonusActivity bonus = Workflow.newActivityStub(BonusActivity.class, bonusActivityOptions, perActivityMethodOptions);

    // The transfer method is the entry point to the Workflow.
    // Activity method executions can be orchestrated here or from within other Activity methods.
    @Override
    public void transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {
        account.withdraw(fromAccountId, referenceId, amount);
        account.deposit(toAccountId, referenceId, amount);
        bonus.checkBonus(toAccountId);
    }

}
// @@@SNIPEND
