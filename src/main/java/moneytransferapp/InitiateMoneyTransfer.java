package moneytransferapp;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.example.workflow.MoneyTransferWorkflow;

import java.util.UUID;

import static org.example.constant.Constants.QUEUE_NAME;

// @@@SNIPSTART money-transfer-project-template-java-workflow-initiator
public class InitiateMoneyTransfer {

    public static void main(String[] args) {

        // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(QUEUE_NAME)
            // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate
            .build();

        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);

        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        String referenceId = UUID.randomUUID().toString();
        String fromAccount = "001-001";
        String toAccount = "002-002";
        double amount = 18.74;

        MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
        String result = workflow.transfer(fromAccount, toAccount, referenceId, amount);
        System.out.println("Result is here : " + result);

//        WorkflowExecution we = WorkflowClient.start(workflow::transfer, 1 + "Account", 2 + "Account", referenceId, amount);

//        int limit = 100;
//        for (int i = 0; i < limit; i++) {
//            // Asynchronous execution. This process will exit after making this call.
//            MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
//            WorkflowExecution we = WorkflowClient.start(workflow::transfer, i + "Account", i + "Account", referenceId, amount);
//            System.out.println("Account + " + i);
//        }

        System.exit(0);
    }

}
// @@@SNIPEND
