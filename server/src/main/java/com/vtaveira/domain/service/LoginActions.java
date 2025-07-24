package com.vtaveira.domain.service;

import com.vtaveira.domain.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.function.Consumer;


@Getter
@Accessors(fluent = true)
@Builder
public class LoginActions {
  private Consumer<User> onSuccess;
  private Consumer<String> onUserNotFound;
  private Consumer<String> onInvalidPassword;
}
