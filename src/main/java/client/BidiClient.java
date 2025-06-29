package client;

import interaction.InteractionRequest;
import interaction.InteractionResponse;
import interaction.Annotation;
import interaction.InteractionServiceGrpc;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BidiClient {
  public static void main(String[] args) throws InterruptedException {
    ManagedChannel channel = OkHttpChannelBuilder
        .forAddress("localhost", 50051)
        .usePlaintext()
        .build();

    InteractionServiceGrpc.InteractionServiceStub asyncStub =
        InteractionServiceGrpc.newStub(channel);

    CountDownLatch finishLatch = new CountDownLatch(1);

    StreamObserver<InteractionRequest> requestObserver =
        asyncStub.chatAnnotate(new StreamObserver<InteractionResponse>() {
          @Override
          public void onNext(InteractionResponse resp) {
            System.out.println("Received response:");
            for (Annotation a : resp.getAnnotationsList()) {
              System.out.printf("  %s = %s%n", a.getKey(), a.getValue());
            }
          }
          @Override public void onError(Throwable t) {
            t.printStackTrace();
            finishLatch.countDown();
          }
          @Override public void onCompleted() {
            System.out.println("Server completed");
            finishLatch.countDown();
          }
        });

    // Send a few requests in a loop
    for (int i = 1; i <= 5; i++) {
      InteractionRequest req = InteractionRequest.newBuilder()
          .setUserId("user" + i)
          .setPayload("payload_" + i)
          .setReceivedAt(Timestamp.newBuilder()
              .setSeconds(System.currentTimeMillis() / 1000)
              .build())
          .build();
      requestObserver.onNext(req);
      // optional delay
      Thread.sleep(200);
    }

    // Mark the end of requests
    requestObserver.onCompleted();

    // Wait for the server to complete
    finishLatch.await(1, TimeUnit.MINUTES);
    channel.shutdown();
  }
}
