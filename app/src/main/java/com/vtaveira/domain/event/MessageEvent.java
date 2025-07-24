package com.vtaveira.domain.event;


public sealed interface MessageEvent {
  record LoginResponse(com.vtaveira.builds.java.LoginResponse response) implements MessageEvent {
  }

  record PrivateMessage(com.vtaveira.builds.java.PrivateMessage message) implements MessageEvent {
  }

  record Ack(com.vtaveira.builds.java.Ack ack) implements MessageEvent {
  }
}
