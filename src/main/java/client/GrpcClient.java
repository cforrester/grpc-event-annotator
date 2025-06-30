package client;

import java.io.File;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;

import interaction.InteractionServiceGrpc;
import interaction.InteractionRequest;
import interaction.InteractionResponse;
import interaction.Annotation;

import com.google.protobuf.Timestamp;

public class GrpcClient {
  public static void main(String[] args) throws Exception {
    // build SSL context for client
    SslContext sslContext = GrpcSslContexts.forClient()
        .keyManager(
          new File("certs/client.crt"),
          new File("certs/client.key"))
        .trustManager(new File("certs/ca.crt"))
        .build();

    ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
        .sslContext(sslContext)
        .build();

    var stub = InteractionServiceGrpc.newBlockingStub(channel);

    InteractionRequest req = InteractionRequest.newBuilder()
        .setUserId("user123")
        .setPayload("hello mTLS")
        .setReceivedAt(Timestamp.newBuilder()
            .setSeconds(System.currentTimeMillis()/1000)
            .build())
        .build();

    InteractionResponse resp = stub.annotateInteraction(req);
    resp.getAnnotationsList().forEach(a ->
      System.out.println(a.getKey()+": "+a.getValue()));

    channel.shutdown();
  }
}
