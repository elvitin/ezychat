package com.vtaveira.infra.persistence.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.infra.persistence.entity.UserEntity;
import com.vtaveira.infra.persistence.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
public class RelationalUserRepository implements UserRepository {

  private final Dao<UserEntity, Long> userDao;

  public RelationalUserRepository(ConnectionSource connectionSource) {
    try {
      this.userDao = DaoManager.createDao(connectionSource, UserEntity.class);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void save(User user) {
    try {
      this.userDao.createOrUpdate(UserMapper.toEntity(user));
    } catch (SQLException e) {
      log.error("error saving user: {}", user, e);
    }
  }


  @Override
  public Optional<User> findByUsername(String username) {
    try {
      var user = this.userDao.queryForEq("username", username);
      return Optional.of(UserMapper.toDomain(user.getFirst()));
    } catch (SQLException | NoSuchElementException e) {
      log.error("error finding user by username {}", username, e);
      return Optional.empty();
    }
  }


  @Override
  public Optional<User> findByEmail(String email) {
    try {
      var user = this.userDao.queryForEq("email", email);
      return Optional.of(UserMapper.toDomain(user.getFirst()));
    } catch (SQLException | NoSuchElementException e) {
      log.error("error finding user by email {}", email, e);
      return Optional.empty();
    }
  }
}

