package com.vtaveira.main.factories;

import com.vtaveira.domain.service.UserService;

import java.sql.SQLException;

public class UserServiceFactory {

  private UserServiceFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static UserService getInstance() throws SQLException {
    return new UserService(UserRepositoryFactory.getInstance());
  }
}
