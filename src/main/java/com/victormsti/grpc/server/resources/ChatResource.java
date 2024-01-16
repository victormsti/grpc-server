package com.victormsti.grpc.server.resources;

import com.victormsti.ChatMessage;
import com.victormsti.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ChatResource extends ChatServiceGrpc.ChatServiceImplBase {

    @Override
    public StreamObserver<ChatMessage> startChat(StreamObserver<ChatMessage> responseObserver) {
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage message) {
                // Process incoming message
                System.out.println("Received message from " +
                        message.getSender() + ": " +
                        message.getContent()
                );
                // Respond back (for demonstration purposes)
                ChatMessage responseMessage = ChatMessage.newBuilder()
                        .setSender("Server")
                        .setContent("Hello, " + message.getSender() +
                                "! I received your message.")
                        .build();
                responseObserver.onNext(responseMessage);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error in chat stream: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("The chat is closed");
                responseObserver.onCompleted();
            }
        };
    }
}
