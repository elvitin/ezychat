package com.vtaveira.domain.service;

import com.vtaveira.domain.model.User;
import com.vtaveira.domain.repository.UserRepository;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean register(String username, String fullName, String email, String password) {
    var user = new User(username, fullName, email, password);
    if (password.length() < 6) {
      return false;
    }
    this.userRepository.save(user);
    return true;
  }
}
