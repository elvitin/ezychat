package com.vtaveira.domain.usecases;

import com.vtaveira.domain.gateways.Messenger;
import com.vtaveira.domain.model.Password;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.model.UserStatus;
import com.vtaveira.domain.repository.FileStorage;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.domain.usecases.dto.RegisterResult;
import com.vtaveira.domain.usecases.dto.RegisterStatus;
import com.vtaveira.domain.usecases.dto.UserToRegister;
import com.vtaveira.domain.usecases.interfaces.DomainUseCase;

public class RegisterUser implements DomainUseCase<UserToRegister> {

  private final UserRepository userRepository;
  private final Messenger messenger;
  private final FileStorage fileStorage;

  public RegisterUser(UserRepository userRepository, Messenger messenger, FileStorage fileStorage) {
    this.userRepository = userRepository;
    this.messenger = messenger;
    this.fileStorage = fileStorage;
  }

  @Override
  public void execute(UserToRegister data) {
    var user = User.builder()
        .username(data.username())
        .fullName(data.fullName())
        .email(data.email())
        .status(UserStatus.OFFLINE)
        .password(new Password(data.password()))
        .build();

    if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
      this.messenger.sent(new RegisterResult(false, RegisterStatus.USERNAME_ALREADY_IN_USE));
      return;
    }

    if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
      this.messenger.sent(new RegisterResult(false, RegisterStatus.EMAIL_ALREADY_IN_USE));
      return;
    }

    this.userRepository.save(user);
    data.profilePicture().ifPresent(file -> this.fileStorage.save(data.username(), file.type(), file.content()));
    this.messenger.sent(new RegisterResult(true, RegisterStatus.SUCCESS));
  }
}
