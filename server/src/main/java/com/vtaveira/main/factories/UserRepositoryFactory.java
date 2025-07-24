package com.vtaveira.main.factories;

import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.infra.config.DatabaseConfig;
import com.vtaveira.infra.persistence.repository.RelationalUserRepository;

import java.sql.SQLException;

public class UserRepositoryFactory {

  private UserRepositoryFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static UserRepository getInstance() throws SQLException {
    return new RelationalUserRepository(DatabaseConfig.getConnectionSource());
  }
}