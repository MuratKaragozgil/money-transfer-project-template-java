package moneytransferapp.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;

import java.time.Duration;

import static org.example.constant.Constants.QUEUE_NAME_BANK;
import static org.example.constant.Constants.QUEUE_NAME_BONUS;

public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

    final ActivityStub accountActivityStub = Workflow.newUntypedActivityStub( //
        ActivityOptions //
            .newBuilder() //
            .setTaskQueue(QUEUE_NAME_BANK) //
            .setRetryOptions( //
                RetryOptions.newBuilder() //
                    .setDoNotRetry(RuntimeException.class.getName()) //
                    .build()) //
            .setStartToCloseTimeout(Duration.ofSeconds(10)) //
            .build() //
    );

    final ActivityStub bonusActivityStub = Workflow.newUntypedActivityStub( //
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

    @Override
    public String transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {
        Saga.Options sagaOptions = new Saga.Options.Builder().setParallelCompensation(true).build();
        Saga saga = new Saga(sagaOptions);

        try {
            accountActivityStub.execute("withdraw", Void.class, fromAccountId, referenceId, amount);
            saga.addCompensation(() -> {
                accountActivityStub.execute("rollbackWithdraw", Void.class, fromAccountId, referenceId, amount);
            });

            accountActivityStub.execute("deposit", Void.class, toAccountId, referenceId, amount);
            saga.addCompensation(() -> {
                accountActivityStub.execute("rollbackDeposit", Void.class, fromAccountId, referenceId, amount);
            });

            saga.addCompensation(() -> {
                bonusActivityStub.execute("rollbackBonus", Void.class, toAccountId);
            });

            // Execute the dynamic Activity. Note that the provided Activity name is not
            // explicitly registered with the Worker
            return bonusActivityStub.execute("checkBonus", String.class, toAccountId);
        } catch (ActivityFailure activityFailure) {
            System.out.println("Exception failure detected");
            saga.compensate();
        }

        return null;
    }

}
