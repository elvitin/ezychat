package com.vtaveira.infra.persistence.mapper;

import com.vtaveira.domain.model.Message;
import com.vtaveira.infra.persistence.entity.MessageEntity;

import java.util.List;


public class MessageMapper {

  private MessageMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Message> toDomainList(List<MessageEntity> entities) {
    return entities.stream()
        .map(MessageMapper::toDomain)
        .toList();
  }

  public static Message toDomain(MessageEntity entity) {
    var message = new Message();
    message.setContent(entity.getContent());
    message.setSender(UserMapper.toDomain(entity.getSender()));
    message.setReceiver(UserMapper.toDomain(entity.getReceiver()));
    message.setStatus(entity.getStatus());
    message.setCreatedAt(entity.getCreatedAt());
    return message;
  }

  public static MessageEntity toEntity(Message message) {
    var entity = new MessageEntity();
    entity.setContent(message.getContent());
    entity.setSender(UserMapper.toEntity(message.getSender()));
    entity.setReceiver(UserMapper.toEntity(message.getReceiver()));
    entity.setStatus(message.getStatus());
    entity.setCreatedAt(message.getCreatedAt());
    return entity;
  }
}
