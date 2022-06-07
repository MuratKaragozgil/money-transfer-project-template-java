package moneytransferapp.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.Workflow;

import java.time.Duration;

import static org.example.constant.Constants.QUEUE_NAME;
import static org.example.constant.Constants.QUEUE_NAME_BONUS;

public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    @Override
    public String transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {

        ActivityStub accountActivityStub = Workflow.newUntypedActivityStub( //
            ActivityOptions //
                .newBuilder() //
                .setTaskQueue(QUEUE_NAME) //
                .setStartToCloseTimeout(Duration.ofSeconds(10)) //
                .build() //
        );

        accountActivityStub.execute("withdraw", Void.class, fromAccountId, referenceId, amount);
        accountActivityStub.execute("deposit", Void.class, toAccountId, referenceId, amount);

        ActivityStub activity = Workflow.newUntypedActivityStub( //
            ActivityOptions //
                .newBuilder() //
                .setTaskQueue(QUEUE_NAME_BONUS) //
                .setRetryOptions(RetryOptions.newBuilder() //
                    .setDoNotRetry(NullPointerException.class.getName()) //
                    .setMaximumAttempts(5) //
                    .setMaximumInterval(Duration.ofSeconds(100)) //
                    .build()) //
                .setStartToCloseTimeout(Duration.ofSeconds(10)) //
                .build() //
        );

        // Execute the dynamic Activity. Note that the provided Activity name is not
        // explicitly registered with the Worker
        return activity.execute("checkBonus", String.class, toAccountId);
    }

}
