package com.vtaveira.client.network;

import com.vtaveira.builds.java.WrapperMessage;
import com.vtaveira.domain.event.EventBus;
import com.vtaveira.domain.event.MessageEvent;
import com.vtaveira.domain.event.UserStatusEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class ProtobufHandler {

  private static ProtobufHandler instance;
  private final EventBus eventBus = EventBus.getInstance();

  public static synchronized ProtobufHandler getInstance() {
    if (ProtobufHandler.instance == null) {
      ProtobufHandler.instance = new ProtobufHandler();
    }
    return ProtobufHandler.instance;
  }

  public void handleIncomingMessage(WrapperMessage message) {
    switch (message.getPayloadCase()) {
      case LOGINRESPONSE -> this.handleLoginResponse(message);
      case PRIVATEMESSAGE -> this.handlePrivateMessage(message);
      case USERSTATUSUPDATE -> this.handleUserStatusUpdate(message);
      case ACK -> this.handleAck(message);
      default -> log.warn("unrecognized message type: {}", message.getPayloadCase());
    }
  }

  private void handleLoginResponse(WrapperMessage message) {
    var loginResponse = message.getLoginResponse();
    eventBus.publish(new MessageEvent.LoginResponse(loginResponse));
  }

  private void handlePrivateMessage(WrapperMessage message) {
    var privateMessage = message.getPrivateMessage();
    eventBus.publish(new MessageEvent.PrivateMessage(privateMessage));
  }

  private void handleUserStatusUpdate(WrapperMessage message) {
    var statusUpdate = message.getUserStatusUpdate();
    eventBus.publish(new UserStatusEvent(
        statusUpdate.getUsername(),
        statusUpdate.getStatus()
    ));
  }

  private void handleAck(WrapperMessage message) {
    var ack = message.getAck();
    eventBus.publish(new MessageEvent.Ack(ack));
  }
}
