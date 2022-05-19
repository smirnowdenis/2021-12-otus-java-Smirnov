package ru.otus.homework.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.homework.protobuf.generated.ClientMessage;
import ru.otus.homework.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.homework.protobuf.generated.ServerMessage;

import java.util.concurrent.TimeUnit;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    @Override
    public void sendMessage(ClientMessage request, StreamObserver<ServerMessage> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();
        for (int i = firstValue; i < lastValue; i++) {
            responseObserver.onNext(ServerMessage.newBuilder().setValue(i).build());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        responseObserver.onCompleted();
    }
}

