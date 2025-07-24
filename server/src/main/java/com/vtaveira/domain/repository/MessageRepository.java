package com.vtaveira.domain.repository;

import com.vtaveira.domain.model.Message;

import java.util.List;

public interface MessageRepository {

  /**
   * Retrieves all undelivered messages for a specific user.
   *
   * @param username the username of the user
   * @return a list of undelivered messages for the user
   */
  List<Message> getAllUndelivered(String username);
}