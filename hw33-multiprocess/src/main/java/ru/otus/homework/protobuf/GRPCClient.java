package ru.otus.homework.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.homework.protobuf.generated.ClientMessage;
import ru.otus.homework.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.homework.protobuf.generated.ServerMessage;

import java.util.concurrent.TimeUnit;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static int serverValue = 0;

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteDBServiceGrpc.newStub(channel);

        stub.sendMessage(ClientMessage.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
            @Override
            public void onNext(ServerMessage serverMessage) {
                synchronized (GRPCClient.class) {
                    serverValue = serverMessage.getValue();
                    System.out.println("serverValue:" + serverValue);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
            }
        });

        int currentValue = 0;
        int previousValueFromServer = 0;
        for (int i = 0; i < 50; i++) {
            synchronized (GRPCClient.class) {
                if (previousValueFromServer == serverValue) {
                    currentValue += 1;
                } else {
                    currentValue += serverValue + 1;
                    previousValueFromServer = serverValue;
                }
            }
            System.out.println("currentValue:" + currentValue);
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        channel.shutdown();
    }
}
