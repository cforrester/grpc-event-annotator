package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
    public static void main(String[] args) throws Exception {
        // 1️⃣ Read the URI (set by Docker Compose)
        String mongoUri = System.getenv().getOrDefault("MONGO_URI",
            "mongodb://localhost:27017");
        MongoClient mongoClient = MongoClients.create(mongoUri);

        // 2️⃣ Pass it into your service impl
        InteractionServiceImpl svc = new InteractionServiceImpl(mongoClient);

        int port = 50051;
        Server server = ServerBuilder.forPort(port)
            .addService(svc)
            .build()
            .start();
        System.out.println("Server started on port " + port);
        server.awaitTermination();
    }
}
