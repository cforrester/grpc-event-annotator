package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import interaction.*;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class InteractionServiceImpl 
    extends InteractionServiceGrpc.InteractionServiceImplBase {

    private final MongoCollection<Document> coll;
    public InteractionServiceImpl(MongoClient client) {
        MongoDatabase db = client.getDatabase("events_db");
        coll = db.getCollection("interactions");

    }




    @Override
    public StreamObserver<InteractionRequest> streamAnnotateInteractions(
        StreamObserver<InteractionResponse> responseObserver) {

        return new StreamObserver<InteractionRequest>() {
            private final List<Annotation> annotations = new ArrayList<>();

            @Override
            public void onNext(InteractionRequest req) {
            // annotate each incoming request
            annotations.add(
                Annotation.newBuilder()
                .setKey("length:" + req.getUserId())
                .setValue(String.valueOf(req.getPayload().length()))
                .build());
            }

            @Override
            public void onError(Throwable t) {
            // client errored
            responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
            // client done—send a single response
            InteractionResponse resp = InteractionResponse.newBuilder()
                .addAllAnnotations(annotations)
                .build();
            responseObserver.onNext(resp);
            responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void annotateInteraction(InteractionRequest req,
                                    StreamObserver<InteractionResponse> respObserver) {
        String userId = req.getUserId();
        String payload = req.getPayload();
        Timestamp receivedAt = req.getReceivedAt();

        // Build the Response
        InteractionResponse.Builder respB = InteractionResponse.newBuilder()
            .addAnnotations(Annotation.newBuilder()
                .setKey("length")
                .setValue(String.valueOf(payload.length()))
                .build())
            .addAnnotations(Annotation.newBuilder()
                .setKey("received_epoch")
                .setValue(String.valueOf(receivedAt.getSeconds()))
                .build());

        InteractionResponse response = respB.build();

        // --- NEW: persist to MongoDB ---
        List<Document> annDocs = new ArrayList<>();
        for (Annotation a : response.getAnnotationsList()) {
            annDocs.add(new Document("key", a.getKey())
                              .append("value", a.getValue()));
        }
        Document doc = new Document("userId",  userId)
            .append("payload",     payload)
            .append("receivedAt",  receivedAt.getSeconds())
            .append("annotations", annDocs);
        coll.createIndex(new Document("userId", 1));
        // --------------------------------
        // Return the annotated response
        respObserver.onNext(response);
        respObserver.onCompleted();
    }
}
