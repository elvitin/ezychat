package com.vtaveira.main.factories;

import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.infra.persistence.repository.RelationalMessageRepository;
import lombok.experimental.UtilityClass;

import java.sql.SQLException;

@UtilityClass
public class MessageRepositoryFactory {

  public static MessageRepository getInstance() throws SQLException {
    return new RelationalMessageRepository(DatabaseConfig.getConnectionSource());
  }
}
