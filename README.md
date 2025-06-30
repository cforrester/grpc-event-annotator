## Project Overview

This microservice ingests user interactions via gRPC, publishes them to RabbitMQ, processes them asynchronously in a separate “annotator” service (adds metadata and persists to MongoDB), and supports mutual TLS (mTLS) for secure communication.

**Components:**
- **gRPC Server** (`service.GrpcServer`):  
  - Exposes `AnnotateInteraction` RPC  
  - Publishes raw `InteractionRequest` messages to RabbitMQ  
- **Annotator Service** (`service.AsyncAnnotator`):  
  - Consumes from `interaction_requests` queue  
  - Enriches each message with annotations (e.g. payload length, timestamp)  
  - Persists enriched documents to MongoDB (`events_db.interactions`)  
  - Publishes annotated responses to `interaction_responses` (optional)  
- **MongoDB**: Stores annotated interactions  
- **RabbitMQ**: Message broker between gRPC server and annotator  
- **mTLS**: TLS for server+client, enforcing client certificates signed by your CA  

## Prerequisites

- Java 11 SDK  
- Maven 3.x  
- Docker & Docker Compose  
- `openssl` (for cert generation)

## TLS Certificate Setup

> **Do not** commit private keys to Git. Add `certs/*.key` to `.gitignore`.

1. **Create a `certs/` folder** at the project root.  
2. **Generate a self-signed CA**:
   ```bash
   cd certs
   openssl genrsa -out ca.key 4096
   openssl req -x509 -new -nodes \
     -key ca.key -days 3650 \
     -subj "/CN=Example CA" \
     -out ca.crt
   ```
3. **Server certificate**:
   ```bash
   openssl genrsa -out server.key 4096
   openssl req -new -key server.key \
     -subj "/CN=localhost" \
     -out server.csr
   openssl x509 -req -in server.csr \
     -CA ca.crt -CAkey ca.key -CAcreateserial \
     -days 365 -out server.crt
   ```
4. **Client certificate**:
   ```bash
   openssl genrsa -out client.key 4096
   openssl req -new -key client.key \
     -subj "/CN=grpc-client" \
     -out client.csr
   openssl x509 -req -in client.csr \
     -CA ca.crt -CAkey ca.key -CAcreateserial \
     -days 365 -out client.crt
   ```

## Local Build

```bash
# compile & package the fat JAR
mvn clean package -DskipTests
```

## Running with Docker Compose

```bash
docker-compose down
docker-compose up --build -d
```

This brings up:
- **mongo** (MongoDB on 27017)  
- **rabbitmq** (AMQP on 5672 + management on 15672)  
- **grpc-server** (waits for RabbitMQ, then starts with mTLS)  
- **annotator** (consumes, annotates, and writes to MongoDB)

### Confirm Services

```bash
docker logs -f grpc-server   
docker logs -f annotator   
```

## Exercise the Pipeline

1. **Send a request** via the Java client on your host:
   ```bash
   java -cp target/grpc-event-annotator-0.1.0-SNAPSHOT.jar client.GrpcClient
   ```
2. **Check MongoDB** for the persisted document:
   ```bash
   docker exec -it mongo mongo events_db --quiet \
     --eval "printjson(db.interactions.find().pretty())"
   ```
3. **(Optional) Consume annotated responses**:
   ```bash
   docker exec -it rabbitmq rabbitmqadmin get \
     queue=interaction_responses requeue=false
   ```

## Cleanup

```bash
docker-compose down -v 
```

---

