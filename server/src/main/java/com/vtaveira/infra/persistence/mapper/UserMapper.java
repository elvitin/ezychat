package com.vtaveira.infra.persistence.mapper;

import com.vtaveira.domain.model.User;
import com.vtaveira.infra.persistence.entity.UserEntity;

public class UserMapper {

  private UserMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static UserEntity toEntity(User user) {
    var entity = new UserEntity();
    entity.setUsername(user.getUsername());
    entity.setFullName(user.getFullName());
    entity.setEmail(user.getEmail());
    entity.setPassword(user.getPassword());
    entity.setStatus(user.getStatus());
    return entity;
  }

  public static User toDomain(UserEntity entity) {
    var user = new User();
    user.setUsername(entity.getUsername());
    user.setFullName(entity.getFullName());
    user.setEmail(entity.getEmail());
    user.setPassword(entity.getPassword());
    user.setStatus(entity.getStatus());
    return user;
  }
}
