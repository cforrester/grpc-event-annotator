package interaction;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.0)",
    comments = "Source: interaction.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class InteractionServiceGrpc {

  private InteractionServiceGrpc() {}

  public static final String SERVICE_NAME = "interaction.InteractionService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<interaction.InteractionRequest,
      interaction.InteractionResponse> getAnnotateInteractionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AnnotateInteraction",
      requestType = interaction.InteractionRequest.class,
      responseType = interaction.InteractionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<interaction.InteractionRequest,
      interaction.InteractionResponse> getAnnotateInteractionMethod() {
    io.grpc.MethodDescriptor<interaction.InteractionRequest, interaction.InteractionResponse> getAnnotateInteractionMethod;
    if ((getAnnotateInteractionMethod = InteractionServiceGrpc.getAnnotateInteractionMethod) == null) {
      synchronized (InteractionServiceGrpc.class) {
        if ((getAnnotateInteractionMethod = InteractionServiceGrpc.getAnnotateInteractionMethod) == null) {
          InteractionServiceGrpc.getAnnotateInteractionMethod = getAnnotateInteractionMethod =
              io.grpc.MethodDescriptor.<interaction.InteractionRequest, interaction.InteractionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AnnotateInteraction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  interaction.InteractionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  interaction.InteractionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InteractionServiceMethodDescriptorSupplier("AnnotateInteraction"))
              .build();
        }
      }
    }
    return getAnnotateInteractionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<interaction.InteractionRequest,
      interaction.InteractionResponse> getStreamAnnotateInteractionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamAnnotateInteractions",
      requestType = interaction.InteractionRequest.class,
      responseType = interaction.InteractionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<interaction.InteractionRequest,
      interaction.InteractionResponse> getStreamAnnotateInteractionsMethod() {
    io.grpc.MethodDescriptor<interaction.InteractionRequest, interaction.InteractionResponse> getStreamAnnotateInteractionsMethod;
    if ((getStreamAnnotateInteractionsMethod = InteractionServiceGrpc.getStreamAnnotateInteractionsMethod) == null) {
      synchronized (InteractionServiceGrpc.class) {
        if ((getStreamAnnotateInteractionsMethod = InteractionServiceGrpc.getStreamAnnotateInteractionsMethod) == null) {
          InteractionServiceGrpc.getStreamAnnotateInteractionsMethod = getStreamAnnotateInteractionsMethod =
              io.grpc.MethodDescriptor.<interaction.InteractionRequest, interaction.InteractionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamAnnotateInteractions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  interaction.InteractionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  interaction.InteractionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new InteractionServiceMethodDescriptorSupplier("StreamAnnotateInteractions"))
              .build();
        }
      }
    }
    return getStreamAnnotateInteractionsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static InteractionServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InteractionServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InteractionServiceStub>() {
        @java.lang.Override
        public InteractionServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InteractionServiceStub(channel, callOptions);
        }
      };
    return InteractionServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static InteractionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InteractionServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InteractionServiceBlockingStub>() {
        @java.lang.Override
        public InteractionServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InteractionServiceBlockingStub(channel, callOptions);
        }
      };
    return InteractionServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static InteractionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<InteractionServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<InteractionServiceFutureStub>() {
        @java.lang.Override
        public InteractionServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new InteractionServiceFutureStub(channel, callOptions);
        }
      };
    return InteractionServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void annotateInteraction(interaction.InteractionRequest request,
        io.grpc.stub.StreamObserver<interaction.InteractionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAnnotateInteractionMethod(), responseObserver);
    }

    /**
     * <pre>
     * client-streaming RPC
     * </pre>
     */
    default io.grpc.stub.StreamObserver<interaction.InteractionRequest> streamAnnotateInteractions(
        io.grpc.stub.StreamObserver<interaction.InteractionResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamAnnotateInteractionsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service InteractionService.
   */
  public static abstract class InteractionServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return InteractionServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service InteractionService.
   */
  public static final class InteractionServiceStub
      extends io.grpc.stub.AbstractAsyncStub<InteractionServiceStub> {
    private InteractionServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InteractionServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InteractionServiceStub(channel, callOptions);
    }

    /**
     */
    public void annotateInteraction(interaction.InteractionRequest request,
        io.grpc.stub.StreamObserver<interaction.InteractionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAnnotateInteractionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * client-streaming RPC
     * </pre>
     */
    public io.grpc.stub.StreamObserver<interaction.InteractionRequest> streamAnnotateInteractions(
        io.grpc.stub.StreamObserver<interaction.InteractionResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getStreamAnnotateInteractionsMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service InteractionService.
   */
  public static final class InteractionServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<InteractionServiceBlockingStub> {
    private InteractionServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InteractionServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InteractionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public interaction.InteractionResponse annotateInteraction(interaction.InteractionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAnnotateInteractionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service InteractionService.
   */
  public static final class InteractionServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<InteractionServiceFutureStub> {
    private InteractionServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InteractionServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new InteractionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<interaction.InteractionResponse> annotateInteraction(
        interaction.InteractionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAnnotateInteractionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ANNOTATE_INTERACTION = 0;
  private static final int METHODID_STREAM_ANNOTATE_INTERACTIONS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ANNOTATE_INTERACTION:
          serviceImpl.annotateInteraction((interaction.InteractionRequest) request,
              (io.grpc.stub.StreamObserver<interaction.InteractionResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_ANNOTATE_INTERACTIONS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamAnnotateInteractions(
              (io.grpc.stub.StreamObserver<interaction.InteractionResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAnnotateInteractionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              interaction.InteractionRequest,
              interaction.InteractionResponse>(
                service, METHODID_ANNOTATE_INTERACTION)))
        .addMethod(
          getStreamAnnotateInteractionsMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              interaction.InteractionRequest,
              interaction.InteractionResponse>(
                service, METHODID_STREAM_ANNOTATE_INTERACTIONS)))
        .build();
  }

  private static abstract class InteractionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    InteractionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return interaction.Interaction.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("InteractionService");
    }
  }

  private static final class InteractionServiceFileDescriptorSupplier
      extends InteractionServiceBaseDescriptorSupplier {
    InteractionServiceFileDescriptorSupplier() {}
  }

  private static final class InteractionServiceMethodDescriptorSupplier
      extends InteractionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    InteractionServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (InteractionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new InteractionServiceFileDescriptorSupplier())
              .addMethod(getAnnotateInteractionMethod())
              .addMethod(getStreamAnnotateInteractionsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
