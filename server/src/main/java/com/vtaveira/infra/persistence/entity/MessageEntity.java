package com.vtaveira.infra.persistence.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.vtaveira.domain.model.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "messages")
public class MessageEntity {

  public static final String RECEIVER_FIELD = "receiverId";
  public static final String SENDER_FIELD = "senderId";
  public static final String STATUS_FIELD = "status";

  @DatabaseField(generatedId = true)
  private long id;

  @DatabaseField(canBeNull = false)
  private String content;

  @DatabaseField(foreign = true, canBeNull = false, readOnly = true, columnName = SENDER_FIELD)
  private UserEntity sender;

  @DatabaseField(foreign = true, canBeNull = false, readOnly = true, columnName = RECEIVER_FIELD)
  private UserEntity receiver;

  @DatabaseField(canBeNull = false)
  private MessageStatus status;

  @DatabaseField(canBeNull = false, readOnly = true, columnDefinition = "CURRENT_TIMESTAMP")
  private Date createdAt;
}
