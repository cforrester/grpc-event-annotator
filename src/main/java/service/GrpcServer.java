package service;

import java.io.File;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;

public class GrpcServer {
  public static void main(String[] args) throws Exception {

    String mongoUri = System.getenv("MONGO_URI");
    MongoClient mongoClient = MongoClients.create(mongoUri);
    InteractionServiceImpl svc = new InteractionServiceImpl(mongoClient);

    SslContext sslContext = GrpcSslContexts.forServer(
        new File("certs/server.crt"),
        new File("certs/server.key"))
      .trustManager(new File("certs/ca.crt"))
      .clientAuth(ClientAuth.REQUIRE)
      .build();

    Server server = NettyServerBuilder.forPort(50051)
        .sslContext(sslContext)
        .addService(svc)
        .build()
        .start();

    System.out.println("mTLS gRPC server listening on 50051");
    server.awaitTermination();
  }
}
