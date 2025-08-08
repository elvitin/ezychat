package com.vtaveira.infra.persistence.mapper;

import com.vtaveira.domain.model.Password;
import com.vtaveira.domain.model.User;
import com.vtaveira.infra.persistence.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

  public UserEntity toEntity(User user) {
    var entity = new UserEntity();
    entity.setUsername(user.getUsername());
    entity.setFullName(user.getFullName());
    entity.setEmail(user.getEmail());
    entity.setPassword(user.getPassword().getValue());
    entity.setStatus(user.getStatus());
    return entity;
  }

  public User toDomain(UserEntity entity) {
    var user = new User();
    user.setUsername(entity.getUsername());
    user.setFullName(entity.getFullName());
    user.setEmail(entity.getEmail());
    user.setPassword(new Password(entity.getPassword()));
    user.setStatus(entity.getStatus());
    return user;
  }
}
