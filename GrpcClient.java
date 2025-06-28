package client;

import interaction.InteractionServiceGrpc;
import interaction.InteractionRequest;
import interaction.InteractionResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.protobuf.Timestamp;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext().build();
        InteractionServiceGrpc.InteractionServiceBlockingStub stub =
            InteractionServiceGrpc.newBlockingStub(channel);

        InteractionRequest req = InteractionRequest.newBuilder()
            .setUserId("user123")
            .setPayload("click_button")
            .setReceivedAt(Timestamp.newBuilder()
                .setSeconds(System.currentTimeMillis()/1000).build())
            .build();

        InteractionResponse resp = stub.annotateInteraction(req);
        resp.getAnnotationsList().forEach(a ->
            System.out.println(a.getKey() + ": " + a.getValue())
        );
        channel.shutdown();
    }
}
