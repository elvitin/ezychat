package com.vtaveira.infra.persistence.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.model.MessageStatus;
import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.domain.repository.dto.SavedMessage;
import com.vtaveira.infra.persistence.entity.MessageEntity;
import com.vtaveira.infra.persistence.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class RelationalMessageRepository implements MessageRepository {

  private final Dao<MessageEntity, Long> messageDao;

  public RelationalMessageRepository(ConnectionSource connectionSource)  {
    try {
      this.messageDao = DaoManager.createDao(connectionSource, MessageEntity.class);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public List<Message> getAllUndelivered(String username) {
    try {
      Where<MessageEntity, Long> where = this.messageDao.queryBuilder().where();
      var messages = where.and(
          where.or(
              where.eq(MessageEntity.RECEIVER_FIELD, username),
              where.eq(MessageEntity.SENDER_FIELD, username)
          ),
          where.in(MessageEntity.STATUS_FIELD, MessageStatus.PENDING, MessageStatus.SENT, MessageStatus.FAILED)
      ).query();

      return MessageMapper.toDomainList(messages);
    } catch (SQLException e) {
      log.error("error retrieving unread messages for user: {}", username, e);
      return List.of();
    }
  }

  @Override
  public Optional<SavedMessage> upsert(Message message) {
    try {
      MessageEntity messageEntity = MessageMapper.toEntity(message);
      messageEntity.setCreatedAt(new Date());
      this.messageDao.createOrUpdate(messageEntity);
      log.info("message saved from {} to {}: {}", message.getSender().getUsername(), message.getReceiver().getUsername(), message.getContent());
      return Optional.of(new SavedMessage(messageEntity.getId(), messageEntity.getCreatedAt()));
    } catch (SQLException e) {
      log.error("failed to save message from {} to {}: {}", message.getSender().getUsername(), message.getReceiver().getUsername(), e.getMessage(), e);
      return Optional.empty();
    }
  }

  @Override
  public Set<String> getOpenChatUsernames(String username) {
    return Set.of();
  }
}
