package com.victormsti.grpc.server.resources;

import com.victormsti.NotificationRequest;
import com.victormsti.NotificationResponse;
import com.victormsti.NotificationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
public class NotificationResource extends NotificationServiceGrpc.NotificationServiceImplBase {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final Map<String, StreamObserver<NotificationResponse>> clientStreams = new ConcurrentHashMap<>();

    @Override
    public void sendNotifications(NotificationRequest request, StreamObserver<NotificationResponse> responseObserver) {
        String clientId = request.getClientId();
        System.out.println("Client " + clientId + " connected for notifications.");

        // Save the client's stream observer for future notifications
        clientStreams.put(clientId, responseObserver);

        // Start a periodic task to send notifications
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            sendNotification(
                    clientId,
                    "This is a periodic notification with a random number: "
                    + Math.random()
            );
        }, 0, 5, TimeUnit.SECONDS); // Send a notification every 5 seconds
    }

    // Method to send a notification to a specific client
    public void sendNotification(String clientId, String message) {
        StreamObserver<NotificationResponse> clientStream = clientStreams.get(clientId);
        if (clientStream != null) {
            NotificationResponse notificationResponse = NotificationResponse.newBuilder()
                    .setNotification(message)
                    .build();
            clientStream.onNext(notificationResponse);
        }
    }
    // Shutdown hook to clean up resources
    @PreDestroy
    public void shutdown() {
        scheduledExecutorService.shutdownNow();
    }

}
