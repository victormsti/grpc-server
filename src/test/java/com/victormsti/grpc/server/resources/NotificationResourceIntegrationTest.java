package com.victormsti.grpc.server.resources;

import com.victormsti.NotificationRequest;
import com.victormsti.NotificationResponse;
import com.victormsti.NotificationServiceGrpc;
import com.victormsti.grpc.server.resources.base.IntegrationTestBaseClass;
import com.victormsti.grpc.server.util.TestStreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationResourceIntegrationTest extends IntegrationTestBaseClass {

    @GrpcClient("inProcess")
    private NotificationServiceGrpc.NotificationServiceStub stub;

    @Test
    public void itShouldReceivePeriodicNotifications() throws InterruptedException {
        // Create a test request
        NotificationRequest request = NotificationRequest.newBuilder()
                .setClientId("TestClient")
                .build();

        // Create a response observer to capture the server's notifications
        TestStreamObserver<NotificationResponse> responseObserver = new TestStreamObserver<>();

        // Call the gRPC method
        stub.sendNotifications(request, responseObserver);

        // Wait for a moment to receive notifications
        Thread.sleep(1000);

        // Verify the response
        assertThat(responseObserver.getResponse()).isNotNull();
        responseObserver.onCompleted();
    }
}
