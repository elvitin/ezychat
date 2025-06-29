package com.vtaveira.infra.persistence.mapper;

import com.vtaveira.domain.model.User;
import com.vtaveira.infra.persistence.entity.UserEntity;

public class UserMapper {
  public static UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setUsername(user.getUsername());
    entity.setFullName(user.getFullName());
    entity.setEmail(user.getEmail());
    entity.setPassword(user.getPassword());
    entity.setStatus(user.getStatus());
    return entity;
  }

  public static User toDomain(UserEntity entity) {
    User user = new User();
    user.setId(entity.getId());
    user.setUsername(entity.getUsername());
    user.setFullName(entity.getFullName());
    user.setEmail(entity.getEmail());
    user.setPassword(entity.getPassword());
    user.setStatus(entity.getStatus());
    return user;
  }
}
