package com.vtaveira.domain.service;

public class SendPrivateMessageActions {

  Runnable onMessageSaved = () -> {
  };

  public SendPrivateMessageActions onMessageSaved(Runnable action) {
    this.onMessageSaved = action;
    return this;
  }
}
