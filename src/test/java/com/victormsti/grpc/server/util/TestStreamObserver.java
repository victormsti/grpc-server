package com.victormsti.grpc.server.util;

import io.grpc.stub.StreamObserver;

public class TestStreamObserver<T> implements StreamObserver<T> {
    private T response;

    @Override
    public void onNext(T value) {
        this.response = value;
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onCompleted() {
    }

    public T getResponse() {
        return response;
    }
}
