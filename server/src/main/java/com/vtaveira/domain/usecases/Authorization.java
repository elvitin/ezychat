package com.vtaveira.domain.usecases;

import com.vtaveira.domain.gateways.Messenger;
import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.model.User;
import com.vtaveira.domain.model.UserStatus;
import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.domain.usecases.dto.*;
import com.vtaveira.domain.usecases.interfaces.DomainUseCase;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Authorization implements DomainUseCase<UserToAuth> {


  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final Messenger messenger;

  public Authorization(
      UserRepository userRepository,
      MessageRepository messageRepository,
      Messenger messenger
  ) {
    this.userRepository = userRepository;
    this.messenger = messenger;
    this.messageRepository = messageRepository;
  }

  @Override
  public void execute(UserToAuth userToAuth) {
    var username = userToAuth.username();
    var password = userToAuth.password();

    Optional<User> userOpt = this.userRepository.findByUsername(username);

    if (userOpt.isEmpty()) {
      messenger.sent(new LoginResult(false, LoginStatus.USER_NOT_FOUND));
      return;
    }

    var user = userOpt.get();
    var pass = user.getPassword();
    if (pass.isEqualTo(password)) {
      this.messenger.markAsAvailable(username);
      this.restorePendingMessages(username);
      this.announcePresence(username);
      return;
    }

    messenger.sent(new LoginResult(false, LoginStatus.INVALID_PASSWORD));
  }

  private void announcePresence(String username) {
    var usernames = this.messageRepository.getOpenChatUsernames(username);
    this.messenger.sentTo(new StatusUpdate(username, UserStatus.ONLINE), usernames.toArray(String[]::new));
  }

  private void restorePendingMessages(String username) {
    var groups = this.getPendingMessagesGroupedByConversation(username);
    messenger.sent(new GroupedMessages(groups));
  }

  public Map<UserKey, List<Message>> getPendingMessagesGroupedByConversation(String username) {
    return this.messageRepository.getAllUndelivered(username).stream().collect(Collectors.groupingBy(
        message -> {
          var sender = message.getSender();
          var receiver = message.getReceiver();
          return sender.getUsername().equals(username)
              ? new UserKey(receiver.getUsername(), receiver.getFullName())
              : new UserKey(sender.getUsername(), sender.getFullName());
        })
    );
  }
}
