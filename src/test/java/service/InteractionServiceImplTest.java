package service;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import interaction.InteractionRequest;
import interaction.InteractionResponse;
import interaction.Annotation;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class InteractionServiceImplTest {
  private MongoClient client;
  // start a real MongoDB 5.x container
  @Container
  static MongoDBContainer mongo = new MongoDBContainer("mongo:5.0.15");

  private InteractionServiceImpl service;

  @BeforeEach
  void setUp() {
    // direct your service’s Mongo client to the Testcontainer URI
    mongo.start();
    String uri = mongo.getReplicaSetUrl();
    client = MongoClients.create(mongo.getReplicaSetUrl());
    service = new InteractionServiceImpl(client);
  }

  @AfterEach
  void tearDown() {
      client.close();
      mongo.stop();
  }

  @Test
  void annotateInteraction_shouldProduceExpectedAnnotations() throws InterruptedException {
    InteractionRequest req = InteractionRequest.newBuilder()
        .setUserId("testUser")
        .setPayload("click")
        .setReceivedAt(Timestamp.newBuilder().setSeconds(1234).build())
        .build();

    BlockingQueue<InteractionResponse> queue = new LinkedBlockingQueue<>();
    StreamObserver<InteractionResponse> obs = new StreamObserver<>() {
      @Override public void onNext(InteractionResponse r) { queue.add(r); }
      @Override public void onError(Throwable t) { fail(t); }
      @Override public void onCompleted() {}
    };

    service.annotateInteraction(req, obs);
    InteractionResponse resp = queue.take();

    assertTrue(resp.getAnnotationsList().stream()
        .anyMatch(a -> a.getKey().equals("length") && a.getValue().equals("5")));
    assertTrue(resp.getAnnotationsList().stream()
        .anyMatch(a -> a.getKey().equals("received_epoch") && a.getValue().equals("1234")));
  }
}
