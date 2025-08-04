package com.vtaveira.main.factories;

import com.vtaveira.domain.service.UserService;
import lombok.experimental.UtilityClass;

import java.sql.SQLException;

@UtilityClass
public class UserServiceFactory {

  public static UserService getInstance() throws SQLException {
    return new UserService(UserRepositoryFactory.getInstance());
  }
}
