package moneytransferapp;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import moneytransferapp.workflow.MoneyTransferWorkflow;
import moneytransferapp.workflow.MoneyTransferWorkflowImpl;

import java.util.UUID;

import static org.example.constant.Constants.QUEUE_NAME;

public class InitiateMoneyTransfer {

    public static void main(String[] args) {

        // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);

        /**
         * Trigger workflow
         */
        WorkflowOptions options = WorkflowOptions //
            .newBuilder() //
            .setTaskQueue(QUEUE_NAME) //
            .build();

        String referenceId = UUID.randomUUID().toString();
        String fromAccount = "001-001";
        String toAccount = "002-002";
        double amount = 18.74;

        MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
        String result = workflow.transfer(fromAccount, toAccount, referenceId, amount);

        System.out.println("Result is here : " + result);

        System.exit(0);
    }

}
