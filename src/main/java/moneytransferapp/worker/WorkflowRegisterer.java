package moneytransferapp.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import moneytransferapp.workflow.MoneyTransferWorkflowImpl;

import static org.example.constant.Constants.QUEUE_NAME_BANK;

public class WorkflowRegisterer {

    public static void main(String[] args) {
        // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);

        /**
         * Register workflow from initiator.
         */
        WorkerFactory factory = WorkerFactory.newInstance(client);
        factory //
            .newWorker(QUEUE_NAME_BANK) //
            .registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl.class); //
        factory.start();
    }

}
