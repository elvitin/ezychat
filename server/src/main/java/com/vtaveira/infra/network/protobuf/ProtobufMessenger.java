package com.vtaveira.infra.network.protobuf;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.gateways.Messenger;
import com.vtaveira.domain.gateways.MessengerData;
import com.vtaveira.server.ConnectionHandler;
import com.vtaveira.server.ConnectionsManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;


@Slf4j
public class ProtobufMessenger implements Messenger {


  private final ConnectionsManager connectionsManager;
  private final ConnectionHandler currentConnection;

  public ProtobufMessenger(ConnectionsManager connectionsManager, ConnectionHandler currentConnection) {
    this.connectionsManager = connectionsManager;
    this.currentConnection = currentConnection;
  }

  @Override
  public void markAsAvailable(String uniqueIdentity) {
    this.currentConnection.setIdentity(uniqueIdentity);
    this.connectionsManager.changeIdentity(this.currentConnection.ipPort, uniqueIdentity);
    this.currentConnection.events().onWriteDataError.andThen(_ -> this.connectionsManager.unregister(uniqueIdentity));
    this.currentConnection.events().onReadDataError.andThen(_ -> this.connectionsManager.unregister(uniqueIdentity));
    this.currentConnection.events().onSetupStreamsError.andThen(_ -> this.connectionsManager.unregister(uniqueIdentity));
  }

  @Override
  public Optional<String> getAvailableIdentity() {
    return Optional.ofNullable(this.currentConnection.getIdentity());
  }

  @Override
  public void sent(MessengerData message) {
    ProtobufMapperRegistry.getFromDomain(message).ifPresent(mapper -> {
      var proto = this.toProto(mapper, message);
      this.currentConnection.writeData(proto.toByteArray());
    });
  }

  @Override
  public void sentTo(MessengerData message, String uniqueIdentity) {
    ProtobufMapperRegistry.getFromDomain(message).ifPresent(mapper -> {
      var proto = this.toProto(mapper, message);
      this.connectionsManager.get(uniqueIdentity)
          .ifPresent(connection -> connection.writeData(proto.toByteArray()));
    });
  }

  @Override
  public void sentTo(MessengerData message, String... uniqueIdentities) {
    Arrays.stream(uniqueIdentities).forEach(id -> this.sentTo(message, id));
  }

  @SuppressWarnings("unchecked")
  private <T extends MessengerData> WrapperMessage toProto(ProtobufMapper<?> mapper, T data) {
    return ((ProtobufMapper<T>) mapper).toProto(data);
  }
}
