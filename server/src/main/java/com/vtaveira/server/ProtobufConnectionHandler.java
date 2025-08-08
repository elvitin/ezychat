package com.vtaveira.server;

import com.google.protobuf.InvalidProtocolBufferException;
import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.usecases.interfaces.DomainUseCase;
import com.vtaveira.infra.network.protobuf.ProtobufMapperRegistry;
import lombok.extern.slf4j.Slf4j;


import java.net.Socket;

@Slf4j
public class ProtobufConnectionHandler extends ConnectionHandler {

  private final PayloadHandlerFactory factory;

  public ProtobufConnectionHandler(Socket socket) {
    this(socket, new SocketConnectionEvents());
  }

  public ProtobufConnectionHandler(Socket socket, SocketConnectionEvents events) {
    super(socket, events);
    this.factory = new PayloadHandlerFactory(this);
    this.setupListeners();
  }

  private void setupListeners() {
    this.events().onDataReceived = data -> {
      try {
        var payload = WrapperMessage.parseFrom(data);
        factory.getHandler(payload.getPayloadCase())
            .ifPresent(usecase -> dispatchToUseCase(usecase, payload));
      } catch (InvalidProtocolBufferException e) {
        log.error("error parsing protobuf message: {}", e.getMessage(), e);
      }
    };
  }

  private void dispatchToUseCase(DomainUseCase usecase, WrapperMessage payload) {
    ProtobufMapperRegistry
        .getFromPayload(payload.getPayloadCase())
        .ifPresent(mapper -> usecase.execute(mapper.fromProto(payload)));
  }
}
