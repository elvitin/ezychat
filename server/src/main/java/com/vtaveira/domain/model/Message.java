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
  private String content;
  private User sender;
  private User receiver;
  private MessageStatus status;
  private Date createdAt;
}
