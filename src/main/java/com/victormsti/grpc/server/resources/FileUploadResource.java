package com.victormsti.grpc.server.resources;

import com.victormsti.FileChunk;
import com.victormsti.FileUploadServiceGrpc;
import com.victormsti.UploadStatus;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.FileOutputStream;
import java.io.IOException;

@GrpcService
public class FileUploadResource extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    @Override
    public StreamObserver<FileChunk> uploadFile(StreamObserver<UploadStatus> responseObserver) {
        return new StreamObserver<>() {
            private FileOutputStream outputStream;

            @Override
            public void onNext(FileChunk fileChunk) {
                try {
                    if (outputStream == null) {
                        outputStream = new FileOutputStream("uploaded_file.txt");
                    }

                    outputStream.write(fileChunk.getChunkData().toByteArray());
                } catch (IOException e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }

                    responseObserver.onNext(UploadStatus
                            .newBuilder()
                            .setSuccess(true)
                            .setMessage("File upload successful")
                            .build()
                    );
                    responseObserver.onCompleted();
                } catch (IOException e) {
                    onError(e);
                }
            }
        };
    }
}
