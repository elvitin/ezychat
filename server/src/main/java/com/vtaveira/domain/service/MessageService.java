package com.vtaveira.domain.service;

import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.domain.repository.UserRepository;
import com.vtaveira.domain.service.dto.SavedMessage;
import com.vtaveira.domain.service.dto.UserKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
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

  public void save(String fromUsername, String toUsername, String content, SendPrivateMessageActions actions) {
    var fromUser = this.userRepository.findByUsername(fromUsername);
    var toUser = this.userRepository.findByUsername(toUsername);
    if (fromUser.isPresent() && toUser.isPresent()) {
      var message = this.messageRepository.upsert(new Message(fromUser.get(), toUser.get(), content));
      message.ifPresentOrElse(savedMessage -> {
        log.info("message saved from {} to {}: {}", fromUsername, toUsername, content);
        actions.onMessageSaved.accept(new SavedMessage(
            savedMessage.id(),
            fromUser.get().getFullName(),
            content,
            savedMessage.createdAt())
        );
      }, () -> log.warn("failed to save message, message is null - from: {}, to: {}", fromUsername, toUsername));
    } else {
      log.warn("failed to save message, user not found - from: {}, to: {}", fromUsername, toUsername);
    }
  }
}
