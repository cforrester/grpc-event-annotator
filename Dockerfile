# Stage 1: build the fat JAR
FROM maven:3-jdk-11 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/grpc-event-annotator-0.1.0-SNAPSHOT.jar ./app.jar

EXPOSE 50051
ENTRYPOINT ["java","-cp","app.jar","service.GrpcServer"]
#ENTRYPOINT ["java","-jar","/app/app.jar"]
