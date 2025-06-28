// src/main/java/client/GrpcClient.java
package client;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import interaction.*;
import com.google.protobuf.Timestamp;
import io.grpc.Server;


public class GrpcClient {
    public static void main(String[] args)  throws Exception {

        ManagedChannel channel = OkHttpChannelBuilder
            .forAddress("127.0.0.1", 50051)
            .usePlaintext()
            .build();

        InteractionServiceGrpc.InteractionServiceBlockingStub stub =
            InteractionServiceGrpc.newBlockingStub(channel);

        InteractionRequest req = InteractionRequest.newBuilder()
            .setUserId("user123")
            .setPayload("click_button")
            .setReceivedAt(Timestamp.newBuilder()
                .setSeconds(System.currentTimeMillis() / 1000)
                .build())
            .build();

        InteractionResponse resp = stub.annotateInteraction(req);
        for (Annotation a : resp.getAnnotationsList()) {
            System.out.println(a.getKey() + ": " + a.getValue());
        }

        channel.shutdown();
    }
}
