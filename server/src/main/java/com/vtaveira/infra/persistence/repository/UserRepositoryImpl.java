package com.vtaveira.infra.persistence.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.infra.persistence.entity.UserEntity;
import com.vtaveira.infra.persistence.mapper.UserMapper;

import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

  private final Dao<UserEntity, Long> userDao;

  public UserRepositoryImpl(ConnectionSource connectionSource) throws SQLException {
    this.userDao = DaoManager.createDao(connectionSource, UserEntity.class);
  }

  public void save(User user) {
    try {
      this.userDao.createOrUpdate(UserMapper.toEntity(user));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<User> findById(long id) {
    return Optional.empty();
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.empty();
  }
}
