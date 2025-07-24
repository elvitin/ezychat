package com.vtaveira.domain.service;

import com.vtaveira.domain.model.User;
import com.vtaveira.domain.model.UserStatus;
import com.vtaveira.domain.repository.UserRepository;

import java.util.Optional;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void register(User user, RegisterActions actions) {
    user.setStatus(UserStatus.OFFLINE);
    this.userRepository.findByUsername(user.getUsername()).ifPresentOrElse(
        _ -> actions.onUsernameAlreadyInUse.run(),
        () -> this.onUserFound(user, actions)
    );
  }

  private void onUserFound(User user, RegisterActions actions) {
    this.userRepository.findByEmail(user.getEmail()).ifPresentOrElse(
        _ -> actions.onEmailAlreadyInUse.run(),
        () -> {
          this.userRepository.save(user);
          actions.onUserRegistered.run();
        }
    );
  }

  public void login(String username, String password, LoginActions actions) {

    Optional<User> userOpt = this.userRepository.findByUsername(username);

    if (userOpt.isEmpty()) {
      actions.onUserNotFound().accept(username);
      return;
    }

    var user = userOpt.get();
    if (user.getPassword().equals(password)) {
      user.setStatus(UserStatus.ONLINE);
      this.userRepository.save(user);
      actions.onSuccess().accept(user);
      return;
    }

    actions.onInvalidPassword().accept(username);
  }
}
