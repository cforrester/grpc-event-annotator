# gRPC Event Annotator

An asynchronous, secure microservice pipeline using gRPC, RabbitMQ, MongoDB, and OAuth2/mTLS.

## Project Overview

This service ingests user interactions via gRPC, publishes them to RabbitMQ, processes them asynchronously in a separate “annotator” service (adds metadata and persists to MongoDB), and supports:

- **mTLS** for gRPC server & client  
- **OAuth2** (Keycloak) with JWT access tokens, refresh tokens, scopes, and third-party IdP  
- **Asynchronous processing** via RabbitMQ  
- **Persistence** in MongoDB  

### Components

- **gRPC Server** (`service.GrpcServer`)  
  - RPC: `AnnotateInteraction(InteractionRequest) → InteractionResponse`  
  - Publishes raw interactions to RabbitMQ queue `interaction_requests`  
  - Secured via mTLS and OAuth2 (JWT + scopes)  
- **Annotator Service** (`service.AsyncAnnotator`)  
  - Consumes `interaction_requests`, enriches data, writes to MongoDB  
  - (Optionally) publishes annotated responses to `interaction_responses`  
- **Keycloak**: Authorization server (OAuth2 + JWT tokens)  
- **RabbitMQ**: Message broker  
- **MongoDB**: Stores annotated interactions  

## Prerequisites

- Java 11 SDK  
- Maven 3.x  
- Docker & Docker Compose  
- `openssl`, `keytool` (for cert generation on Keycloak & mTLS)

## TLS Certificate Setup

> **Never** commit private keys. Add `certs/*.key` to `.gitignore`.

1. Create `certs/` at repo root.  
2. **Generate CA**:
   ```bash
   cd certs
   openssl genrsa -out ca.key 4096
   openssl req -x509 -new -nodes \
     -key ca.key -days 3650 \
     -subj "/CN=Example CA" \
     -out ca.crt
   ```
3. **Service mTLS certs** (for gRPC server & client):
   ```bash
   # Server
   openssl genrsa -out server.key 4096
   openssl req -new -key server.key \
     -subj "/CN=localhost" \
     -out server.csr
   openssl x509 -req -in server.csr \
     -CA ca.crt -CAkey ca.key -CAcreateserial \
     -days 365 -out server.crt

   # Client
   openssl genrsa -out client.key 4096
   openssl req -new -key client.key \
     -subj "/CN=grpc-client" \
     -out client.csr
   openssl x509 -req -in client.csr \
     -CA ca.crt -CAkey ca.key -CAcreateserial \
     -days 365 -out client.crt
   ```
4. **Keycloak HTTPS cert** (optional):
   ```bash
   openssl genrsa -out kc.key 2048
   openssl req -new -x509 \
     -key kc.key -out kc.crt \
     -days 365 -subj "/CN=localhost"
   ```

## OAuth2 Realm Import (`realms.json`)


## Local Build

```bash
mvn clean package -DskipTests
```

## Running Everything

```bash
docker-compose down -v
docker-compose up --build -d
```

### Confirm Services

```bash
docker-compose logs -f mongo
docker-compose logs -f rabbitmq
docker-compose logs -f keycloak   # “Listening on: http://0.0.0.0:8080”
docker-compose logs -f grpc-server
#   mTLS gRPC server listening on 50051
docker-compose logs -f annotator
#   Annotator listening for messages...
```

## Access Keycloak

- **URL**: `http://<HOST_IP>:8080/auth`  
- **Admin**: `admin` / `admin`

## Exercise the Pipeline

1. **Obtain JWT** (password grant):
   ```bash
   TOKEN=$(curl -s \
     -d "grant_type=password" \
     -d "client_id=grpc-client" \
     -d "client_secret=<SECRET>" \
     -d "username=<USER>" \
     -d "password=<PASS>" \
     http://localhost:8080/realms/event-realm/protocol/openid-connect/token \
     | jq -r .access_token)
   ```
2. **Call gRPC** with the token:
   ```bash
   java -cp target/grpc-event-annotator-0.1.0-SNAPSHOT.jar client.GrpcClient --token "$TOKEN"
   ```
3. **Verify Mongo**:
   ```bash
   docker exec -it mongo mongo events_db --quiet \
     --eval "printjson(db.interactions.find().pretty())"
   ```

## Cleanup

```bash
docker-compose down -v
```
