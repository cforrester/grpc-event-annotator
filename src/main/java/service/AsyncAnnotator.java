package service;

import java.util.List;
import com.mongodb.client.*;
import com.rabbitmq.client.*;
import interaction.InteractionRequest;
import interaction.Annotation;
import interaction.InteractionResponse;
import com.google.protobuf.Timestamp;
import org.bson.Document;

public class AsyncAnnotator {
  public static void main(String[] args) throws Exception {
    
    String mongoUri = System.getenv("MONGO_URI");
    String rabbitUri = System.getenv("RABBITMQ_URI");
    MongoClient mongo = MongoClients.create(mongoUri);

    MongoCollection<Document> coll = mongo.getDatabase("events_db")
                                          .getCollection("interactions");
    ConnectionFactory cf = new ConnectionFactory();
    cf.setUri(rabbitUri);
    Connection conn = cf.newConnection();
    Channel ch = conn.createChannel();
    ch.queueDeclare("interaction_requests", true, false, false, null);

    System.out.println("Annotator listening for messages...");
    DeliverCallback dc = (consumerTag, delivery) -> {
      InteractionRequest req = InteractionRequest.parseFrom(delivery.getBody());
      // simple annotation logic
      Annotation length = Annotation.newBuilder()
          .setKey("length")
          .setValue(String.valueOf(req.getPayload().length()))
          .build();
      Annotation ts = Annotation.newBuilder()
          .setKey("received_epoch")
          .setValue(String.valueOf(req.getReceivedAt().getSeconds()))
          .build();
      InteractionResponse resp = InteractionResponse.newBuilder()
          .addAnnotations(length)
          .addAnnotations(ts)
          .build();
      // persist to Mongo
      
      long seconds = req.getReceivedAt().getSeconds();
      // convert to milliseconds
      Date receivedAtDate = new Date(seconds * 1000L);
      Document doc = new Document("user_id", req.getUserId())
           .append("payload", req.getPayload())
           .append("received_at", receivedAtDate)
           .append("annotations", 
               List.of(
                 new Document("key", length.getKey()).append("value", length.getValue()),
                 new Document("key", ts.getKey()).append("value", ts.getValue())
               )
           );
      coll.insertOne(doc);
      ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };
    ch.basicConsume("interaction_requests", false, dc, consumerTag -> {});
  }
}
