package com.vtaveira.infra.persistence.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.vtaveira.domain.model.Message;
import com.vtaveira.domain.model.MessageStatus;
import com.vtaveira.domain.repository.MessageRepository;
import com.vtaveira.infra.persistence.entity.MessageEntity;
import com.vtaveira.infra.persistence.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class RelationalMessageRepository implements MessageRepository {

  private final Dao<MessageEntity, Long> messageDao;

  public RelationalMessageRepository(ConnectionSource connectionSource) throws SQLException {
    this.messageDao = DaoManager.createDao(connectionSource, MessageEntity.class);
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
}
