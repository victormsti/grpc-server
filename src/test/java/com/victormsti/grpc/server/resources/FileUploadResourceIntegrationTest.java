package com.victormsti.grpc.server.resources;

import com.google.protobuf.ByteString;
import com.victormsti.FileChunk;
import com.victormsti.FileUploadServiceGrpc;
import com.victormsti.UploadStatus;
import com.victormsti.grpc.server.resources.base.IntegrationTestBaseClass;
import com.victormsti.grpc.server.util.TestStreamObserver;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileUploadResourceIntegrationTest extends IntegrationTestBaseClass {

    @GrpcClient("inProcess")
    private FileUploadServiceGrpc.FileUploadServiceStub stub;

    @Test
    public void itShouldUploadFileSuccessfully() throws IOException, InterruptedException {
        byte[] fileContent = "Test file content".getBytes();
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileContent);

        TestStreamObserver<UploadStatus> responseObserver = new TestStreamObserver<>();

        // Call the gRPC method with file chunks
        StreamObserver<FileChunk> requestObserver = stub.uploadFile(responseObserver);

        int chunkSize = 5; // Adjust the chunk size as needed
        byte[] buffer = new byte[chunkSize];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            FileChunk fileChunk = FileChunk.newBuilder()
                    .setChunkData(ByteString.copyFrom(buffer, 0, bytesRead))
                    .build();
            requestObserver.onNext(fileChunk);
        }

        // Complete the gRPC call
        requestObserver.onCompleted();

        // Wait for a moment to receive the response
        Thread.sleep(1000);

        // Verify the response
        assertThat(responseObserver.getResponse()).isNotNull();
        assertThat(responseObserver.getResponse().getSuccess()).isTrue();
        assertThat(responseObserver.getResponse().getMessage()).isEqualTo("File upload successful");
    }


}
