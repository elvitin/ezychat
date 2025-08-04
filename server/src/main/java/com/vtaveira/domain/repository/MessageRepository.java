package com.vtaveira.domain.repository;

import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.repository.dto.SavedMessage;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

  List<Message> getAllUndelivered(String username);

  Optional<SavedMessage> upsert(Message message);
}