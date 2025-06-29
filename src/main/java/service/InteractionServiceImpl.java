package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.*;
import interaction.*;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class InteractionServiceImpl 
    extends InteractionServiceGrpc.InteractionServiceImplBase {

    String rabbitUri = System.getenv("RABBITMQ_URI");
    private final MongoCollection<Document> coll;
    private final Channel rabbitChannel;

    public InteractionServiceImpl(MongoClient client) {
        MongoDatabase db = client.getDatabase("events_db");
        coll = db.getCollection("interactions");
        Channel channel;
        try {
            ConnectionFactory cf = new ConnectionFactory();
            cf.setUri(rabbitUri);                  
            Connection conn = cf.newConnection();  
            channel = conn.createChannel();
            channel.queueDeclare("interaction_requests", true, false, false, null);
        } catch (URISyntaxException
               | IOException
               | TimeoutException
               | NoSuchAlgorithmException
               | KeyManagementException e) {
            throw new RuntimeException("Failed to initialize RabbitMQ", e);
        }
        rabbitChannel = channel;
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
    public StreamObserver<InteractionRequest> chatAnnotate(
        StreamObserver<InteractionResponse> responseObserver) {

        return new StreamObserver<InteractionRequest>() {
            @Override
            public void onNext(InteractionRequest req) {
            // For each incoming request, immediately send back an annotation response:
            InteractionResponse resp = InteractionResponse.newBuilder()
                .addAnnotations(Annotation.newBuilder()
                    .setKey("echo_user")
                    .setValue(req.getUserId())
                    .build())
                .addAnnotations(Annotation.newBuilder()
                    .setKey("echo_payload")
                    .setValue(req.getPayload())
                    .build())
                .build();
            responseObserver.onNext(resp);
            }

            @Override
            public void onError(Throwable t) {
            // Propagate errors to the client
            responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
            // Client has finished sending—close the response stream
            responseObserver.onCompleted();
            }
        };
    }


    @Override
    public void annotateInteraction(InteractionRequest req,
                                    StreamObserver<InteractionResponse> obs) {
        try {
            byte[] body = req.toByteArray();
            rabbitChannel.basicPublish("", "interaction_requests", MessageProperties.PERSISTENT_BASIC, body);
            obs.onNext(InteractionResponse.newBuilder().build());  // immediate ack
            obs.onCompleted();
            } catch (Exception e) {
            obs.onError(e);
        }
    }
}
