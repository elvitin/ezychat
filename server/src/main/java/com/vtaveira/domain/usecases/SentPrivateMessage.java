package com.vtaveira.domain.usecases;

import com.vtaveira.domain.gateways.Messenger;
import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.domain.usecases.dto.PrivateMessageConfirmation;
import com.vtaveira.domain.usecases.dto.PrivateMessageToSent;
import com.vtaveira.domain.usecases.dto.SavedMessage;
import com.vtaveira.domain.usecases.interfaces.DomainUseCase;

public class SentPrivateMessage implements DomainUseCase<PrivateMessageToSent> {
  private final Messenger messenger;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  public SentPrivateMessage(Messenger messenger, UserRepository userRepository, MessageRepository messageRepository) {
    this.messenger = messenger;
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
  }

  @Override
  public void execute(PrivateMessageToSent data) {
    this.messenger.getAvailableIdentity().ifPresent(username ->
      this.userRepository.findByUsername(data.toUsername()).ifPresent(toUser -> {
        var fromUser = this.userRepository.findByUsername(username).get();
        this.messageRepository.upsert(new Message(fromUser, toUser, data.content())).ifPresent(message -> {
          this.messenger.sent(new PrivateMessageConfirmation(true, message.id()));
          this.messenger.sentTo(new SavedMessage(message.id(), username, toUser.getFullName(), data.content(), message.createdAt()), toUser.getUsername());
        });
      })
    );
  }
}
