package com.victormsti.grpc.server.resources;

import com.victormsti.ChatMessage;
import com.victormsti.ChatServiceGrpc;
import com.victormsti.grpc.server.resources.base.IntegrationTestBaseClass;
import com.victormsti.grpc.server.util.TestStreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatResourceIntegrationTest extends IntegrationTestBaseClass {

    @GrpcClient("inProcess")
    private ChatServiceGrpc.ChatServiceStub stub;

    @Test
    public void itShouldStartAChatWithTheServer() throws InterruptedException {
        // Create a test message
        ChatMessage testMessage = ChatMessage.newBuilder()
                .setSender("TestSender")
                .setContent("TestContent")
                .build();

        // Create a response observer to capture the server's response
        TestStreamObserver<ChatMessage> responseObserver = new TestStreamObserver<>();

        // Call the gRPC method
        stub.startChat(responseObserver).onNext(testMessage);

        // Wait for 1 second to get message back from server
        Thread.sleep(1000);

        // Verify the response
        assertThat(responseObserver.getResponse()).isNotNull();
        assertThat(responseObserver.getResponse().getSender()).isEqualTo("Server");
        assertThat(responseObserver.getResponse().getContent()).startsWith("Hello, TestSender!");

        responseObserver.onCompleted();
    }
}
