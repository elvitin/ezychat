package com.vtaveira.main.factories;

import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.infra.persistence.repository.RelationalMessageRepository;

import java.sql.SQLException;

public class MessageRepositoryFactory {

  private MessageRepositoryFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static MessageRepository getInstance() throws SQLException {
    return new RelationalMessageRepository(DatabaseConfig.getConnectionSource());
  }
}
