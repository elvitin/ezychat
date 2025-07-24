package com.vtaveira.domain.service;

public class SendPrivateMessageActions {

  Runnable onMessageSaved = () -> {
  };

  Runnable onRecipientNotFound = () -> {
  };

  public SendPrivateMessageActions onMessageSaved(Runnable action) {
    this.onMessageSaved = action;
    return this;
  }

  public SendPrivateMessageActions onRecipientNotFound(Runnable action) {
    this.onRecipientNotFound = action;
    return this;
  }
}
