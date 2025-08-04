package com.vtaveira.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
  private Long id;
  private String content;
  private User sender;
  private User receiver;
  private MessageStatus status;
  private Date createdAt;

  public Message(User fromUser, User toUser, String content) {
    this.sender = fromUser;
    this.receiver = toUser;
    this.content = content;
    this.status = MessageStatus.PENDING;
  }
}
