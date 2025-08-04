package com.vtaveira.main.factories;

import com.vtaveira.domain.service.MessageService;
import lombok.experimental.UtilityClass;

import java.sql.SQLException;

@UtilityClass
public class MessageServiceFactory {

  public static MessageService getInstance() throws SQLException {
    return new MessageService(MessageRepositoryFactory.getInstance(), UserRepositoryFactory.getInstance());
  }
}
