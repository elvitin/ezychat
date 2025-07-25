package com.vtaveira.domain.repository;

import com.vtaveira.domain.model.User;

import java.util.Optional;

public interface UserRepository {
  void save(User user);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
