package com.vtaveira.domain.service;

import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.MessageRepository;

import java.util.List;

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
}
