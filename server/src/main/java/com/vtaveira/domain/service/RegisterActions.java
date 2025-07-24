package com.vtaveira.domain.service;

public class RegisterActions {
  Runnable onUserRegistered = () -> {
    throw new UnsupportedOperationException("user registered action not implemented");
  };
  Runnable onEmailAlreadyInUse = () -> {
    throw new UnsupportedOperationException("email already in use action not implemented");
  };
  Runnable onUsernameAlreadyInUse = () -> {
    throw new UnsupportedOperationException("username already in use action not implemented");
  };

  public RegisterActions onUserRegistered(Runnable action) {
    this.onUserRegistered = action;
    return this;
  }

  public RegisterActions onEmailAlreadyInUse(Runnable action) {
    this.onEmailAlreadyInUse = action;
    return this;
  }

   public RegisterActions onUsernameAlreadyInUse(Runnable action) {
    this.onUsernameAlreadyInUse = action;
    return this;
   }
}
