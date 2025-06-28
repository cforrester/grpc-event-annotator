package client;

import interaction.InteractionRequest;
import interaction.InteractionResponse;
import interaction.InteractionServiceGrpc;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StreamClient {
  public static void main(String[] args) throws InterruptedException {
    ManagedChannel channel = OkHttpChannelBuilder
        .forAddress("127.0.0.1", 50051)
        .usePlaintext()
        .build();

    InteractionServiceGrpc.InteractionServiceStub stub =
        InteractionServiceGrpc.newStub(channel);

    CountDownLatch done = new CountDownLatch(1);
    StreamObserver<InteractionRequest> requestObserver =
        stub.streamAnnotateInteractions(new StreamObserver<InteractionResponse>() {
          @Override
          public void onNext(InteractionResponse resp) {
            System.out.println("Got " + resp.getAnnotationsCount() + " annotations");
            resp.getAnnotationsList().forEach(a ->
              System.out.println(a.getKey() + " -> " + a.getValue()));
          }
          @Override public void onError(Throwable t) {
            t.printStackTrace();
            done.countDown();
          }
          @Override public void onCompleted() {
            System.out.println("Stream completed");
            done.countDown();
          }
        });

    // send 3 requests
    for (int i = 1; i <= 3; i++) {
      InteractionRequest req = InteractionRequest.newBuilder()
          .setUserId("user" + i)
          .setPayload("event" + i)
          .setReceivedAt(Timestamp.newBuilder()
              .setSeconds(System.currentTimeMillis() / 1000)
              .build())
          .build();
      requestObserver.onNext(req);
    }
    // signal end-of-stream
    requestObserver.onCompleted();

    // wait for the response
    done.await(1, TimeUnit.MINUTES);
    channel.shutdown();
  }
}
