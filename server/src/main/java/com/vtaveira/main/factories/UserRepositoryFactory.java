package com.vtaveira.main.factories;

import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.infra.persistence.repository.RelationalUserRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRepositoryFactory {

  public UserRepository getInstance() {
    return new RelationalUserRepository(DatabaseConfig.getConnectionSource());
  }
}