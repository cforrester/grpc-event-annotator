# Stage 1: build the fat JAR
FROM maven:3-jdk-11 AS builder
WORKDIR /build

# copy your sources and the wait-for-it script into the builder
COPY pom.xml .  
COPY src     ./src  
COPY wait-for-it.sh ./wait-for-it.sh
RUN chmod +x ./wait-for-it.sh

RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM openjdk:11-jdk-slim
WORKDIR /app

# pull in your built jar
COPY --from=builder /build/target/grpc-event-annotator-0.1.0-SNAPSHOT.jar ./app.jar

# also pull in the wait-for-it script from the builder
COPY --from=builder /build/wait-for-it.sh ./wait-for-it.sh
RUN chmod +x ./wait-for-it.sh

# copy your TLS certs
COPY certs ./certs

EXPOSE 50051

ENTRYPOINT [ \
  "/app/wait-for-it.sh", \
  "rabbitmq:5672", \
  "-t", "60", \
  "--", \
  "java", \
  "-cp", "app.jar", \
  "service.GrpcServer" \
]
