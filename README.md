# gRPC Server Implementation with Spring Boot

This project provides a simple example of a gRPC server implemented using Spring Boot. 
It showcases three types of gRPC methods: 
- Client Streaming (heavy file upload example)
- Server Streaming (real time notification example)
- Bidirectional Streaming (live chat example) 

Additionally, integration tests are included to demonstrate the functionality of these methods.

[Link to the client implementation](https://github.com/victormsti/grpc-client)

## Project Structure

- `src/main/proto`: Contains Protocol Buffer (proto) files that define the service and message types.
- `src/main/java`: Contains the Java implementation of the gRPC server and related classes.
- `src/test/java`: Includes integration tests for the gRPC server.

## Implemented gRPC Methods

### 1. Client Streaming
#### File Upload Service

The server implements a client-streaming service that allows clients to upload a file in chunks. The server processes the file as it receives chunks from the client.

### 2. Server Streaming
#### Notification Service

The server implements a server-streaming service for sending periodic notifications to clients. Clients connect and receive notifications every 5 seconds.

### 3. Bidirectional Streaming
#### Chat Service

The server implements a bidirectional-streaming service for real-time chat. Clients can send and receive messages asynchronously.

## Running the Server
To run the gRPC server, you can use the provided Spring Boot Maven plugin
```bash
./mvnw spring-boot:run
```

But first, remember to compile and generate the protobuf resources

```bash
./mvnw protobuf:compile
```
```bash
./mvnw protobuf:compile-custom
```

## Running Integration Tests
You can execute the integration tests using the following Maven command:
```bash
./mvnw test
```

Feel free to use and modify this project to suit your specific needs.
