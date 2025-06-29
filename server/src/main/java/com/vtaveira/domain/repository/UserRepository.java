package com.vtaveira.domain.repository;

import com.vtaveira.domain.model.User;

import java.util.Optional;

public interface UserRepository {
  void save(User user);

  Optional<User> findById(long id);

  Optional<User> findByUsername(String username);
}
