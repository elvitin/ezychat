package com.vtaveira.domain.service;

import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MessageService {
  private final MessageRepository messageRepository;

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public Iterable<String> getPendingMessages(String username) {
    List<Message> messages = this.messageRepository.getAllUndelivered(username);
    return messages.stream()
        .map(Message::getContent)
        .toList();
  }

  public void sendPrivateMessage(String username, String receiverUsername, String content, SendPrivateMessageActions messageSentSuccessfully) {
    log.debug("{}{}{}{}", username, receiverUsername, content, messageSentSuccessfully);
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
