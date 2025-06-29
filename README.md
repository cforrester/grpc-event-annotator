
# grpc-event-annotator

A Java-based gRPC microservice for ingesting and annotating user interactions. Built with gRPC, Protocol Buffers, and MongoDB, and containerized via Docker.

## Features

* **Unary RPC**: Annotate a single interaction per call.
* **Client-Streaming RPC**: Batch multiple interactions in a single stream and receive annotations in one response.
* **Persistence**: Stores raw interactions and annotations in MongoDB.
* **Dockerized**: Multi-stage Dockerfile produces a lightweight, runnable container.
* **Compose Support**: Docker Compose configuration to launch the service and MongoDB together.
* **JUnit Tests**: Integration tests using Testcontainers for MongoDB.
* Bidirectional streaming for live annotate/chat
* Asynchronous ingestion via RabbitMQ

## Prerequisites

* Java 11+
* Maven 3.6+
* Docker & Docker Compose (for containerized deployment)

## Getting Started

### Clone the repository

```bash
git clone https://github.com/cforrester/grpc-event-annotator.git
cd grpc-event-annotator
```

### Build the project

```bash
mvn clean package
```

### Run locally (without Docker)

1. Start MongoDB on localhost:27017.
2. Run the server:

   ```bash
   java -cp target/grpc-event-annotator-0.1.0-SNAPSHOT.jar service.GrpcServer
   ```
3. In another terminal, run the client:

   ```bash
   java -cp target/grpc-event-annotator-0.1.0-SNAPSHOT.jar client.GrpcClient
   ```

### Run with Docker Compose

```bash
docker-compose up --build
```

* MongoDB and gRPC server start automatically.
* To test client:

  ```bash
  java -cp target/grpc-event-annotator-0.1.0-SNAPSHOT.jar client.GrpcClient
  ```

## Testing

Integration tests use Testcontainers:

```bash
mvn clean test
```

## Feature Roadmap

* **\[ ] Unary and streaming authentication**: Add mTLS and JWT support.
* **\[ ] REST/JSON gateway**: Provide HTTP/JSON API via grpc-gateway or Envoy.
* **\[ ] Metrics & tracing**: Integrate Prometheus and OpenTelemetry.
* **\[ ] Historical query API**: Expose RPCs to fetch stored interaction history.
* **\[ ] Bulk re-annotation job**: On-demand batch processing of stored data.
* **\[ ] Kubernetes deployment**: Helm chart with autoscaling and probes.
* **\[ ] ML-powered annotations**: Plug in sentiment analysis or image tagging models.

## Contributing

1. Fork the repo.
2. Create a feature branch.
3. Submit a pull request.

## License

MIT License. See [LICENSE](LICENSE).
