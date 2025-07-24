package com.vtaveira.main.factories;

import com.vtaveira.domain.service.MessageService;

import java.sql.SQLException;

public class MessageServiceFactory {

  private MessageServiceFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static MessageService getInstance() throws SQLException {
    return new MessageService(MessageRepositoryFactory.getInstance());
  }
}
