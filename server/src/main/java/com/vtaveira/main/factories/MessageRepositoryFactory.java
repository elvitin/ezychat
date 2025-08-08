package com.vtaveira.main.factories;

import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.infra.persistence.repository.RelationalMessageRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageRepositoryFactory {

  public MessageRepository getInstance() {
    return new RelationalMessageRepository(DatabaseConfig.getConnectionSource());
  }
}
